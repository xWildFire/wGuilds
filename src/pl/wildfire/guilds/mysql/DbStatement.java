/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import pl.wildfire.guilds.GuildsPlugin;
import pl.wildfire.guilds.managers.Msg;

public final class DbStatement {

    protected Statement s;

    public DbStatement(final Statement ss) {
        s = ss;
    }

    public DbStatement(final Statement ss, final String... sqls) throws SQLException {
        s = ss;
        addToStatement(sqls);
    }

    public void addToStatement(final String... sqls) {
        try {
            for (final String sql : sqls) {
                s.addBatch(sql);
            }
        } catch (SQLException e) {
            Msg.log(e);
            GuildsPlugin.getDb().check();
        }
    }

    public void runAndClose() {
        run();
        close();
    }

    private void run() {
        try {
            s.executeBatch();
        } catch (SQLException e) {
            Msg.log(e);
            GuildsPlugin.getDb().check();
        }
    }

    private void close() {
        try {
            s.close();
        } catch (SQLException e) {
            Msg.log(e);
            GuildsPlugin.getDb().check();
        }
    }
}
