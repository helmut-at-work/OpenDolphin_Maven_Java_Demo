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

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opendolphin.core.client.ClientDolphin;

public class DemoApp extends Application {

	static ClientDolphin clientDolphin;

	/**
	 * Starter for combined mode
	 *
	 * @param clientDolphin
	 */
	public static void debugStart(ClientDolphin clientDolphin) {
		DemoApp.clientDolphin = clientDolphin;
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/styles/Styles.css");

		stage.setTitle("Demo with JavaFX / OpenDolphin");
		stage.setScene(scene);
		stage.show();
	}

}
