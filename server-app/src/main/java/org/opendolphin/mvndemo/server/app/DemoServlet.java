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
package org.opendolphin.mvndemo.server.app;

import org.opendolphin.mvndemo.server.DemoDirector;
import javax.servlet.annotation.WebServlet;
import org.opendolphin.LogConfig;
import org.opendolphin.core.server.ServerDolphin;
import org.opendolphin.server.adapter.DolphinServlet;

@WebServlet(name = "mvndemo", urlPatterns = "/mvndemo/")
public class DemoServlet extends DolphinServlet {

	@Override
	protected void registerApplicationActions(ServerDolphin serverDolphin) {
		LogConfig.noLogs();
		serverDolphin.register(new DemoDirector());
	}

}
