package pl.wildfire.guilds.mysql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.bukkit.Location;
import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Config;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Msg;

public class Db {

    private final String host, user, pass, db;
    private final int port;
    private Connection con = null;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Db(String host, int port, String user, String pass, String db) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.db = db;
    }

    public boolean connect() {
        try {
            if (Config.getConfig("config").getBoolean("mysql.use")) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);

                DbStatement stat = createStatement();
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Guilds` (Tag VARCHAR(5), Nazwa VARCHAR(20), Cuboid INT(100), x DOUBLE, y DOUBLE, z DOUBLE, Data BIGINT, Punkty BIGINT, Smierci BIGINT, Zabojstwa BIGINT, PRIMARY KEY (Tag));");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Users` (Gracz VARCHAR(20), Tag VARCHAR(10), Lider VARCHAR(10), PRIMARY KEY (Gracz));");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Allies` (Gildia1 VARCHAR(5), Gildia2 VARCHAR(5));");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Bans` (Tag VARCHAR(5), Powod VARCHAR(100), Czas LONG, PRIMARY KEY (Tag));");
                stat.runAndClose();

                Msg.sendConsole(Lang.CON_MYSQL);
                return true;
            } else {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:" + new File("plugins" + File.separator + "wGuilds" + File.separator + "SQLite.db").getAbsolutePath());

                DbStatement stat = createStatement();
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Guilds` (Tag, Nazwa, Cuboid, Zabojstwa, Punkty, Smierci, x, y, z, Data, PRIMARY KEY (Tag));");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Users` (Gracz, Tag, Nazwa, Lider, PRIMARY KEY (Gracz));");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Allies` (Gildia1, Gildia2);");
                stat.addToStatement("CREATE TABLE IF NOT EXISTS `wGuilds_Bans` (Tag, Powod, Czas, PRIMARY KEY (Tag));");
                stat.runAndClose();

                Msg.sendConsole(Lang.CON_SQLITE);
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            Msg.log(e);
        }
        return false;
    }

    private DbStatement createStatement() {
        try {
            return new DbStatement(con.createStatement());
        } catch (SQLException e) {
            Msg.log(e);
            check();
        }
        return null;
    }

    private void runStatement(final String... sqls) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new DbStatement(con.createStatement(), sqls).runAndClose();
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }
            }
        });
    }

    private DbResult getResult(String sql) throws SQLException {
        return new DbResult(con.createStatement(), sql);
    }

    public void check() {
        try {
            if (con != null && !con.isClosed() && (con.getClass().getMethod("idValid", Integer.class) != null && con.isValid(3))) {
                return;
            }
        } catch (Exception e) {
        }

        if (!connect()) {
            Msg.sendConsole("Connection to the database has died!");
        }
    }

    public void createGuild(String tag, String nazwa, int cuboid, Location l, long data) {
        runStatement("INSERT INTO `wGuilds_Guilds` (Tag, Nazwa, Cuboid, x, y, z, Data, Punkty, Smierci, Zabojstwa) VALUES ('" + tag + "', '" + nazwa + "', '" + cuboid + "', '" + l.getX() + "', '" + l.getY() + "', '" + l.getZ() + "', '" + data + "', '500', '0', '0');");
    }

    public void deleteGuild(String tag) {
        DbStatement stat = createStatement();
        stat.addToStatement("DELETE FROM `wGuilds_Guilds` WHERE Tag='" + tag + "';");
        stat.addToStatement("DELETE FROM `wGuilds_Users` WHERE Tag='" + tag + "';");
        stat.addToStatement("DELETE FROM `wGuilds_Allies` WHERE Gildia1='" + tag + "' OR Gildia2='" + tag + "';");
        stat.runAndClose();
    }

    public void setCuboid(String tag, int wielkosc) {
        runStatement("UPDATE `wGuilds_Guilds` SET Cuboid='" + wielkosc + "' WHERE Tag='" + tag + "';");
    }

    public void setHome(String tag, double x, double y, double z) {
        DbStatement stat = createStatement();
        stat.addToStatement("UPDATE `wGuilds_Guilds` SET x='" + x + "' WHERE Tag='" + tag + "';");
        stat.addToStatement("UPDATE `wGuilds_Guilds` SET y='" + y + "' WHERE Tag='" + tag + "';");
        stat.addToStatement("UPDATE `wGuilds_Guilds` SET z='" + z + "' WHERE Tag='" + tag + "';");
        stat.runAndClose();
    }

    public void setDate(String tag, long data) {
        runStatement("UPDATE `wGuilds_Guilds` SET Data='" + data + "' WHERE Tag='" + tag + "';");
    }

    public void addPlayer(String player, String tag, String lider) {
        runStatement("INSERT INTO `wGuilds_Users` (Gracz, Tag, Lider) VALUES ('" + player + "', '" + tag + "', '" + lider + "') ON DUPLICATE KEY UPDATE Tag='" + tag + "' AND Lider='" + lider + "';");
    }

    public void remPlayer(String player) {
        runStatement("DELETE FROM `wGuilds_Users` WHERE Gracz='" + player + "';");
    }

    public void addAlly(String tag, String tag2) {
        runStatement("INSERT INTO `wGuilds_Allies` (Gildia1, Gildia2) VALUES ('" + tag + "', '" + tag2 + "');");
    }

    public void remAlly(String tag, String tag2) {
        runStatement("DELETE FROM `wGuilds_Allies` WHERE Gildia1='" + tag + "' AND Gildia2='" + tag2 + "' OR Gildia2='" + tag + "' AND Gildia1='" + tag2 + "';");
    }

    public void addBan(String tag, String powod, long czas) {
        runStatement("INSERT INTO `wGuilds_Bans` (Tag, Powod, Czas) VALUES ('" + tag + "', '" + powod + "', '" + czas + "');");
    }

    public void remBan(String tag) {
        runStatement("DELETE FROM `wGuilds_Bans` WHERE Tag='" + tag + "';");
    }

    public void addKill(String player) {
        runStatement("UPDATE `wGuilds_Guilds` SET Zabojstwa=Zabojstwa+1 WHERE Tag='" + player + "';");
    }

    public void addPoints(String tag, long l) {
        runStatement("UPDATE `wGuilds_Guilds` SET Punkty=Punkty+" + l + " WHERE Tag='" + tag + "';");
    }

    public void addDeath(String tag) {
        runStatement("UPDATE `wGuilds_Guilds` SET Smierci=Smierci+1 WHERE Tag='" + tag + "';");
    }

    public void setTagAndName(String oldt, String newt, String oldn, String newn) {
        DbStatement stat = createStatement();
        stat.addToStatement("UPDATE `wGuilds_Guilds` SET Tag='" + newt + "' WHERE Tag='" + oldt + "';");
        stat.addToStatement("UPDATE `wGuilds_Users` SET Tag='" + newt + "' WHERE Tag='" + oldt + "';");
        stat.addToStatement("UPDATE `wGuilds_Allies` SET Gildia1='" + newt + "' WHERE Gildia1='" + oldt + "' OR Gildia2='" + oldt + "';");
        stat.addToStatement("UPDATE `wGuilds_Guilds` SET Nazwa='" + newn + "' WHERE Nazwa='" + oldn + "';");
        stat.runAndClose();
    }

    public void setLider(String oldl, String newl) {
        DbStatement stat = createStatement();
        stat.addToStatement("UPDATE `wGuilds_Users` SET Lider='tak' WHERE Gracz='" + newl + "';");
        stat.addToStatement("UPDATE `wGuilds_Users` SET Lider='nie' WHERE Gracz='" + oldl + "';");
        stat.runAndClose();
    }

    public void setMod(String p) {
        runStatement("UPDATE `wGuilds_Users` SET Lider='mod' WHERE Gracz='" + p + "';");
    }

    public void setPlayer(String gracz) {
        runStatement("UPDATE `wGuilds_Users` SET Lider='nie' WHERE Gracz='" + gracz + "';");
    }

    public void loadGuild(final String gildia) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Guilds` WHERE Tag='" + gildia + "'");
                    while (r.next()) {
                        new Guild(r);
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Users` WHERE Tag='" + gildia + "'");
                    while (r.next()) {
                        Guild g = Guild.get(r.getString("Tag"));
                        User u = new User(r.getString("Gracz"), g, false);

                        g.addUser(u, false);
                        if (r.getString("Lider").equalsIgnoreCase("Tak")) {
                            g.setLider(u, false);
                        } else if (r.getString("Lider").equalsIgnoreCase("Mod")) {
                            g.addMod(u, false);
                        }
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Allies` WHERE Gildia1='" + gildia + "' OR Gildia2='" + gildia + "'");
                    while (r.next()) {
                        String t1 = r.getString("Gildia1");
                        String t2 = r.getString("Gildia2");
                        Guild g1 = Guild.get(t1);
                        Guild g2 = Guild.get(t2);

                        if (g1 != null && g2 != null) {
                            if (!g1.getAllies().contains(g2)) {
                                g1.addAlly(g2, false);
                            }
                            if (!g2.getAllies().contains(g1)) {
                                g2.addAlly(g1, false);
                            }
                        }
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Bans` WHERE Tag='" + gildia + "'");
                    while (r.next()) {
                        Guild.get(r.getString("Tag")).addBan(r.getLong("Czas"), r.getString("Powod"));
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }
            }
        });
    }

    public void loadGuilds() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Guilds`");
                    while (r.next()) {
                        new Guild(r);
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Users`");
                    while (r.next()) {
                        Guild g = Guild.get(r.getString("Tag"));
                        if (g == null) {
                            continue;
                        }
                        User u = new User(r.getString("Gracz"), g, false);

                        g.addUser(u, false);
                        if (r.getString("Lider").equalsIgnoreCase("Tak")) {
                            g.setLider(u, false);
                        } else if (r.getString("Lider").equalsIgnoreCase("Mod")) {
                            g.addMod(u, false);
                        }

                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Allies`");
                    while (r.next()) {
                        String t1 = r.getString("Gildia1");
                        String t2 = r.getString("Gildia2");
                        Guild g1 = Guild.get(t1);
                        Guild g2 = Guild.get(t2);

                        if (g1 == null || g2 == null) {
                            continue;
                        }
                        if (!g1.getAllies().contains(g2)) {
                            g1.addAlly(g2, false);
                        }
                        if (!g2.getAllies().contains(g1)) {
                            g2.addAlly(g1, false);
                        }
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }

                try {
                    DbResult r = getResult("SELECT * FROM `wGuilds_Bans`");
                    while (r.next()) {
                        Guild.get(r.getString("Tag")).addBan(r.getLong("Czas"), r.getString("Powod"));
                    }
                } catch (SQLException e) {
                    Msg.log(e);
                    check();
                }
            }
        });
    }
}
