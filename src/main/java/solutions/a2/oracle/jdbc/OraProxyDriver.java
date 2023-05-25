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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import oracle.jdbc.driver.OracleConnection;

/**
 * 
 * Oracle JDBC Proxy Driver implementation
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxyDriver implements Driver  {

	/**
	 * 'a2.mapping.file.url' - URL to SQL mapping file.
	 *   File must be yaml in format:
	 *  SQL-ID:
	 *     SQL-TEXT 
	 *  or <a href="https://chronicle.software/">Chronicle Map</a>
	 */
	public static final String A2_MAPPING_FILE_URL = "a2.mapping.file.url";
	/**
	 * 'a2.mapping.file.type' - file type of SQL mapping file.
	 *   Allowed values 'yaml' or 'chronicle'
	 */
	public static final String A2_MAPPING_FILE_TYPE = "a2.mapping.file.type";
	private static final String URL_PREFIX = "jdbc:proxy:oracle:";
	private static final String ORA_PREFIX = "jdbc:oracle:thin:";


	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		if (acceptsURL(url)) {
			final int paramsPos = url.indexOf('?');
			if (paramsPos > -1) {
				final StringBuilder sb = new StringBuilder(url.length());
				sb.append(url.substring(0, paramsPos).replace(URL_PREFIX, ORA_PREFIX));
				String mappingFile = null;
				boolean useYamlStore = true;
				boolean formatParamPresent = false;
				boolean firstParam = true;
				for (final String param : url.substring(paramsPos + 1).split("&")) {
					if (param.startsWith(A2_MAPPING_FILE_URL)) {
						mappingFile = param.substring(param.indexOf('=') + 1);
					} else if (param.startsWith(A2_MAPPING_FILE_TYPE)) {
						formatParamPresent = true;
						final String mappingFileType = param.substring(param.indexOf('=') + 1);
						if ("yaml".equals(mappingFileType)) {
							useYamlStore = true;
						} else if ("chronicle".equals(mappingFileType)) {
							useYamlStore = false;
						} else {
							throw new SQLException(
									"For the '" + A2_MAPPING_FILE_TYPE + "' only yaml and chronicle are allowed!");
						}
					} else {
						if (firstParam) {
							sb.append('?');
							firstParam = false;
						} else {
							sb.append('&');
						}
						sb.append(param);
					}
				}
				if (mappingFile == null) {
					if (info.containsKey(A2_MAPPING_FILE_URL)) {
						mappingFile = info.getProperty(A2_MAPPING_FILE_URL);
					} else {
						throw new SQLException("The '" + A2_MAPPING_FILE_URL + "' parameter must be set!");
					}
				}
				if (!formatParamPresent && info.containsKey(A2_MAPPING_FILE_TYPE)) {
					final String mappingFileType = info.getProperty(A2_MAPPING_FILE_TYPE);
					if ("yaml".equals(mappingFileType)) {
						useYamlStore = true;
					} else if ("chronicle".equals(mappingFileType)) {
						useYamlStore = false;
					} else {
						throw new SQLException(
								"For the '" + A2_MAPPING_FILE_TYPE + "' only yaml and chronicle are allowed!");
					}
				}
				final OraProxySqlTranslator translator =  OraProxySqlTranslator.getInstance(useYamlStore, mappingFile);
				final Driver oraDriver = DriverManager.getDriver(sb.toString());
				return new OraProxyConnection((OracleConnection) oraDriver.connect(sb.toString(), info), translator);
			} else {
				throw new SQLException("Incorrect format of '" + url + "' for driver!");
			}

		}
		return null;
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith(URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMajorVersion() {
		return OraProxyUtils.getVersionMajor();
	}

	@Override
	public int getMinorVersion() {
		return OraProxyUtils.getVersionMinor();
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// return Oracle JDBC logger
		return Logger.getLogger("oracle.jdbc");
	}

	static {
		try {
			DriverManager.registerDriver(new OraProxyDriver());
		} catch (SQLException sqle) {
			throw new RuntimeException("Unable to register orajdbc-proxy driver!");
		}
	}

}
