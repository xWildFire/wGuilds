/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import pl.wildfire.guilds.GuildsPlugin;

import pl.wildfire.guilds.managers.Config;
import pl.wildfire.guilds.managers.Msg;
import pl.wildfire.guilds.mysql.DbResult;

public class Guild {

    private String tag = "";
    private String name = "";
    private int kills = 0;
    private int deaths = 0;
    private long points = 0;
    private User lider = null;
    private List<User> mods = new ArrayList<User>();
    private List<User> players = new ArrayList<User>();
    private List<Guild> allies = new ArrayList<Guild>();
    private List<Guild> invally = new ArrayList<Guild>();
    private long bantime = 0;
    private String banreason = "";
    private long tnt = 0;
    private long data = 0;
    private int cuboid = 0;
    private Location home;

    public Guild(String t, String n, int c, Location hom, long d, User l, List<User> m, List<User> g, List<Guild> s, long b, String bp, long po, int ki, int de) {
        tag = t.toUpperCase();
        name = n;
        cuboid = c;
        home = hom;
        points = po;
        deaths = de;
        kills = ki;
        data = d;
        lider = l;
        mods = m;
        players = g;
        allies = s;
        bantime = b;
        banreason = bp;

        guilds.put(tag.toUpperCase(), this);
    }

    public Guild(DbResult r) throws SQLException {
        double x = r.getDouble("x");
        double y = r.getDouble("y");
        double z = r.getDouble("z");

        tag = r.getString("Tag").toUpperCase();
        name = r.getString("Nazwa");
        cuboid = r.getInt("Cuboid");
        home = new Location(Bukkit.getWorld(Config.getConfig("config").getString("gildie.swiat")), x, y, z);
        points = r.getLong("Punkty");
        deaths = r.getInt("Smierci");
        kills = r.getInt("Zabojstwa");
        data = r.getLong("Data");

        guilds.put(tag.toUpperCase(), this);
    }

    public String getColor(Guild g) {
        if (g.equals(this)) {
            return "§a";
        } else if (allies.contains(g)) {
            return "§9";
        } else {
            return "§c";
        }
    }

    public void sendMsg(List<String> msg) {
        for (User s : players) {
            Player t = s.getPlayer();
            if (t != null) {
                for (String ss : msg) {
                    t.sendMessage(Msg.c(ss));
                }
            }
        }
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public int getCuboid() {
        return cuboid;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public long getPoints() {
        return points;
    }

    public Location getHome() {
        return home;
    }

    public long getDate() {
        return data;
    }

    public User getLider() {
        return lider;
    }

    public List<User> getMods() {
        return mods;
    }

    public List<Guild> getAllies() {
        return allies;
    }

    public List<Guild> getAllyInvs() {
        return invally;
    }

    public void addAllyInv(Guild g) {
        invally.add(g);
    }

    public void remAllyInv(Guild g) {
        invally.remove(g);
    }

    public Long getBanTime() {
        return bantime;
    }

    public String getBanReason() {
        return banreason;
    }

    public boolean isInCub(Location loc) {
        if (loc == null || loc.getWorld() == null || !loc.getWorld().getName().equalsIgnoreCase("world")) {
            return false;
        }
        Vector v = loc.toVector();
        Location loc1 = home;
        Location min = new Location(loc1.getWorld(), loc1.getBlockX() - cuboid / 2, 0, loc1.getBlockZ() - cuboid / 2);
        Location max = new Location(loc1.getWorld(), loc1.getBlockX() + cuboid / 2, 256, loc1.getBlockZ() + cuboid / 2);
        return v.isInAABB(min.toVector(), max.toVector());
    }

    public void setTagAndName(String t, String n) {
        guilds.remove(tag.toUpperCase());
        GuildsPlugin.getDb().setTagAndName(tag, t.toUpperCase(), name, n);

        tag = t.toUpperCase();
        name = n;
        guilds.put(t.toUpperCase(), this);

    }

    public void setCuboid(int c) {
        cuboid = c;
        GuildsPlugin.getDb().setCuboid(tag, c);

    }

    public void addKill(User s) {
        kills += 1;
        GuildsPlugin.getDb().addKill(tag);
    }

    public void addDeath(User s) {
        deaths += 1;
        GuildsPlugin.getDb().addDeath(tag);
    }

    public void addPoints(long i) {
        points += i;
        GuildsPlugin.getDb().addPoints(tag, i);
    }

    public void setHome(Location h) {
        home = h;
        GuildsPlugin.getDb().setHome(tag, h.getX(), h.getY(), h.getZ());

    }

    public void setDate(long d) {
        data = d;
        GuildsPlugin.getDb().setDate(tag, d);

    }

    public void setLider(User l, boolean b) {
        if (b) {
            GuildsPlugin.getDb().setLider(lider.getName(), l.getName());

        }
        lider = l;
    }

    public void addMod(User m, boolean b) {
        mods.add(m);
        if (b) {
            GuildsPlugin.getDb().setMod(m.getName());
        }
    }

    public void remMod(User t) {
        mods.remove(t);
        GuildsPlugin.getDb().setPlayer(t.getName());

    }

    public void addUser(User gracz, boolean b) {
        gracz.setGuild(this, true);
        players.add(gracz);
        if (b) {
            GuildsPlugin.getDb().addPlayer(gracz.getName(), tag, "nie");

        }
    }

    public void remUser(User t) {
        t.setGuild(null, true);
        if (mods.contains(t)) {
            mods.remove(t);
        }

        players.remove(t);
        GuildsPlugin.getDb().remPlayer(t.getName());

    }

    public List<User> getUsers() {
        return players;
    }

    public void remGuild() {
        for (User gracz : players) {
            gracz.setGuild(null, true);
            gracz.setChat(null);
        }

        for (Guild g : allies) {
            if (g != null) {
                g.remAlly(g, false);
            }
        }

        guilds.remove(tag);
        GuildsPlugin.getDb().deleteGuild(tag);

    }

    public void addAlly(Guild i, boolean db) {
        allies.add(i);
        i.getAllies().add(this);
        if (db) {
            GuildsPlugin.getDb().addAlly(tag, i.getTag());

        }
    }

    public void remAlly(Guild i, boolean db) {
        allies.remove(i);
        i.getAllies().remove(this);
        if (db) {
            GuildsPlugin.getDb().remAlly(tag, i.getTag());

        }
    }

    public void addBan(long czas, String powod) {
        banreason = powod;
        bantime = czas;
        GuildsPlugin.getDb().addBan(tag, powod, czas);

    }

    public void remBan() {
        banreason = null;
        bantime = 0;
        GuildsPlugin.getDb().remBan(tag);

    }

    public long getTnt() {
        return tnt;
    }

    public void setTnt(long t) {
        tnt = t;
    }

    public int getKillRank() {
        int ranking = 1;
        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            Entry<String, Guild> n = it.next();
            if (!n.getKey().equalsIgnoreCase(tag)) {
                if (n.getValue().getKills() > kills) {
                    ranking++;
                }
            }
        }
        return ranking;
    }

    public int getDeathRank() {
        int ranking = 1;
        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            Entry<String, Guild> n = it.next();
            if (!n.getKey().equalsIgnoreCase(tag)) {
                if (n.getValue().getDeaths() > deaths) {
                    ranking++;
                }
            }
        }
        return ranking;
    }

    public int getPointsRank() {
        int ranking = 1;
        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            Entry<String, Guild> n = it.next();
            if (!n.getKey().equalsIgnoreCase(tag)) {
                if (n.getValue().getPoints() > points) {
                    ranking++;
                }
            }
        }
        return ranking;
    }

    public void setAllies(List<Guild> al) {
        allies = al;
    }

    public void sendMessage(String c) {
        for (User s : players) {
            Player t = s.getPlayer();
            if (t != null) {
                t.sendMessage(Msg.c(c));
            }
        }
    }

    private static HashMap<String, Guild> guilds = new HashMap<String, Guild>();

    public static Guild get(String t) {
        t = t.toUpperCase();

        if (guilds.containsKey(t)) {
            return guilds.get(t);
        }
        return null;
    }

    public static Guild get(Location to) {
        Iterator<Entry<String, Guild>> it = guilds.entrySet().iterator();
        while (it.hasNext()) {
            Guild g = it.next().getValue();

            if (g.isInCub(to)) {
                return g;
            }
        }
        return null;
    }

    public static boolean free(Location loc, String gildia) {
        Iterator<Entry<String, Guild>> it = guilds.entrySet().iterator();
        while (it.hasNext()) {
            Guild g = it.next().getValue();

            if (gildia == null || gildia != null && !g.getTag().equalsIgnoreCase(gildia)) {

                Location ch = g.getHome();
                Location loc1 = new Location(ch.getWorld(), ch.getX(), 50, ch.getZ());
                Location loc2 = loc;
                loc1.setY(50);
                loc2.setY(50);

                int maxX = Math.max(loc2.getBlockX(), loc1.getBlockX());
                int minX = Math.min(loc2.getBlockX(), loc1.getBlockX());
                int maxZ = Math.max(loc2.getBlockZ(), loc1.getBlockZ());
                int minZ = Math.min(loc2.getBlockZ(), loc1.getBlockZ());

                int pg = 0;
                if (gildia != null) {
                    pg = 1 + (g.getCuboid() / 2) + (Guild.get(gildia).getCuboid() / 2);
                } else {
                    pg = 1 + (g.getCuboid() / 2) + Config.getConfig("config").getInt("cuboid.wielkosc");
                }

                if (maxX - minX < pg && maxZ - minZ < pg) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Iterator<Entry<String, Guild>> iterator() {
        return guilds.entrySet().iterator();
    }

    public static Set<String> list() {
        return guilds.keySet();
    }
}
