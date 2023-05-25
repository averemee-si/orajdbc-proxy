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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleResultSet;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

/**
 * 
 * Oracle JDBC Proxy ResultSet/OracleResultSet implementation
 * @author <a href="mailto:averemee@a2.solutions">Aleksei Veremeev</a>
 * 
 */
public class OraProxyResultSet implements OracleResultSet {

	final Statement statement;
	final OracleResultSet oracle;

	OraProxyResultSet(Statement statement, OracleResultSet resultSet) {
		this.statement = statement;
		this.oracle = resultSet;
	}

	@Override
	public Statement getStatement() throws SQLException {
		return statement;
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
	public boolean next() throws SQLException {
		return oracle.next();
	}

	@Override
	public void close() throws SQLException {
		oracle.close();
	}

	@Override
	public boolean wasNull() throws SQLException {
		return oracle.wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return oracle.getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return oracle.getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return oracle.getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return oracle.getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return oracle.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return oracle.getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return oracle.getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return oracle.getDouble(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return oracle.getBigDecimal(columnIndex);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return oracle.getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return oracle.getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return oracle.getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return oracle.getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return oracle.getAsciiStream(columnIndex);
	}

	@Deprecated
	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return oracle.getUnicodeStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return oracle.getBinaryStream(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return oracle.getString(columnLabel);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return oracle.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return oracle.getByte(columnLabel);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return oracle.getShort(columnLabel);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return oracle.getInt(columnLabel);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return oracle.getLong(columnLabel);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return oracle.getFloat(columnLabel);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return oracle.getDouble(columnLabel);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return oracle.getBigDecimal(columnLabel, scale);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return oracle.getBytes(columnLabel);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return oracle.getDate(columnLabel);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return oracle.getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return oracle.getTimestamp(columnLabel);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return oracle.getAsciiStream(columnLabel);
	}

	@Deprecated
	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return oracle.getUnicodeStream(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return oracle.getBinaryStream(columnLabel);
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
	public String getCursorName() throws SQLException {
		return oracle.getCursorName();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return oracle.getMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return oracle.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return oracle.getObject(columnLabel);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return oracle.findColumn(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return oracle.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return oracle.getCharacterStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return oracle.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return oracle.getBigDecimal(columnLabel);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return oracle.isBeforeFirst();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return oracle.isAfterLast();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return oracle.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return oracle.isLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		oracle.beforeFirst();
	}

	@Override
	public void afterLast() throws SQLException {
		oracle.afterLast();
	}

	@Override
	public boolean first() throws SQLException {
		return oracle.first();
	}

	@Override
	public boolean last() throws SQLException {
		return oracle.last();
	}

	@Override
	public int getRow() throws SQLException {
		return oracle.getRow();
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return oracle.absolute(row);
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return oracle.relative(rows);
	}

	@Override
	public boolean previous() throws SQLException {
		return oracle.previous();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		oracle.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return oracle.getFetchDirection();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		oracle.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return oracle.getFetchSize();
	}

	@Override
	public int getType() throws SQLException {
		return oracle.getType();
	}

	@Override
	public int getConcurrency() throws SQLException {
		return oracle.getConcurrency();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return oracle.rowUpdated();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return oracle.rowInserted();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return oracle.rowDeleted();
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		oracle.updateNull(columnIndex);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		oracle.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		oracle.updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		oracle.updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		oracle.updateInt(columnIndex, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		oracle.updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		oracle.updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		oracle.updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		oracle.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		oracle.updateString(columnIndex, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		oracle.updateBytes(columnIndex, x);
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		oracle.updateDate(columnIndex, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		oracle.updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		oracle.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		oracle.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		oracle.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		oracle.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		oracle.updateObject(columnIndex, x, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		oracle.updateObject(columnIndex, x);
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		oracle.updateNull(columnLabel);
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		oracle.updateBoolean(columnLabel, x);
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		oracle.updateByte(columnLabel, x);
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		oracle.updateShort(columnLabel, x);
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		oracle.updateInt(columnLabel, x);
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		oracle.updateLong(columnLabel, x);
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		oracle.updateFloat(columnLabel, x);
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		oracle.updateDouble(columnLabel, x);
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		oracle.updateBigDecimal(columnLabel, x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		oracle.updateString(columnLabel, x);
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		oracle.updateBytes(columnLabel, x);
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		oracle.updateDate(columnLabel, x);
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		oracle.updateTime(columnLabel, x);
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		oracle.updateTimestamp(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		oracle.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		oracle.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		oracle.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		oracle.updateObject(columnLabel, x, scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		oracle.updateObject(columnLabel, x);
	}

	@Override
	public void insertRow() throws SQLException {
		oracle.insertRow();
	}

	@Override
	public void updateRow() throws SQLException {
		oracle.updateRow();
	}

	@Override
	public void deleteRow() throws SQLException {
		oracle.deleteRow();
	}

	@Override
	public void refreshRow() throws SQLException {
		oracle.refreshRow();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		oracle.cancelRowUpdates();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		oracle.moveToInsertRow();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		oracle.moveToCurrentRow();
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return oracle.getObject(columnIndex, map);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return oracle.getRef(columnIndex);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return oracle.getBlob(columnIndex);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return oracle.getClob(columnIndex);
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		return oracle.getArray(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return oracle.getObject(columnLabel, map);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return oracle.getRef(columnLabel);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return oracle.getBlob(columnLabel);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return oracle.getClob(columnLabel);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return oracle.getArray(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return oracle.getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return oracle.getDate(columnLabel, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return oracle.getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return oracle.getTime(columnLabel, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return oracle.getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return oracle.getTimestamp(columnLabel, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return oracle.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return oracle.getURL(columnLabel);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		oracle.updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		oracle.updateRef(columnLabel, x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		oracle.updateBlob(columnIndex, x);
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		oracle.updateBlob(columnLabel, x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		oracle.updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		oracle.updateClob(columnLabel, x);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		oracle.updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		oracle.updateArray(columnLabel, x);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return oracle.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return oracle.getRowId(columnLabel);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		oracle.updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		oracle.updateRowId(columnLabel, x);
	}

	@Override
	public int getHoldability() throws SQLException {
		return oracle.getHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return oracle.isClosed();
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		oracle.updateNString(columnIndex, nString);
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		oracle.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		oracle.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		oracle.updateNClob(columnLabel, nClob);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return oracle.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return oracle.getNClob(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return oracle.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return oracle.getSQLXML(columnLabel);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		oracle.updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		oracle.updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return oracle.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return oracle.getNString(columnLabel);
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return oracle.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return oracle.getNCharacterStream(columnLabel);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		oracle.updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		oracle.updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		oracle.updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		oracle.updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		oracle.updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		oracle.updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		oracle.updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		oracle.updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		oracle.updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		oracle.updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		oracle.updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		oracle.updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		oracle.updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		oracle.updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		oracle.updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		oracle.updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		oracle.updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		oracle.updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		oracle.updateCharacterStream(columnIndex, x);
		
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		oracle.updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		oracle.updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		oracle.updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		oracle.updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		oracle.updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		oracle.updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		oracle.updateClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		oracle.updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		oracle.updateNClob(columnLabel, reader);
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return oracle.getObject(columnIndex, type);
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return oracle.getObject(columnLabel, type);
	}

	@Override
	public ARRAY getARRAY(int columnIndex) throws SQLException {
		return oracle.getARRAY(columnIndex);
	}

	@Override
	public ARRAY getARRAY(String columnLabel) throws SQLException {
		return oracle.getARRAY(columnLabel);
	}

	@Override
	public AuthorizationIndicator getAuthorizationIndicator(int columnIndex) throws SQLException {
		return oracle.getAuthorizationIndicator(columnIndex);
	}

	@Override
	public AuthorizationIndicator getAuthorizationIndicator(String columnLabel) throws SQLException {
		return oracle.getAuthorizationIndicator(columnLabel);
	}

	@Override
	public BFILE getBFILE(int columnIndex) throws SQLException {
		return oracle.getBFILE(columnIndex);
	}

	@Override
	public BFILE getBFILE(String columnLabel) throws SQLException {
		return oracle.getBFILE(columnLabel);
	}

	@Override
	public BLOB getBLOB(int columnIndex) throws SQLException {
		return oracle.getBLOB(columnIndex);
	}

	@Override
	public BLOB getBLOB(String columnLabel) throws SQLException {
		return oracle.getBLOB(columnLabel);
	}

	@Override
	public BFILE getBfile(int columnIndex) throws SQLException {
		return oracle.getBfile(columnIndex);
	}

	@Override
	public BFILE getBfile(String columnLabel) throws SQLException {
		return oracle.getBfile(columnLabel);
	}

	@Override
	public CHAR getCHAR(int columnIndex) throws SQLException {
		return oracle.getCHAR(columnIndex);
	}

	@Override
	public CHAR getCHAR(String columnLabel) throws SQLException {
		return oracle.getCHAR(columnLabel);
	}

	@Override
	public CLOB getCLOB(int columnIndex) throws SQLException {
		return oracle.getCLOB(columnIndex);
	}

	@Override
	public CLOB getCLOB(String columnLabel) throws SQLException {
		return oracle.getCLOB(columnLabel);
	}

	@Override
	public ResultSet getCursor(int columnIndex) throws SQLException {
		return oracle.getCursor(columnIndex);
	}

	@Override
	public ResultSet getCursor(String columnLabel) throws SQLException {
		return oracle.getCursor(columnLabel);
	}

	@Deprecated
	@Override
	public CustomDatum getCustomDatum(int columnIndex, CustomDatumFactory factory) throws SQLException {
		return oracle.getCustomDatum(columnIndex, factory);
	}

	@Deprecated
	@Override
	public CustomDatum getCustomDatum(String columnLabel, CustomDatumFactory factory) throws SQLException {
		return oracle.getCustomDatum(columnLabel, factory);
	}

	@Override
	public DATE getDATE(int columnIndex) throws SQLException {
		return oracle.getDATE(columnIndex);
	}

	@Override
	public DATE getDATE(String columnLabel) throws SQLException {
		return oracle.getDATE(columnLabel);
	}

	@Override
	public INTERVALDS getINTERVALDS(int columnIndex) throws SQLException {
		return oracle.getINTERVALDS(columnIndex);
	}

	@Override
	public INTERVALDS getINTERVALDS(String columnLabel) throws SQLException {
		return oracle.getINTERVALDS(columnLabel);
	}

	@Override
	public INTERVALYM getINTERVALYM(int columnIndex) throws SQLException {
		return oracle.getINTERVALYM(columnIndex);
	}

	@Override
	public INTERVALYM getINTERVALYM(String columnLabel) throws SQLException {
		return oracle.getINTERVALYM(columnLabel);
	}

	@Override
	public NUMBER getNUMBER(int columnIndex) throws SQLException {
		return oracle.getNUMBER(columnIndex);
	}

	@Override
	public NUMBER getNUMBER(String columnLabel) throws SQLException {
		return oracle.getNUMBER(columnLabel);
	}

	@Override
	public OPAQUE getOPAQUE(int columnIndex) throws SQLException {
		return oracle.getOPAQUE(columnIndex);
	}

	@Override
	public OPAQUE getOPAQUE(String columnLabel) throws SQLException {
		return oracle.getOPAQUE(columnLabel);
	}

	@Override
	public ORAData getORAData(int columnIndex, ORADataFactory factory) throws SQLException {
		return oracle.getORAData(columnIndex, factory);
	}

	@Override
	public ORAData getORAData(String columnLabel, ORADataFactory factory) throws SQLException {
		return oracle.getORAData(columnLabel, factory);
	}

	@Override
	public Object getObject(int columnIndex, OracleDataFactory factory) throws SQLException {
		return oracle.getObject(columnIndex, factory);
	}

	@Override
	public Object getObject(String columnLabel, OracleDataFactory factory) throws SQLException {
		return oracle.getObject(columnLabel, factory);
	}

	@Override
	public Datum getOracleObject(int columnIndex) throws SQLException {
		return oracle.getOracleObject(columnIndex);
	}

	@Override
	public Datum getOracleObject(String columnLabel) throws SQLException {
		return oracle.getOracleObject(columnLabel);
	}

	@Override
	public RAW getRAW(int columnIndex) throws SQLException {
		return oracle.getRAW(columnIndex);
	}

	@Override
	public RAW getRAW(String columnLabel) throws SQLException {
		return oracle.getRAW(columnLabel);
	}

	@Override
	public REF getREF(int columnIndex) throws SQLException {
		return oracle.getREF(columnIndex);
	}

	@Override
	public REF getREF(String columnLabel) throws SQLException {
		return oracle.getREF(columnLabel);
	}

	@Override
	public ROWID getROWID(int columnIndex) throws SQLException {
		return oracle.getROWID(columnIndex);
	}

	@Override
	public ROWID getROWID(String columnLabel) throws SQLException {
		return oracle.getROWID(columnLabel);
	}

	@Override
	public STRUCT getSTRUCT(int columnIndex) throws SQLException {
		return oracle.getSTRUCT(columnIndex);
	}

	@Override
	public STRUCT getSTRUCT(String columnLabel) throws SQLException {
		return oracle.getSTRUCT(columnLabel);
	}

	@Override
	public TIMESTAMP getTIMESTAMP(int columnIndex) throws SQLException {
		return oracle.getTIMESTAMP(columnIndex);
	}

	@Override
	public TIMESTAMP getTIMESTAMP(String columnLabel) throws SQLException {
		return oracle.getTIMESTAMP(columnLabel);
	}

	@Override
	public TIMESTAMPLTZ getTIMESTAMPLTZ(int columnIndex) throws SQLException {
		return oracle.getTIMESTAMPLTZ(columnIndex);
	}

	@Override
	public TIMESTAMPLTZ getTIMESTAMPLTZ(String columnLabel) throws SQLException {
		return oracle.getTIMESTAMPLTZ(columnLabel);
	}

	@Override
	public TIMESTAMPTZ getTIMESTAMPTZ(int columnIndex) throws SQLException {
		return oracle.getTIMESTAMPTZ(columnIndex);
	}

	@Override
	public TIMESTAMPTZ getTIMESTAMPTZ(String columnLabel) throws SQLException {
		return oracle.getTIMESTAMPTZ(columnLabel);
	}

	@Override
	public void updateARRAY(int columnIndex, ARRAY array) throws SQLException {
		oracle.updateARRAY(columnIndex, array);
	}

	@Override
	public void updateARRAY(String columnLabel, ARRAY array) throws SQLException {
		oracle.updateARRAY(columnLabel, array);
	}

	@Override
	public void updateBFILE(int columnIndex, BFILE x) throws SQLException {
		oracle.updateBFILE(columnIndex, x);
	}

	@Override
	public void updateBFILE(String columnLabel, BFILE x) throws SQLException {
		oracle.updateBFILE(columnLabel, x);
	}

	@Override
	public void updateBLOB(int columnIndex, BLOB x) throws SQLException {
		oracle.updateBLOB(columnIndex, x);
	}

	@Override
	public void updateBLOB(String columnLabel, BLOB x) throws SQLException {
		oracle.updateBLOB(columnLabel, x);
	}

	@Override
	public void updateBfile(int columnIndex, BFILE x) throws SQLException {
		oracle.updateBfile(columnIndex, x);
	}

	@Override
	public void updateBfile(String columnLabel, BFILE x) throws SQLException {
		oracle.updateBfile(columnLabel, x);
	}

	@Override
	public void updateCHAR(int columnIndex, CHAR x) throws SQLException {
		oracle.updateCHAR(columnIndex, x);
	}

	@Override
	public void updateCHAR(String columnLabel, CHAR x) throws SQLException {
		oracle.updateCHAR(columnLabel, x);
	}

	@Override
	public void updateCLOB(int columnIndex, CLOB x) throws SQLException {
		oracle.updateCLOB(columnIndex, x);
	}

	@Override
	public void updateCLOB(String columnLabel, CLOB x) throws SQLException {
		oracle.updateCLOB(columnLabel, x);
	}

	@Deprecated
	@Override
	public void updateCustomDatum(int columnIndex, CustomDatum x) throws SQLException {
		oracle.updateCustomDatum(columnIndex, x);
	}

	@Deprecated
	@Override
	public void updateCustomDatum(String columnLabel, CustomDatum x) throws SQLException {
		oracle.updateCustomDatum(columnLabel, x);
	}

	@Override
	public void updateDATE(int columnIndex, DATE x) throws SQLException {
		oracle.updateDATE(columnIndex, x);
	}

	@Override
	public void updateDATE(String columnLabel, DATE x) throws SQLException {
		oracle.updateDATE(columnLabel, x);
	}

	@Override
	public void updateINTERVALDS(int columnIndex, INTERVALDS x) throws SQLException {
		oracle.updateINTERVALDS(columnIndex, x);
	}

	@Override
	public void updateINTERVALDS(String columnLabel, INTERVALDS x) throws SQLException {
		oracle.updateINTERVALDS(columnLabel, x);
	}

	@Override
	public void updateINTERVALYM(int columnIndex, INTERVALYM x) throws SQLException {
		oracle.updateINTERVALYM(columnIndex, x);
	}

	@Override
	public void updateINTERVALYM(String columnLabel, INTERVALYM x) throws SQLException {
		oracle.updateINTERVALYM(columnLabel, x);
	}

	@Override
	public void updateNUMBER(int columnIndex, NUMBER x) throws SQLException {
		oracle.updateNUMBER(columnIndex, x);
	}

	@Override
	public void updateNUMBER(String columnLabel, NUMBER x) throws SQLException {
		oracle.updateNUMBER(columnLabel, x);
	}

	@Override
	public void updateORAData(int columnIndex, ORAData x) throws SQLException {
		oracle.updateORAData(columnIndex, x);
	}

	@Override
	public void updateORAData(String columnLabel, ORAData x) throws SQLException {
		oracle.updateORAData(columnLabel, x);
	}

	@Override
	public void updateOracleObject(int columnIndex, Datum x) throws SQLException {
		oracle.updateOracleObject(columnIndex, x);
	}

	@Override
	public void updateOracleObject(String columnLabel, Datum x) throws SQLException {
		oracle.updateOracleObject(columnLabel, x);
	}

	@Override
	public void updateRAW(int columnIndex, RAW x) throws SQLException {
		oracle.updateRAW(columnIndex, x);
	}

	@Override
	public void updateRAW(String columnLabel, RAW x) throws SQLException {
		oracle.updateRAW(columnLabel, x);
	}

	@Override
	public void updateREF(int columnIndex, REF x) throws SQLException {
		oracle.updateREF(columnIndex, x);
	}

	@Override
	public void updateREF(String columnLabel, REF x) throws SQLException {
		oracle.updateREF(columnLabel, x);
	}

	@Override
	public void updateROWID(int columnIndex, ROWID x) throws SQLException {
		oracle.updateROWID(columnIndex, x);
	}

	@Override
	public void updateROWID(String columnLabel, ROWID x) throws SQLException {
		oracle.updateROWID(columnLabel, x);
	}

	@Override
	public void updateSTRUCT(int columnIndex, STRUCT x) throws SQLException {
		oracle.updateSTRUCT(columnIndex, x);
	}

	@Override
	public void updateSTRUCT(String columnLabel, STRUCT x) throws SQLException {
		oracle.updateSTRUCT(columnLabel, x);
	}

	@Override
	public void updateTIMESTAMP(int columnIndex, TIMESTAMP x) throws SQLException {
		oracle.updateTIMESTAMP(columnIndex, x);
	}

	@Override
	public void updateTIMESTAMP(String columnLabel, TIMESTAMP x) throws SQLException {
		oracle.updateTIMESTAMP(columnLabel, x);
	}

	@Override
	public void updateTIMESTAMPLTZ(int columnIndex, TIMESTAMPLTZ x) throws SQLException {
		oracle.updateTIMESTAMPLTZ(columnIndex, x);
	}

	@Override
	public void updateTIMESTAMPLTZ(String columnLabel, TIMESTAMPLTZ x) throws SQLException {
		oracle.updateTIMESTAMPLTZ(columnLabel, x);
	}

	@Override
	public void updateTIMESTAMPTZ(int columnIndex, TIMESTAMPTZ x) throws SQLException {
		oracle.updateTIMESTAMPTZ(columnIndex, x);
	}

	@Override
	public void updateTIMESTAMPTZ(String columnLabel, TIMESTAMPTZ x) throws SQLException {
		oracle.updateTIMESTAMPTZ(columnLabel, x);
	}

}
