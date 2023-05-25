/**
 * Copyright (c) 2018-present, A2 Rešitve d.o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package solutions.a2.oracle.jdbc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * SQL Mapping/tranlsation implementation using <a href="https://chronicle.software/">Chronicle Map</a>
 *
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxySqlTranslatorChronicle implements OraProxyTranslatorIntf {

	private final ChronicleMap<String, String> sqlMap;

	OraProxySqlTranslatorChronicle(final URL mappingFileUrl) throws SQLException {
		if ("file".equals(mappingFileUrl.getProtocol())) {
			try {
				sqlMap = ChronicleMapBuilder
							.of(String.class, String.class)
							.createOrRecoverPersistedTo(new File(mappingFileUrl.getFile()));
			} catch (IOException ioe) {
				throw new SQLException(
						String.format("Unable to instantiate Chronicle Map from file '%s'!", mappingFileUrl.getFile()),
						ioe);
			}
		} else {
			throw new SQLException("Only 'file' URL is currently supported for Chronicle Map!");
		}
	}


	/**
	 * translate
	 *   Translates SQL Query
	 * 
	 * @param source
	 * @return translated SQL string using predefined mapping
	 * @throws SQLException 
	 */
	@Override
	public String translate(final String source) throws SQLException {
		final String sqlId = OraProxyUtils.sql_id(source);
		return sqlMap.getOrDefault(sqlId, source);
	}

}
