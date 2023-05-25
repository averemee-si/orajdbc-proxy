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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * 
 * File conversion utility for Oracle JDBC Proxy
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxyFileUtils {

	private final static Logger LOGGER = Logger.getLogger(OraProxyFileUtils.class.getName());
	 /**
	  * Conversion between yaml and Chronicle Map 
	  * @param argv  CLI arguments:
	  *              -m  - conversion type, allowed values:
	  *                    yaml2cmap - convert from yaml to Chronicle Map
	  *                    cmap2yaml - convert from Chronicle Map to yaml
	  *              -f, - full path to source file    
	  */
	public static void main(String[] argv) {

		int modeIndex = 0;
		int fileIndex = 0;
		if (argv.length != 4 && argv.length != 2) {
			usage();
			System.exit(1);
		} else {
			 if ("-m".equals(argv[0])) {
				 modeIndex = 1;
			 } else if (argv.length == 4 && "-m".equals(argv[2])) {
				 modeIndex = 3;
			 } else {
				 modeIndex = 5;
			 }
			 if ("-f".equals(argv[0])) {
				 fileIndex = 1;
			 } else if (argv.length == 4 && "-f".equals(argv[2])) {
				 fileIndex = 3;
			 }
			 if (modeIndex == 0 || fileIndex == 0) {
				usage();
				System.exit(1);
			 }
		 }

		final boolean fromYaml;
		if (modeIndex == 5) {
			fromYaml = true;
		} else if ("yaml2cmap".equalsIgnoreCase(argv[modeIndex])) {
			fromYaml = true;
		} else if ("cmap2yaml".equalsIgnoreCase(argv[modeIndex])) {
			fromYaml = false;
		} else {
			fromYaml = false;
			usage();
			System.exit(1);
		}

		final String sourceFileName = argv[fileIndex];
		final File sourceFile = new File(sourceFileName);
		if (!sourceFile.exists()) {
			LOGGER.log(Level.SEVERE, "File '" + sourceFileName + "' does not exists!");
			System.exit(1);
		}

		final String targetFileName =
				sourceFileName.substring(0, sourceFileName.lastIndexOf(".") + 1)
				+ (fromYaml ? "cmap" : "yaml");
		final File targetFile = new File(targetFileName);
		if (targetFile.exists()) {
			LOGGER.log(Level.SEVERE, "Output file '" + targetFileName + "' already exists!");
			System.exit(1);
		}

		try {
			if (fromYaml) {
				convertYaml(sourceFile, targetFile);
			} else {
				convertCmap(sourceFile, targetFile);
			}
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, "Error while running conversion utility!", ioe);
			System.exit(1);
		}

		System.out.println("Done.");
	}

	private static final void usage() {
		System.err.println("Usage:");
		System.err.println(
				OraProxyFileUtils.class.getName() + " [-m yaml2cmap|cmap2yaml] -f <SOURCE-FILE>");
		System.err.println("\twhen the option -ь is not specified, the default is yaml2cmap");
	}

	 private static void convertYaml(final File sourceFile, final File targetFile) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		final Map<String, String> data = new HashMap<String, String>();
		OraProxyUtils.readSimpleYaml(reader, data);
		reader.close();

		int entries = 0;
		double avgKeySize = 0;
		double avgValueSize = 0;
		for (final String k : data.keySet()) {
			entries++;
			avgKeySize += k.getBytes("UTF-8").length;
			avgValueSize += data.get(k).getBytes("UTF-8").length;
		}

		avgKeySize = Math.floor(avgKeySize/entries);
		avgValueSize = Math.floor(avgValueSize/entries);

		ChronicleMap<String, String> sqlIdMapping = ChronicleMapBuilder
				.of(String.class, String.class)
				.name("https://a2.solutions/ - SQL substition")
				.averageKeySize(avgKeySize)
				.averageValueSize(avgValueSize)
				.entries(entries)
				.createOrRecoverPersistedTo(targetFile);
		sqlIdMapping.putAll(data);
		sqlIdMapping.close();
	}

	private static void convertCmap(final File sourceFile, final File targetFile) throws IOException {
		ChronicleMap<String, String> sqlIdMapping = ChronicleMapBuilder
				.of(String.class, String.class)
				.createOrRecoverPersistedTo(sourceFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
		for (final String sqlId : sqlIdMapping.keySet()) {
			final String sqlStatement = sqlIdMapping.get(sqlId);
			OraProxyUtils.write2Yaml(writer, sqlId, sqlStatement);
		}
		writer.flush();
		writer.close();
		sqlIdMapping.close();
	 }

}
