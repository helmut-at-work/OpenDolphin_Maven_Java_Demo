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
package org.opendolphin.mvndemo.server;

import static org.opendolphin.mvndemo.share.DemoConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opendolphin.core.comm.Command;
import org.opendolphin.core.comm.DataCommand;
import org.opendolphin.core.comm.GetPresentationModelCommand;
import org.opendolphin.core.comm.NamedCommand;
import org.opendolphin.core.server.DTO;
import org.opendolphin.core.server.Slot;
import org.opendolphin.core.server.action.DolphinServerAction;
import org.opendolphin.core.server.comm.ActionRegistry;
import org.opendolphin.core.server.comm.CommandHandler;
import org.opendolphin.core.server.comm.NamedCommandHandler;

public class DemoLazyTableAction extends DolphinServerAction {

	private static final Logger LOG = Logger.getLogger(DemoLazyTableAction.class.getName());
	//
	private int maxRowCount;
	private String[] colNames;
	private DummyData data;

	public DemoLazyTableAction(int maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	@Override
	public void registerIn(ActionRegistry actionRegistry) {

		//Init table columns
		actionRegistry.register(CMD_GETLAZYCOLUMNS, new NamedCommandHandler() {

			@Override
			public void handleCommand(NamedCommand command, List<Command> response) {
				LOG.log(Level.INFO, "Erstellen der Tabellen Spalten.");
				//colNames = new String[]{"id", "col01", "col02", "col03", "col04", "col05", "col101", "col102", "col103", "col104", "col105", "col201", "col202", "col203", "col204", "col205"};
				colNames = new String[]{"id", "col01", "col02", "col03", "col04", "col05"};
				data = new DummyData(colNames, maxRowCount);

				Map<Integer, String> cmdData = new HashMap<>();
				int i = 0;
				for (String colName : colNames) {
					cmdData.put(i++, colName);
				}
				response.add(new DataCommand(cmdData));
			}
		});

		//init table rows
		actionRegistry.register(CMD_LAZYTABLELOAD, new NamedCommandHandler() {

			@Override
			public void handleCommand(NamedCommand command, List<Command> response) {
				if (data != null) {
					try {
						for (int i = 0; i < data.getRowCount(); i++) {
							Map<String, Object> cmdData = new HashMap<String, Object>();
							cmdData.put("id", i);
							response.add(new DataCommand(cmdData));
						}
					} catch (Exception ex) {
						LOG.log(Level.SEVERE, null, ex);
					}
				}
			}
		});

		//get tabel row data
		actionRegistry.register(GetPresentationModelCommand.class, new CommandHandler<GetPresentationModelCommand>() {
			public void handleCommand(GetPresentationModelCommand cmd, List<Command> response) {
				String pmId = cmd.getPmId();
				if (pmId == null) {
					return;
				}
				if (getServerDolphin().getAt(pmId) == null) {
					String[] row = data.getRow(Integer.parseInt(pmId));
					List<Slot> slots = new ArrayList<>(row.length);
					int i = 0;
					for (String col : row) {
						slots.add(new Slot(colNames[i++], col));
					}
					DTO dto = new DTO(slots);
					presentationModel(pmId, TYPE_LAZY, dto);
				}
			}
		});

	}
}
