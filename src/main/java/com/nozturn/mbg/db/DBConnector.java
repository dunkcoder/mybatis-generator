package com.nozturn.mbg.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nozturn.mbg.model.Columns;
import com.nozturn.mbg.model.Tables;
import com.nozturn.mbg.properties.PropertiesUtil;

public class DBConnector {

    private static String driverClass = null;
    private static String dbUrl = null;
    private static String dbUser = null;
    private static String dbPasswd = null;

    static {
        if (driverClass == null && dbUrl == null && dbUser == null && dbPasswd == null) {
            synchronized (DBConnector.class) {
                if (driverClass == null || dbUrl == null || dbUser == null || dbPasswd == null) {
                    driverClass = PropertiesUtil.getProperty("driverClass");
                    dbUrl = PropertiesUtil.getProperty("dbUrl");
                    dbUser = PropertiesUtil.getProperty("dbUser");
                    dbPasswd = PropertiesUtil.getProperty("dbPasswd");
                }
            }
        }
    }

    public static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        return DriverManager.getConnection(dbUrl, dbUser, dbPasswd);
    }

    public static void disconnect(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    /**
     * 查询数据库中所有表信息
     */
    public List<Tables> queryAllTables() throws ClassNotFoundException, SQLException {
        String sql = "select * from information_schema.tables where table_schema=DATABASE()";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Tables> resultList = new ArrayList<Tables>();

        try {
            conn = DBConnector.connect();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            Tables tables = null;

            while (rs.next()) {
                tables = new Tables();
                tables.setTableCatalog(rs.getString("TABLE_CATALOG"));
                tables.setTableSchema(rs.getString("TABLE_SCHEMA"));
                tables.setTableName(rs.getString("TABLE_NAME"));
                tables.setTableType(rs.getString("TABLE_TYPE"));
                tables.setEngine(rs.getString("ENGINE"));
                tables.setVersion(rs.getLong("VERSION"));
                tables.setRowFormat(rs.getString("ROW_FORMAT"));
                tables.setTableRows(rs.getLong("TABLE_ROWS"));
                tables.setAvgRowLength(rs.getLong("AVG_ROW_LENGTH"));
                tables.setDataLength(rs.getLong("DATA_LENGTH"));
                tables.setMaxDataLength(rs.getLong("MAX_DATA_LENGTH"));
                tables.setIndexLength(rs.getLong("INDEX_LENGTH"));
                tables.setDataFree(rs.getLong("DATA_FREE"));
                tables.setAutoIncrement(rs.getLong("AUTO_INCREMENT"));
                tables.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                tables.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
                tables.setCheckTime(rs.getTimestamp("CHECK_TIME"));
                tables.setTableCollation(rs.getString("TABLE_COLLATION"));
                tables.setChecksum(rs.getLong("CHECKSUM"));
                tables.setCreateOptions(rs.getString("CREATE_OPTIONS"));
                tables.setTableComment(rs.getString("TABLE_COMMENT"));

                resultList.add(tables);
            }

        } catch (ClassNotFoundException cnfe) {
            throw cnfe;
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            DBConnector.disconnect(conn, stmt, rs);
        }

        return resultList;
    }

    /**
     * 查询数据库中表名称为tableName的信息
     */
    public Tables queryTableByTableName(String tableName) throws ClassNotFoundException, SQLException {
        String sqlTempl = "select * from information_schema.tables where table_schema=DATABASE() and table_name = '%s'";
        String sql = String.format(sqlTempl, tableName);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Tables tables = null;

        try {
            conn = DBConnector.connect();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                tables = new Tables();
                tables.setTableCatalog(rs.getString("TABLE_CATALOG"));
                tables.setTableSchema(rs.getString("TABLE_SCHEMA"));
                tables.setTableName(rs.getString("TABLE_NAME"));
                tables.setTableType(rs.getString("TABLE_TYPE"));
                tables.setEngine(rs.getString("ENGINE"));
                tables.setVersion(rs.getLong("VERSION"));
                tables.setRowFormat(rs.getString("ROW_FORMAT"));
                tables.setTableRows(rs.getLong("TABLE_ROWS"));
                tables.setAvgRowLength(rs.getLong("AVG_ROW_LENGTH"));
                tables.setDataLength(rs.getLong("DATA_LENGTH"));
                tables.setMaxDataLength(rs.getLong("MAX_DATA_LENGTH"));
                tables.setIndexLength(rs.getLong("INDEX_LENGTH"));
                tables.setDataFree(rs.getLong("DATA_FREE"));
                tables.setAutoIncrement(rs.getLong("AUTO_INCREMENT"));
                tables.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                tables.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
                tables.setCheckTime(rs.getTimestamp("CHECK_TIME"));
                tables.setTableCollation(rs.getString("TABLE_COLLATION"));
                tables.setChecksum(rs.getLong("CHECKSUM"));
                tables.setCreateOptions(rs.getString("CREATE_OPTIONS"));
                tables.setTableComment(rs.getString("TABLE_COMMENT"));
            }

        } catch (ClassNotFoundException cnfe) {
            throw cnfe;
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            DBConnector.disconnect(conn, stmt, rs);
        }

        return tables;
    }

    /**
     * 根据表明查询表内所有字段的信息
     */
    public List<Columns> queryColumnsByTableName(String tableName) throws ClassNotFoundException, SQLException {
        String sql = "select * from information_schema.columns where table_schema=DATABASE() and table_name='" + tableName + "'";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Columns> resultList = new ArrayList<Columns>();

        try {
            conn = DBConnector.connect();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            Columns columns = null;
            while (rs.next()) {
                columns = new Columns();
                columns.setTableCatalog(rs.getString("TABLE_CATALOG"));
                columns.setTableSchema(rs.getString("TABLE_SCHEMA"));
                columns.setTableName(rs.getString("TABLE_NAME"));
                columns.setColumnName(rs.getString("COLUMN_NAME"));
                columns.setOrdinalPosition(rs.getLong("ORDINAL_POSITION"));
                columns.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
                columns.setIsNullable(rs.getString("IS_NULLABLE"));
                columns.setDataType(rs.getString("DATA_TYPE"));
                columns.setCharacterMaximumLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
                columns.setCharacterOctetLength(rs.getLong("CHARACTER_OCTET_LENGTH"));
                columns.setNumericPrecision(rs.getLong("NUMERIC_PRECISION"));
                columns.setNumericScale(rs.getLong("NUMERIC_SCALE"));
                columns.setDatetimePrecision(rs.getLong("DATETIME_PRECISION"));
                columns.setCharacterSetName(rs.getString("CHARACTER_SET_NAME"));
                columns.setCollationName(rs.getString("COLLATION_NAME"));
                columns.setColumnType(rs.getString("COLUMN_TYPE"));
                columns.setColumnKey(rs.getString("COLUMN_KEY"));
                columns.setExtra(rs.getString("EXTRA"));
                columns.setPrivileges(rs.getString("PRIVILEGES"));
                columns.setColumnComment(rs.getString("COLUMN_COMMENT"));

                resultList.add(columns);
            }

        } catch (ClassNotFoundException cnfe) {
            throw cnfe;
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            DBConnector.disconnect(conn, stmt, rs);
        }

        return resultList;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.out.println(new DBConnector().queryTableByTableName("t_user"));
    }

}
