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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Miscellaneous utils for Oracle JDBC Proxy
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxyUtils {

	private final static Logger LOGGER = Logger.getLogger(OraProxyUtils.class.getName());
	private static final String PROPS_PATH = "/orajdbc-proxy-version.properties";
	private static final int SQL_ID_END = 12;
	private static final String ORA_SQL_ID_ALPHABET = "0123456789abcdfghjkmnpqrstuvwxyz";
	private static final byte NULL_CHAR_BYTE = 0x00;

	private static String version = "undefined";
	private static int versionMajor = 0;
	private static int versionMinor = 0;

	static {
		try (InputStream is = OraProxyUtils.class.getResourceAsStream(PROPS_PATH)) {
			Properties props = new Properties();
			props.load(is);
			version = props.getProperty("version", version).trim();
			final int firstDotPos = version.indexOf('.');
			final int secondDotPos = firstDotPos + version.substring(firstDotPos + 1).indexOf('.') + 1;
			versionMajor = Integer.parseInt(version.substring(0, firstDotPos));
			versionMinor = Integer.parseInt(version.substring(firstDotPos + 1, secondDotPos));
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Returns full version string of orajdbc-proxy
	 * 
	 * @return version String
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * Returns version major, i.e. the minimum JDK version that this driver
	 * is compatible with. For JDK 1.6, 1.7, 1.8 returns 6, 7, 8 respectively
	 * 
	 * @return version major 
	 */
	public static int getVersionMajor() {
		return versionMajor;
	}

	/**
	 * Returns version minor, i.e. the Oracle Database major version:
	 * 11, 12, 18, 19, 21, 23
	 * 
	 * @return version minor 
	 */
	public static int getVersionMinor() {
		return versionMinor;
	}

	/**
	 * Writes SQL mapping information to file
	 * 
	 * @param writer  destination
	 * @param sqlId   13 character Oracle SQL_ID of SQL statement
	 * @param sqlStatement SQL statement text
	 * @throws IOException
	 */
	public static void write2Yaml(final Writer writer, final String sqlId, final String sqlStatement) throws IOException {
		final String[] sqlLines = sqlStatement.split("\n");
		writer
			.append(sqlId)
			.append(": ");
		if (sqlLines.length > 1) {
			writer.append("|-\n");
			boolean firstLine = true;
			for (int i = 0; i < sqlLines.length; i++) {
				if (firstLine) {
					firstLine = false;
				} else {
					writer.append("\n");
				}
				writer
					.append("  ")
					.append(sqlLines[i]);
			}
		} else {
			writer.append(sqlStatement);
		}
		writer.append('\n');
	}

	/**
	 * 
	 * @param reader        java.io.BufferedReader pointing to source YAML
	 * @param mappingData   java.util.Map to store mapping data 
	 * @throws IOException
	 */
	public static void readSimpleYaml(final BufferedReader reader, final Map<String, String> mappingData) throws IOException {
		String line = reader.readLine();
		while (line != null) {
			boolean readNextLine = true;
			if (line.trim().length() > SQL_ID_END + 1) {
				final int semiColonPos = line.indexOf(':');
				if (semiColonPos >= SQL_ID_END + 1 && semiColonPos < (line.length() - 1)) {
					final String sqlId = line.substring(0, SQL_ID_END + 1);
					String sqlStatement = "";
					boolean multiLine = false;
					int pos = semiColonPos + 1;
					for (; pos < line.length(); pos++ ) {
						if (line.charAt(pos) == ' ' || line.charAt(pos) == '\t') {
							continue;
						} else if (line.charAt(pos) == '|' && (pos + 1) <= line.length() && line.charAt(pos + 1) == '-') {
							multiLine = true;
							break;
						} else {
							break;
						}
					}
					if (multiLine) {
						final StringBuilder sb = new StringBuilder(line.length() * 16);
						while (readNextLine) {
							line = reader.readLine();
							if (line == null || !line.startsWith("  ")) {
								readNextLine = false;
							} else {
								sb
									.append(line.substring(2))
									.append('\n');
							}
						}
						sqlStatement = sb.toString();
					} else {
						sqlStatement = line.substring(pos); 
					}
					mappingData.put(sqlId, sqlStatement);
				} else {
					throw new IOException("Invalid format for YAML file!");
				}
			}
			if (readNextLine) {
				line = reader.readLine();
			}
		}
	 }

	/**
	 * Returns Oracle sql_id of a SQL statement, the same as PL/SQL call in RDBMS 12c+
	 * SELECT DBMS_SQL_TRANSLATOR.SQL_ID('select ''Test'' from dual') FROM DUAL;
	 * Based on <a href="https://carlos-sierra.net/2013/09/12/function-to-compute-sql_id-out-of-sql_text/">Function to compute SQL_ID out of SQL_TEXT</a> and <a href="https://www.perumal.org/computing-oracle-sql_id-and-hash_value/">Computing Oracle SQL_ID and HASH_VALUE</a>
	 * 
	 * @param sqlText SQL statement text
	 * @return Oracle sql_id of a SQL statement 
	 * @throws SQLException 
	 */
	public static String sql_id(final String sqlText) throws SQLException {
		try {
			// Get 32 Hex Characters
			final String strHex32Hash = getStatementMd5Hash(sqlText);
			// Obtain MD5 hash, split and reverse Q3 and Q4
			// Reverse order of each of the 4 pairs of hex characters in Q3 and Q4
			final String strHexQ3Reverse = strHex32Hash.substring(22, 24) + strHex32Hash.substring(20, 22)
					+ strHex32Hash.substring(18, 20) + strHex32Hash.substring(16, 18);
			final String strHexQ4Reverse = strHex32Hash.substring(30, 32) + strHex32Hash.substring(28, 30)
					+ strHex32Hash.substring(26, 28) + strHex32Hash.substring(24, 26);
			// assemble lower 16 with reversed q3 and q4
			final String strHexLower16Assembly = strHexQ3Reverse + strHexQ4Reverse;
			// convert to 64 bits of binary data
			String strBinary16Assembly = (new BigInteger(strHexLower16Assembly, 16).toString(2));

			// Left pad with zeros, if the length is less than 64
			final StringBuilder sbBinary16mAssembly = new StringBuilder();
			for (int toLefPad = 64 - strBinary16Assembly.length(); toLefPad > 0; toLefPad--) {
				sbBinary16mAssembly.append('0');
			}
			sbBinary16mAssembly.append(strBinary16Assembly);
			strBinary16Assembly = sbBinary16mAssembly.toString();

			// Split the 64 bit string into 13 pieces
			// First split as 4 bit and remaining as 5 bits
			final StringBuilder sbSQLId = new StringBuilder();
			for (int i = 0; i < 13; i++) {
				final String strPartBindaySplit = (i == 0 ? strBinary16Assembly.substring(0, 4)
						: (i < 12 ? strBinary16Assembly.substring((i * 5) - 1, (i * 5) + 4)
								: strBinary16Assembly.substring((i * 5) - 1)));
				// Index position on Alphabet
				final int intIndexOnAlphabet = Integer.parseInt(strPartBindaySplit, 2);
				// Stick 13 characters
				sbSQLId.append(ORA_SQL_ID_ALPHABET.charAt(intIndexOnAlphabet));
			}
			return sbSQLId.toString();
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(String.format(
					"Unable to convert SQL text\n\t'%s'\n\t\tto UTF-8 encoding.", sqlText));
		} catch (NoSuchAlgorithmException e) {
			throw new SQLException(String.format(
					"Unable to init crypto while calculating SQL_ID for '%s'.", sqlText));
		}
	}

	/**
	 * Returns Oracle hash_value of a SQL statement, the same as PL/SQL call in RDBMS 12c+
	 * SELECT DBMS_SQL_TRANSLATOR.SQL_HASH('select ''Test'' from dual') FROM DUAL;
	 * Based on <a href="https://www.perumal.org/computing-oracle-sql_id-and-hash_value/">Computing Oracle SQL_ID and HASH_VALUE</a>
	 * 
	 * @param sqlText SQL statement text
	 * @return Oracle hash_value of a SQL statement 
	 * @throws SQLException
	 */
	public static BigInteger hash_value(final String sqlText) throws SQLException {
		/* Example
         *  Statement: SELECT 'Ram' ram_stmt FROM dual
         *  MD5 Hash with null terminator: 93bc072570a58c1f330166ab2d0a88d2
         *  Oracle SQL_ID: aqth16g98h2jd
         *  Address: 00000000826C6060 
         *  Hash Value: 3532130861 
         *  Hash Value Hex: D2880A2D
         *  MD5 Hash without null terminator: 93bc072570a58c1f330166ab2d0a88d2
         *  Exact Matching Signature: 4178266890746386855
         *  Exact Matching Signature Hex: 39FC2F9987B6D9A7  
         *  MD5 Hash without null terminator for bound statement: 
         *  Force Matching Signature: 16194980974160721469
         *  Force Matching Signature Hex: E0C021642D0F363D  
         */
		try {
			// Get 32 Hex Characters
			// Obtain MD5 hash, split and reverse Q4
			final String strHex32Hash = getStatementMd5Hash(sqlText);
			// Reverse order of hex characters in Q4
			String strHexQ4Reverse = strHex32Hash.substring(30, 32) + strHex32Hash.substring(28, 30)
				+ strHex32Hash.substring(26, 28) + strHex32Hash.substring(24, 26);
			// convert to 4 bytes of hex to integer
			return new BigInteger(strHexQ4Reverse, 16);
		} catch (UnsupportedEncodingException e) {
			throw new SQLException(String.format(
					"Unable to convert SQL text\n\t'%s'\n\t\tto UTF-8 encoding.", sqlText));
		} catch (NoSuchAlgorithmException e) {
			throw new SQLException(String.format(
					"Unable to init crypto while calculating HASH_VALUE for '%s'.", sqlText));
		}
	}

	private static String getStatementMd5Hash(final String sqlText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		//final byte[] bytesOfStatement = sqlText.trim().getBytes("UTF-8");
		final byte[] bytesOfStatement = sqlText.getBytes("UTF-8");
		// last bucket used for Null Terminator
		final byte[] bytesOfStatementWithNull = new byte[bytesOfStatement.length + 1];

		// Null terminator is lost or has no effect (Unicode \\u0000, \\U0000 or hex 0x00 or "\0") 
		// on MD5 digest when you append to String
		// Hence add the byte value as a last element after cloning
		System.arraycopy(bytesOfStatement, 0, bytesOfStatementWithNull, 0, bytesOfStatement.length);
		// add null terminator as last element
		bytesOfStatementWithNull[bytesOfStatement.length]=NULL_CHAR_BYTE;

		// get the MD5 digest
		final MessageDigest md = MessageDigest.getInstance("MD5");
		final byte[] bytesFromMD5Digest =  md.digest(bytesOfStatementWithNull);
		
		// convert the byte to hex format and generate MD5 hash
		final StringBuilder sbMD5HashHex32 = new StringBuilder(32);
		for (int i = 0; i < bytesFromMD5Digest.length; i++) {
			sbMD5HashHex32.append(Integer.toString((bytesFromMD5Digest[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sbMD5HashHex32.toString();
	}


}
