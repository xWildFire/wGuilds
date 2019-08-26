/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbResult {

    protected Statement s;
    protected ResultSet result;

    public DbResult(final Statement ss, final String sql) throws SQLException {
        s = ss;
        result = s.executeQuery(sql);
    }

    public void nextResult(final String sql) throws SQLException {
        if (result != null) {
            result.close();
        }
        result = s.executeQuery(sql);
    }

    public void close() throws SQLException {
        result.close();
        s.close();
    }

    public String getString(final String col) throws SQLException {
        return result.getString(col);
    }

    public int getInt(final String col) throws SQLException {
        return result.getInt(col);
    }

    public double getDouble(final String col) throws SQLException {
        return result.getDouble(col);
    }

    public boolean getBoolean(final String col) throws SQLException {
        return result.getBoolean(col);
    }

    public int getRow() throws SQLException {
        return result.getRow();
    }

    public long getLong(final String col) throws SQLException {
        return result.getLong(col);
    }

    public boolean next() throws SQLException {
        return result.next();
    }

    public void update(final String sql) throws SQLException {
        s.executeUpdate(sql);
    }

    public void update(final String[] sqls) throws SQLException {
        for (final String sql : sqls) {
            s.addBatch(sql);
        }
        s.executeBatch();
    }
}
