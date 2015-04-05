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

import org.opendolphin.mvndemo.server.DemoDirector;
import java.util.logging.Level;
import org.opendolphin.LogConfig;

public class Starter {

	public static JavaFxInMemoryConfig config;

	public static void main(String[] args) throws Exception {

		//FIXME Batcher Fehler
		config = new JavaFxInMemoryConfig(100, 0);
		//This works
		//config = new JavaFxInMemoryConfig(10, 10);
		//System.out.println("InMemoryClientConnector slepp: " + ((InMemoryClientConnector) config.getClientDolphin().getClientConnector()).getSleepMillis());
		config.getServerDolphin().register(new DemoDirector());
		//LogConfig.noLogs(); // Do not log any dolphin messages.
		LogConfig.logOnLevel(Level.WARNING);
		org.opendolphin.mvndemo.clientlazy.DemoApp.debugStart(config.getClientDolphin());
	}
}
