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

import java.util.Iterator;

class DummyData implements Iterable<String[]> {

	private String[][] data;

	DummyData(String[] colNames, int maxRowCount) {
		data = new String[maxRowCount][colNames.length];
		for (int c = 0; c < colNames.length; c++) {
			for (int i = 0; i < maxRowCount; i++) {
				data[i][c] = colNames[c] + "_" + i;
			}
		}
	}

	public int getRowCount() {
		if (data != null) {
			return data.length;
		}
		return 0;
	}

	@Override
	public Iterator<String[]> iterator() {
		return new Iterator<String[]>() {
			int pos = 0;

			@Override
			public boolean hasNext() {
				return pos < data.length;
			}

			@Override
			public String[] next() {
				return data[pos++];
			}
		};
	}

	String[] getRow(int i) {
		if (data != null && data.length > i) {
			return data[i];
		} else {
			return new String[data[0].length];
		}
	}

}
