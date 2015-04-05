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
package org.opendolphin.mvndemo.combined;

import org.opendolphin.core.client.comm.BlindCommandBatcher;
import org.opendolphin.core.client.comm.InMemoryClientConnector;
import org.opendolphin.core.client.comm.JavaFXUiThreadHandler;
import org.opendolphin.core.comm.DefaultInMemoryConfig;

class JavaFxInMemoryConfig extends DefaultInMemoryConfig {

	JavaFxInMemoryConfig() {
		this(400, 100);
	}

	/**
	 *
	 * @param defferMillis Zeit in Millis in der CMDs gesammelt werden.
	 * @param sleepMillis Zeit in Millis zwischen zwei Nachrichten?
	 */
	public JavaFxInMemoryConfig(int defferMillis, int sleepMillis) {
		BlindCommandBatcher batcher = new BlindCommandBatcher();
		batcher.setDeferMillis(defferMillis);
		batcher.setMergeValueChanges(true);

		InMemoryClientConnector imc = new InMemoryClientConnector(getClientDolphin(), batcher);
		imc.setSleepMillis(sleepMillis);
		imc.setServerConnector(getServerDolphin().getServerConnector());
		getClientDolphin().setClientConnector(imc);

		getClientDolphin().getClientConnector().setUiThreadHandler(new JavaFXUiThreadHandler());
		getServerDolphin().registerDefaultActions();
	}

}
