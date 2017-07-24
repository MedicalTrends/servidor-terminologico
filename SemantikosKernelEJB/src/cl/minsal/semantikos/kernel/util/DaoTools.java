package cl.minsal.semantikos.kernel.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by root on 26-07-16.
 */
public class DaoTools {
    static public Integer getInteger(ResultSet rs, String strColName) throws SQLException {
        int nValue = rs.getInt(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public Float getFloat(ResultSet rs, String strColName) throws SQLException {
        Float nValue = rs.getFloat(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public Boolean getBoolean(ResultSet rs, String strColName) throws SQLException {
        Boolean nValue = rs.getBoolean(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public Long getLong(ResultSet rs, String strColName) throws SQLException {
        Long nValue = rs.getLong(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public String getString(ResultSet rs, String strColName) throws SQLException {
        String nValue = rs.getString(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public Date getDate(ResultSet rs, String strColName) throws SQLException {
        Date nValue = rs.getDate(strColName);
        return rs.wasNull() ? null : nValue;
    }

    static public Timestamp getTimestamp(ResultSet rs, String strColName) throws SQLException {
        Timestamp nValue = rs.getTimestamp(strColName);
        return rs.wasNull() ? null : nValue;
    }
}
