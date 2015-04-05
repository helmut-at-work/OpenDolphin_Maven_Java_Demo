/*
 * Copyright 2015 helmut.at.work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opendolphin.mvndemo.clientlazy;

import static org.opendolphin.mvndemo.share.DemoConstants.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.map.LazyMap;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.ModelStoreEvent;
import org.opendolphin.core.ModelStoreListener;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.client.comm.OnFinishedHandlerAdapter;

public class DemoController implements Initializable {

	/**
	 * Array with column names filled from server
	 */
	private String[] colNames = new String[0];
	private IntegerProperty used = new SimpleIntegerProperty(0);
	private IntegerProperty unused = new SimpleIntegerProperty(0);

	//
	@FXML
	private TableView tableLazy;
	private ObservableList<Integer> lazyRows = FXCollections.observableArrayList();
	private List<Map<String, SimpleStringProperty>> lazyModel = new ArrayList<>();

	@FXML
	private PieChart pieLazyUse;
	@FXML
	private Label pieLabel;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//Sending Command and get list with column names in the response.
		DemoApp.clientDolphin.send(CMD_GETLAZYCOLUMNS, new OnFinishedHandlerAdapter() {
			@Override
			public void onFinishedData(List<Map> data) {
				if (data != null && data.size() == 1) {
					//Create lazy table with colums from the response
					createTableLazy(data);
				}
			}

		});
		//
		// when starting, first fill the table with pm ids
		DemoApp.clientDolphin.send(CMD_LAZYTABLELOAD, new OnFinishedHandlerAdapter() {
			@Override
			public void onFinishedData(List<Map> data) {
				for (Map map : data) {
					lazyRows.addAll(map.values());
				}
				unused.setValue(lazyRows.size());
			}
		});
		//
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		PieChart.Data usedData = new PieChart.Data("Used", 23);
		usedData.pieValueProperty().bind(used);
		pieChartData.add(usedData);
		PieChart.Data unusedData = new PieChart.Data("unused", 100 - 23);
		unusedData.pieValueProperty().bind(unused);
		pieChartData.add(unusedData);
		pieLazyUse.setData(pieChartData);
		//
		used.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				pieLabel.setText(String.format("Zeilen: %d / %d ", newValue, lazyRows.size()));
			}
		});
		//
		DemoApp.clientDolphin.getClientConnector().setUiThreadHandler(new JavaFXUiThreadHandler());
	}

	private void createTableLazy(List<Map> data) {
		//Tabellen Spalten wieder sortieren.
		SortedMap<Integer, String> ms = new TreeMap<>(data.get(0));
		colNames = ms.values().toArray(colNames);
		// Model Listener
		initLazyModle(colNames);
		DemoApp.clientDolphin.addModelStoreListener(TYPE_LAZY, new ModelStoreListener() {

			@Override
			public void modelStoreChanged(ModelStoreEvent event) {
				PresentationModel pm = event.getPresentationModel();
				used.setValue(used.get() + 1);
				unused.setValue(lazyRows.size() - used.get());
				//
				int i = 0;
				for (Map<String, SimpleStringProperty> m : lazyModel) {
					String v = null;
					Attribute atr = pm.getAt(colNames[i]);
					if (atr != null) {
						v = (String) atr.getValue();
					}
					m.get(pm.getId()).setValue(v);
					i++;
				}
			}
		});
		//Create table columns
		TableColumn<Integer, String> tableColID = new TableColumn<>("rowID");
		tableColID.setSortable(true);
		tableColID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Integer, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Integer, String> cellDataFeature) {
				return new SimpleStringProperty(cellDataFeature.getValue().toString());
			}
		});
		tableLazy.getColumns().add(tableColID);

		for (int i = 0; i < colNames.length; i++) {
			TableColumn<Integer, String> tableCol = new TableColumn<>(colNames[i]);
			tableCol.setSortable(false);
			tableLazy.getColumns().add(tableCol);
			final int index = i;
			tableCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Integer, String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(TableColumn.CellDataFeatures<Integer, String> cellDataFeature) {
					String lazyId = cellDataFeature.getValue().toString();
					SimpleStringProperty placeholder = lazyModel.get(index).get(lazyId);
					if ("...".equals(placeholder.getValue())) {
						DemoApp.clientDolphin.getClientModelStore().withPresentationModel(lazyId, (pm) -> {
							/*NOP*/
						});
						// Wrong Way
						//						GetPresentationModelCommand pmc = new GetPresentationModelCommand();
						//						pmc.setPmId(lazyId);
						//						MonitorApp.clientDolphin.getClientConnector().send(pmc);
					}
					return placeholder;
				}
			});
		}
		//
		tableLazy.setItems(lazyRows);
	}

	private void initLazyModle(String[] colNames) {
		lazyModel.clear();
		Factory<SimpleStringProperty> factory = new Factory<SimpleStringProperty>() {
			public SimpleStringProperty create() {
				return new SimpleStringProperty("...");
			}
		};
		for (String colName : colNames) {
			Map<String, SimpleStringProperty> colProps = LazyMap.lazyMap(new HashMap<String, SimpleStringProperty>(), factory);
			lazyModel.add(colProps);
		}
	}

}
