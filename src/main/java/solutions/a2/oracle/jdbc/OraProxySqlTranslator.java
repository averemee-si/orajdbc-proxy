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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

/**
 * 
 * SQL translation engine
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxySqlTranslator {

	private static OraProxySqlTranslator instance;
	private final OraProxyTranslatorIntf translator;

	private OraProxySqlTranslator(final boolean yamlStore, final String mappingFileUrl) throws SQLException {
		if (mappingFileUrl.startsWith("s3:")) {
			//TODO
			//TODO
			//TODO
			throw new SQLException("S3 support not implemented yet!");
		} else {
			final URL url;
			try {
				url = new URL(mappingFileUrl);
			} catch (MalformedURLException mfe) {
				throw new SQLException(
						String.format("Malformed URL '%s'", mappingFileUrl), mfe);
			}
			if (yamlStore) {
				translator = new OraProxySqlTranslatorMap(url);
			} else {
				translator = new OraProxySqlTranslatorChronicle(url);
			}
		}
	}

	/**
	 * Returns SQL Translation instance
	 * 
	 * @param yamlStore  when set to 'true' YAML format will be used for parsing
	 *                   input file, when set to 'false' Chronicle Map (JDK1.8+)
	 *                   file will be used.   
	 * @param fileUrl URL to file with SQL Translation in format (YAML below)
	 *092pcht0h6c0t: |-
	 *  SELECT 'EXMPLE TRANSLATION'
	 *  FROM DUAL
	 *
	 * @return OraProxySqlTranslator instance
	 * @throws SQLException 
	 */
	public static OraProxySqlTranslator getInstance(final boolean yamlStore, final String fileUrl) throws SQLException {
		if (instance == null) {
			synchronized (OraProxySqlTranslator.class) {
				instance = new OraProxySqlTranslator(yamlStore, fileUrl);
			}
		}
		return instance;
	}

	/**
	 * Returns the translated SQL statement, or source SQL statement if no translation is found 
	 * 
	 * @param source source SQL statement
	 * @return translated SQL string using predefined mapping
	 * @throws SQLException 
	 */
	public String translate(final String source) throws SQLException {
		return translator.translate(source);
	}

}
