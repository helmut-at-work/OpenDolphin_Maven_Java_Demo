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

import java.util.logging.Level;
import javafx.application.Application;
import org.opendolphin.LogConfig;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientModelStore;
import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.HttpClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.JsonCodec;

public class Starter {

	/**
	 * Starter for URL: http://localhost:8080/server-app/mvndemo/
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClientDolphin clientDolphin = new ClientDolphin();
		clientDolphin.setClientModelStore(new ClientModelStore(clientDolphin));
		BlindCommandBatcher batcher = new BlindCommandBatcher();
		batcher.setDeferMillis(30);
		batcher.setMergeValueChanges(true);
		HttpClientConnector connector = new HttpClientConnector(clientDolphin, batcher, "http://localhost:8080/server-app/mvndemo/");
		connector.setCodec(new JsonCodec());
		connector.setUiThreadHandler(new JavaFXUiThreadHandler());
		LogConfig.logOnLevel(Level.WARNING);
		clientDolphin.setClientConnector(connector);

		DemoApp.clientDolphin = clientDolphin;
		Application.launch(DemoApp.class);
	}

}
