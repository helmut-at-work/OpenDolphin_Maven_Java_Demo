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
package org.opendolphin.mvndemo.share;

public class DemoConstants {

	//
	public static final String MODEL_MAXROW = unique("maxRowModell");
	public static final String MODEL_MAXROW_ATTRIBUTE = unique("maxRowCount");
	//DemoTable
	public static final String CMD_GETLAZYCOLUMNS = unique("getLazyColumns");
	public static final String CMD_LAZYTABLELOAD = unique("lazyTableInitialDataRequest");

	public static final String TYPE_LAZY = unique("LAZY");

	//Helper
	static String unique(String part) {
		return DemoConstants.class.getSimpleName() + "-" + part;
	}

	public static String qualify(String id, String attributeName) {
		StringBuilder sb = new StringBuilder(50);
		return sb.append(DemoConstants.class.getSimpleName()).append('-').append(id).append('.').append(attributeName).toString();
	}
}
