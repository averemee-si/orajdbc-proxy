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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import oracle.jdbc.LogicalTransactionId;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.pool.OracleConnectionCacheCallback;
import oracle.sql.ARRAY;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;

/**
 * 
 * Oracle JDBC Proxy Connection implementation
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxyConnection implements OracleConnection {

	final OracleConnection oracle;
	final OraProxySqlTranslator translator;

	OraProxyConnection(OracleConnection connection, OraProxySqlTranslator translator) {
		this.oracle = connection;
		this.translator = translator;
	}

	@Override
	public Statement createStatement() throws SQLException {
		return new OraProxyStatement(
				this,
				(OracleStatement) oracle.createStatement(),
				translator); 
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return new OraProxyStatement(
				this,
				(OracleStatement) oracle.createStatement(resultSetType, resultSetConcurrency),
				translator);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new OraProxyStatement(
				this,
				(OracleStatement) oracle.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability),
				translator);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return new OraProxyCallableStatement(
				this,
				(OracleCallableStatement) oracle.prepareCall(translator.translate(sql)),
				translator);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return new OraProxyCallableStatement(
				this,
				(OracleCallableStatement) oracle.prepareCall(translator.translate(sql), resultSetType, resultSetConcurrency),
				translator);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return new OraProxyCallableStatement(
				this,
				(OracleCallableStatement) oracle.prepareCall(translator.translate(sql), resultSetType, resultSetConcurrency, resultSetHoldability),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql)),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql), resultSetType, resultSetConcurrency),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql), resultSetType, resultSetConcurrency, resultSetHoldability),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql), autoGeneratedKeys),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql), columnIndexes),
				translator);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatement(translator.translate(sql), columnNames),
				translator);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return oracle.nativeSQL(translator.translate(sql));
	}

	@Override
	public PreparedStatement getStatementWithKey(String key) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.getStatementWithKey(key),
				translator);
	}

	@Deprecated
	@Override
	public CallableStatement prepareCallWithKey(String key) throws SQLException {
		return new OraProxyCallableStatement(
				this,
				(OracleCallableStatement) oracle.prepareCallWithKey(key),
				translator);
	}

	@Deprecated
	@Override
	public PreparedStatement prepareStatementWithKey(String key) throws SQLException {
		return new OraProxyPreparedStatement(
				this,
				(OraclePreparedStatement) oracle.prepareStatementWithKey(key),
				translator);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return oracle.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return oracle.isWrapperFor(iface);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		oracle.setAutoCommit(autoCommit);
		
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return oracle.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		oracle.commit();
	}

	@Override
	public void rollback() throws SQLException {
		oracle.rollback();
	}

	@Override
	public void close() throws SQLException {
		oracle.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return oracle.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return oracle.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		oracle.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return oracle.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		oracle.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return oracle.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		oracle.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return oracle.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return oracle.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		oracle.clearWarnings();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return oracle.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		oracle.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		oracle.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return oracle.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return oracle.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return oracle.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		oracle.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		oracle.releaseSavepoint(savepoint);
	}

	@Override
	public Clob createClob() throws SQLException {
		return oracle.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return oracle.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return oracle.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return oracle.createSQLXML();
	}

	@Override
	public boolean isValid(ConnectionValidation effort, int timeout) throws SQLException {
		return oracle.isValid(effort, timeout);
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return oracle.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		oracle.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		oracle.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return oracle.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return oracle.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return oracle.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return oracle.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		oracle.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return oracle.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		oracle.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		oracle.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return oracle.getNetworkTimeout();
	}

	@Override
	public Connection _getPC() {
		return oracle._getPC();
	}

	@Override
	public void abort() throws SQLException {
		oracle.abort();
	}

	@Deprecated
	@Override
	public void applyConnectionAttributes(Properties connAttr) throws SQLException {
		oracle.applyConnectionAttributes(connAttr);
	}

	@Deprecated
	@Override
	public void archive(int mode, int aseq, String acstext) throws SQLException {
		oracle.archive(mode, aseq, acstext);
	}

	@Override
	public void cancel() throws SQLException {
		oracle.cancel();
	}

	@Deprecated
	@Override
	public void clearAllApplicationContext(String nameSpace) throws SQLException {
		oracle.clearAllApplicationContext(nameSpace);
	}

	@Deprecated
	@Override
	public void close(Properties connAttr) throws SQLException {
		oracle.close(connAttr);
	}

	@Override
	public void close(int opt) throws SQLException {
		oracle.close(opt);
	}

	@Override
	public void commit(EnumSet<CommitOption> options) throws SQLException {
		oracle.commit(options);
	}

	@Override
	public ARRAY createARRAY(String typeName, Object elements) throws SQLException {
		return oracle.createARRAY(typeName, elements);
	}

	@Override
	public BINARY_DOUBLE createBINARY_DOUBLE(double value) throws SQLException {
		return oracle.createBINARY_DOUBLE(value);
	}

	@Override
	public BINARY_FLOAT createBINARY_FLOAT(float value) throws SQLException {
		return oracle.createBINARY_FLOAT(value);
	}

	@Override
	public DATE createDATE(Date value) throws SQLException {
		return oracle.createDATE(value);
	}

	@Override
	public DATE createDATE(Time value) throws SQLException {
		return oracle.createDATE(value);
	}

	@Override
	public DATE createDATE(Timestamp value) throws SQLException {
		return oracle.createDATE(value);
	}

	@Override
	public DATE createDATE(String value) throws SQLException {
		return oracle.createDATE(value);
	}

	@Override
	public DATE createDATE(Date value, Calendar cal) throws SQLException {
		return oracle.createDATE(value, cal);
	}

	@Override
	public DATE createDATE(Time value, Calendar cal) throws SQLException {
		return oracle.createDATE(value, cal);
	}

	@Override
	public DATE createDATE(Timestamp value, Calendar cal) throws SQLException {
		return oracle.createDATE(value, cal);
	}

	@Override
	public INTERVALDS createINTERVALDS(String value) throws SQLException {
		return oracle.createINTERVALDS(value);
	}

	@Override
	public INTERVALYM createINTERVALYM(String value) throws SQLException {
		return oracle.createINTERVALYM(value);
	}

	@Override
	public NUMBER createNUMBER(boolean value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(byte value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(short value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(int value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(long value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(float value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(double value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(BigDecimal value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(BigInteger value) throws SQLException {
		return oracle.createNUMBER(value);
	}

	@Override
	public NUMBER createNUMBER(String value, int scale) throws SQLException {
		return oracle.createNUMBER(value, scale);
	}

	@Override
	public Array createOracleArray(String arrayTypeName, Object elements) throws SQLException {
		return oracle.createOracleArray(arrayTypeName, elements);
	}

	@Override
	public TIMESTAMP createTIMESTAMP(Date value) throws SQLException {
		return oracle.createTIMESTAMP(value);
	}

	@Override
	public TIMESTAMP createTIMESTAMP(DATE value) throws SQLException {
		return oracle.createTIMESTAMP(value);
	}

	@Override
	public TIMESTAMP createTIMESTAMP(Time value) throws SQLException {
		return oracle.createTIMESTAMP(value);
	}

	@Override
	public TIMESTAMP createTIMESTAMP(Timestamp value) throws SQLException {
		return oracle.createTIMESTAMP(value);
	}

	@Override
	public TIMESTAMP createTIMESTAMP(String value) throws SQLException {
		return oracle.createTIMESTAMP(value);
	}

	@Override
	public TIMESTAMPLTZ createTIMESTAMPLTZ(Date value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPLTZ(value, cal);
	}

	@Override
	public TIMESTAMPLTZ createTIMESTAMPLTZ(Time value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPLTZ(value, cal);
	}

	@Override
	public TIMESTAMPLTZ createTIMESTAMPLTZ(Timestamp value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPLTZ(value, cal);
	}

	@Override
	public TIMESTAMPLTZ createTIMESTAMPLTZ(String value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPLTZ(value, cal);
	}

	@Override
	public TIMESTAMPLTZ createTIMESTAMPLTZ(DATE value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPLTZ(value, cal);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Date value) throws SQLException {
		return oracle.createTIMESTAMPTZ(value);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Time value) throws SQLException {
		return oracle.createTIMESTAMPTZ(value);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp value) throws SQLException {
		return oracle.createTIMESTAMPTZ(value);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(String value) throws SQLException {
		return oracle.createTIMESTAMPTZ(value);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(DATE value) throws SQLException {
		return oracle.createTIMESTAMPTZ(value);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Date value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPTZ(value, cal);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Time value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPTZ(value, cal);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPTZ(value, cal);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(String value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMPTZ(value, cal);
	}

	@Override
	public AQMessage dequeue(String queueName, AQDequeueOptions opt, byte[] tdo) throws SQLException {
		return oracle.dequeue(queueName, opt, tdo);
	}

	@Override
	public AQMessage dequeue(String queueName, AQDequeueOptions opt, byte[] tdo, int version) throws SQLException {
		return oracle.dequeue(queueName, opt, tdo, version);
	}

	@Override
	public AQMessage[] dequeue(String queueName, AQDequeueOptions opt, byte[] tdo, int version, int deqSize)
			throws SQLException {
		return oracle.dequeue(queueName, opt, tdo, version, deqSize);
	}

	@Override
	public AQMessage dequeue(String queueName, AQDequeueOptions opt, String typeName) throws SQLException {
		return oracle.dequeue(queueName, opt, typeName);
	}

	@Override
	public AQMessage[] dequeue(String queueName, AQDequeueOptions opt, String typeName, int deqSize) throws SQLException {
		return oracle.dequeue(queueName, opt, typeName, deqSize);
	}

	@Override
	public void enqueue(String queueName, AQEnqueueOptions opt, AQMessage mesg) throws SQLException {
		oracle.enqueue(queueName, opt, mesg);
	}

	@Override
	public int enqueue(String queueName, AQEnqueueOptions opt, AQMessage[] mesgs) throws SQLException {
		return oracle.enqueue(queueName, opt, mesgs);
	}

	@Override
	public TypeDescriptor[] getAllTypeDescriptorsInCurrentSchema() throws SQLException {
		return oracle.getAllTypeDescriptorsInCurrentSchema();
	}

	@Override
	public String getAuthenticationAdaptorName() throws SQLException {
		return oracle.getAuthenticationAdaptorName();
	}

	@Override
	public boolean getAutoClose() throws SQLException {
		return oracle.getAutoClose();
	}

	@Override
	public CallableStatement getCallWithKey(String key) throws SQLException {
		return oracle.getCallWithKey(key);
	}

	@Deprecated
	@Override
	public Properties getConnectionAttributes() throws SQLException {
		return oracle.getConnectionAttributes();
	}

	@Deprecated
	@Override
	public int getConnectionReleasePriority() throws SQLException {
		return oracle.getConnectionReleasePriority();
	}

	@Override
	public boolean getCreateStatementAsRefCursor() {
		return oracle.getCreateStatementAsRefCursor();
	}

	@Override
	public String getCurrentSchema() throws SQLException {
		return oracle.getCurrentSchema();
	}

	@Override
	public String getDataIntegrityAlgorithmName() throws SQLException {
		return oracle.getDataIntegrityAlgorithmName();
	}

	@Override
	public DatabaseChangeRegistration getDatabaseChangeRegistration(int regid) throws SQLException {
		return oracle.getDatabaseChangeRegistration(regid);
	}

	@Deprecated
	@Override
	public int getDefaultExecuteBatch() {
		return oracle.getDefaultExecuteBatch();
	}

	@Override
	public int getDefaultRowPrefetch() {
		return oracle.getDefaultRowPrefetch();
	}

	@Override
	public TimeZone getDefaultTimeZone() throws SQLException {
		return oracle.getDefaultTimeZone();
	}

	@Override
	public Object getDescriptor(String sqlName) {
		return oracle.getDescriptor(sqlName);
	}

	@Override
	public String getEncryptionAlgorithmName() throws SQLException {
		return oracle.getEncryptionAlgorithmName();
	}

	@Deprecated
	@Override
	public short getEndToEndECIDSequenceNumber() throws SQLException {
		return oracle.getEndToEndECIDSequenceNumber();
	}

	@Deprecated
	@Override
	public String[] getEndToEndMetrics() throws SQLException {
		return oracle.getEndToEndMetrics();
	}

	@Override
	public boolean getExplicitCachingEnabled() throws SQLException {
		return oracle.getExplicitCachingEnabled();
	}

	@Override
	public boolean getImplicitCachingEnabled() throws SQLException {
		return oracle.getImplicitCachingEnabled();
	}

	@Override
	public boolean getIncludeSynonyms() {
		return oracle.getIncludeSynonyms();
	}

	@Deprecated
	@Override
	public Object getJavaObject(String sqlName) throws SQLException {
		return oracle.getJavaObject(sqlName);
	}

	@Override
	public Properties getProperties() {
		return oracle.getProperties();
	}

	@Override
	public boolean getRemarksReporting() {
		return oracle.getRemarksReporting();
	}

	@Override
	public boolean getRestrictGetTables() {
		return oracle.getRestrictGetTables();
	}

	@Deprecated
	@Override
	public String getSQLType(Object obj) throws SQLException {
		return oracle.getSQLType(obj);
	}

	@Override
	public String getSessionTimeZone() {
		return oracle.getSessionTimeZone();
	}

	@Override
	public String getSessionTimeZoneOffset() throws SQLException {
		return oracle.getSessionTimeZoneOffset();
	}

	@Override
	public int getStatementCacheSize() throws SQLException {
		return oracle.getStatementCacheSize();
	}

	@Deprecated
	@Override
	public int getStmtCacheSize() {
		return oracle.getStmtCacheSize();
	}

	@Override
	public short getStructAttrCsId() throws SQLException {
		return oracle.getStructAttrCsId();
	}

	@Override
	public TypeDescriptor[] getTypeDescriptorsFromList(String[][] schemaAndTypeNamePairs) throws SQLException {
		return oracle.getTypeDescriptorsFromList(schemaAndTypeNamePairs);
	}

	@Override
	public TypeDescriptor[] getTypeDescriptorsFromListInCurrentSchema(String[] typeNames) throws SQLException {
		return oracle.getTypeDescriptorsFromListInCurrentSchema(typeNames);
	}

	@Deprecated
	@Override
	public Properties getUnMatchedConnectionAttributes() throws SQLException {
		return oracle.getUnMatchedConnectionAttributes();
	}

	@Override
	public String getUserName() throws SQLException {
		return oracle.getUserName();
	}

	@Deprecated
	@Override
	public boolean getUsingXAFlag() {
		return oracle.getUsingXAFlag();
	}

	@Deprecated
	@Override
	public boolean getXAErrorFlag() {
		return oracle.getXAErrorFlag();
	}

	@Override
	public boolean isLogicalConnection() {
		return oracle.isLogicalConnection();
	}

	@Override
	public boolean isProxySession() {
		return oracle.isProxySession();
	}

	@Override
	public boolean isUsable() {
		return oracle.isUsable();
	}

	@Override
	public void openProxySession(int type, Properties prop) throws SQLException {
		oracle.openProxySession(type, prop);
	}

	@Override
	public void oracleReleaseSavepoint(OracleSavepoint savepoint) throws SQLException {
		oracle.oracleReleaseSavepoint(savepoint);
	}

	@Override
	public void oracleRollback(OracleSavepoint savepoint) throws SQLException {
		oracle.oracleRollback(savepoint);
	}

	@Override
	public OracleSavepoint oracleSetSavepoint() throws SQLException {
		return oracle.oracleSetSavepoint();
	}

	@Override
	public OracleSavepoint oracleSetSavepoint(String name) throws SQLException {
		return oracle.oracleSetSavepoint(name);
	}

	@Deprecated
	@Override
	public oracle.jdbc.internal.OracleConnection physicalConnectionWithin() {
		return oracle.physicalConnectionWithin();
	}

	@Override
	public int pingDatabase() throws SQLException {
		return oracle.pingDatabase();
	}

	@Deprecated
	@Override
	public int pingDatabase(int timeOut) throws SQLException {
		return oracle.pingDatabase(timeOut);
	}

	@Override
	public void purgeExplicitCache() throws SQLException {
		oracle.purgeExplicitCache();
	}

	@Override
	public void purgeImplicitCache() throws SQLException {
		oracle.purgeImplicitCache();
	}

	@Override
	public void putDescriptor(String sqlName, Object desc) throws SQLException {
		oracle.putDescriptor(sqlName, desc);
	}

	@Override
	public AQNotificationRegistration[] registerAQNotification(String[] name, Properties[] options, Properties globalOptions)
			throws SQLException {
		return oracle.registerAQNotification(name, options, globalOptions);
	}

	@Deprecated
	@Override
	public void registerConnectionCacheCallback(OracleConnectionCacheCallback occc, Object userObj, int cbkFlag)
			throws SQLException {
		oracle.registerConnectionCacheCallback(occc, userObj, cbkFlag);
	}

	@Override
	public DatabaseChangeRegistration registerDatabaseChangeNotification(Properties options) throws SQLException {
		return oracle.registerDatabaseChangeNotification(options);
	}

	@Deprecated
	@Override
	public void registerSQLType(String sqlName, Class javaClass) throws SQLException {
		oracle.registerSQLType(sqlName, javaClass);
	}

	@Deprecated
	@Override
	public void registerSQLType(String sqlName, String javaClassName) throws SQLException {
		oracle.registerSQLType(sqlName, javaClassName);
	}

	@Override
	public void registerTAFCallback(OracleOCIFailover cbk, Object obj) throws SQLException {
		oracle.registerTAFCallback(cbk, obj);
	}

	@Deprecated
	@Override
	public void setApplicationContext(String nameSpace, String attribute, String value) throws SQLException {
		oracle.setApplicationContext(nameSpace, attribute, value);
	}

	@Override
	public void setAutoClose(boolean autoClose) throws SQLException {
		oracle.setAutoClose(autoClose);
	}

	@Deprecated
	@Override
	public void setConnectionReleasePriority(int priority) throws SQLException {
		oracle.setConnectionReleasePriority(priority);
	}

	@Override
	public void setCreateStatementAsRefCursor(boolean value) {
		oracle.setCreateStatementAsRefCursor(value);
	}

	@Deprecated
	@Override
	public void setDefaultExecuteBatch(int batch) throws SQLException {
		oracle.setDefaultExecuteBatch(batch);
	}

	@Override
	public void setDefaultRowPrefetch(int value) throws SQLException {
		oracle.setDefaultRowPrefetch(value);
	}

	@Override
	public void setDefaultTimeZone(TimeZone tz) throws SQLException {
		oracle.setDefaultTimeZone(tz);
	}

	@Deprecated
	@Override
	public void setEndToEndMetrics(String[] metrics, short sequenceNumber) throws SQLException {
		oracle.setEndToEndMetrics(metrics, sequenceNumber);
	}

	@Override
	public void setExplicitCachingEnabled(boolean cache) throws SQLException {
		oracle.setExplicitCachingEnabled(cache);
	}

	@Override
	public void setImplicitCachingEnabled(boolean cache) throws SQLException {
		oracle.setImplicitCachingEnabled(cache);
	}

	@Override
	public void setIncludeSynonyms(boolean synonyms) {
		oracle.setIncludeSynonyms(synonyms);
	}

	@Override
	public void setPlsqlWarnings(String setting) throws SQLException {
		oracle.setPlsqlWarnings(setting);
	}

	@Override
	public void setRemarksReporting(boolean reportRemarks) {
		oracle.setRemarksReporting(reportRemarks);
	}

	@Override
	public void setRestrictGetTables(boolean restrict) {
		oracle.setRestrictGetTables(restrict);
	}

	@Override
	public void setSessionTimeZone(String regionName) throws SQLException {
		oracle.setSessionTimeZone(regionName);
	}

	@Override
	public void setStatementCacheSize(int size) throws SQLException {
		oracle.setStatementCacheSize(size);
	}

	@Deprecated
	@Override
	public void setStmtCacheSize(int size) throws SQLException {
		oracle.setStmtCacheSize(size);
	}

	@Deprecated
	@Override
	public void setStmtCacheSize(int size, boolean clearMetaData) throws SQLException {
		oracle.setStmtCacheSize(size, clearMetaData);
	}

	@Deprecated
	@Override
	public void setUsingXAFlag(boolean value) {
		oracle.setUsingXAFlag(value);
	}

	@Override
	public void setWrapper(OracleConnection wrapper) {
		oracle.setWrapper(wrapper);
	}

	@Deprecated
	@Override
	public void setXAErrorFlag(boolean value) {
		oracle.setXAErrorFlag(value);
	}

	@Override
	public void shutdown(DatabaseShutdownMode mode) throws SQLException {
		oracle.shutdown(mode);
	}

	@Override
	public void startup(DatabaseStartupMode mode) throws SQLException {
		oracle.startup(mode);
	}

	@Deprecated
	@Override
	public void startup(String startupString, int mode) throws SQLException {
		oracle.startup(startupString, mode);
	}

	@Override
	public void unregisterAQNotification(AQNotificationRegistration registration) throws SQLException {
		oracle.unregisterAQNotification(registration);
	}

	@Override
	public void unregisterDatabaseChangeNotification(DatabaseChangeRegistration registration) throws SQLException {
		oracle.unregisterDatabaseChangeNotification(registration);
	}

	@Deprecated
	@Override
	public void unregisterDatabaseChangeNotification(int registrationId) throws SQLException {
		oracle.unregisterDatabaseChangeNotification(registrationId);
	}

	@Override
	public void unregisterDatabaseChangeNotification(long registrationId, String callback) throws SQLException {
		oracle.unregisterDatabaseChangeNotification(registrationId, callback);
	}

	@Deprecated
	@Override
	public void unregisterDatabaseChangeNotification(int registrationId, String host, int tcpPort) throws SQLException {
		oracle.unregisterDatabaseChangeNotification(registrationId, host, tcpPort);
	}

	@Override
	public OracleConnection unwrap() {
		return oracle.unwrap();
	}

	@Override
	public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener listener) throws SQLException {
		oracle.addLogicalTransactionIdEventListener(listener);
	}

	@Override
	public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener listener, Executor executor)
			throws SQLException {
		oracle.addLogicalTransactionIdEventListener(listener, executor);
	}

	@Override
	public boolean attachServerConnection() throws SQLException {
		return oracle.attachServerConnection();
	}

	@Override
	public void beginRequest() throws SQLException {
		oracle.beginRequest();
	}

	@Override
	public TIMESTAMP createTIMESTAMP(Timestamp value, Calendar cal) throws SQLException {
		return oracle.createTIMESTAMP(value, cal);
	}

	@Override
	public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp value, ZoneId tzid) throws SQLException {
		return oracle.createTIMESTAMPTZ(value, tzid);
	}

	@Override
	public void detachServerConnection(String tag) throws SQLException {
		oracle.detachServerConnection(tag);
	}

	@Override
	public void endRequest() throws SQLException {
		oracle.endRequest();
	}

	@Override
	public String getDRCPPLSQLCallbackName() throws SQLException {
		return oracle.getDRCPPLSQLCallbackName();
	}

	@Override
	public String getDRCPReturnTag() throws SQLException {
		return oracle.getDRCPReturnTag();
	}

	@Override
	public DRCPState getDRCPState() throws SQLException {
		return oracle.getDRCPState();
	}

	@Override
	public LogicalTransactionId getLogicalTransactionId() throws SQLException {
		return oracle.getLogicalTransactionId();
	}

	@Override
	public boolean isDRCPEnabled() throws SQLException {
		return oracle.isDRCPEnabled();
	}

	@Override
	public boolean isDRCPMultitagEnabled() throws SQLException {
		return oracle.isDRCPMultitagEnabled();
	}

	@Override
	public boolean needToPurgeStatementCache() throws SQLException {
		return oracle.needToPurgeStatementCache();
	}

	@Override
	public void removeLogicalTransactionIdEventListener(LogicalTransactionIdEventListener listener) throws SQLException {
		oracle.removeLogicalTransactionIdEventListener(listener);
	}

	@Override
	public void setShardingKey(OracleShardingKey shardingKey, OracleShardingKey superShardingKey) throws SQLException {
		oracle.setShardingKey(shardingKey, superShardingKey);
	}

	@Override
	public void setShardingKey(OracleShardingKey shardingKey) throws SQLException {
		oracle.setShardingKey(shardingKey);
	}

	@Override
	public boolean setShardingKeyIfValid(OracleShardingKey shardingKey, OracleShardingKey superShardingKey, int timeout) throws SQLException {
		return oracle.setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
	}

	@Override
	public boolean setShardingKeyIfValid(OracleShardingKey shardingKey, int timeout) throws SQLException {
		return oracle.setShardingKeyIfValid(shardingKey, timeout);
	}

	@Override
	public void startup(DatabaseStartupMode mode, String pfileName) throws SQLException {
		oracle.startup(mode, pfileName);
	}

	@Override
	public String getChecksumProviderName() throws SQLException {
		return oracle.getChecksumProviderName();
	}

	@Override
	public String getEncryptionProviderName() throws SQLException {
		return oracle.getEncryptionProviderName();
	}

	@Override
	public void disableLogging() throws SQLException {
		oracle.disableLogging();
	}

	@Override
	public void dumpLog() throws SQLException {
		oracle.dumpLog();
	}

	@Override
	public void enableLogging() throws SQLException {
		oracle.enableLogging();
	}

	@Override
	public SecuredLogger getLogger() throws SQLException {
		return oracle.getLogger();
	}

	@Override
	public String getNetConnectionId() throws SQLException {
		return oracle.getNetConnectionId();
	}

	@Override
	public Properties getServerSessionInfo() throws SQLException {
		return oracle.getServerSessionInfo();
	}


}
