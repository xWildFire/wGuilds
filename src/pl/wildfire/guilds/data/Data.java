/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import pl.wildfire.guilds.GuildsPlugin;

public class Data {

    private static GuildsPlugin plugin;
    private static Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

    public Data(GuildsPlugin pg) {
        plugin = pg;
    }

    public static Scoreboard getSb() {
        return sb;
    }

    private static long time = System.currentTimeMillis();
    private static List<Guild> kills = null;
    private static List<Guild> deaths = null;
    private static List<Guild> points = null;

    public static List<Guild> getTopKill() {
        if (kills != null && time + 300000 >= System.currentTimeMillis()) {
            return kills;
        }
        List<Guild> list = new ArrayList<Guild>();
        List<Guild> gi = new ArrayList<Guild>();

        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            gi.add(it.next().getValue());
        }

        for (int i = 0; i <= 19; i++) {
            Guild g = null;
            for (Guild gg : gi) {
                if (g == null) {
                    g = gg;
                } else {
                    if (gg.getKills() > g.getKills()) {
                        g = gg;
                    }
                }
            }
            gi.remove(g);
            list.add(g);
        }
        return kills = list;
    }

    public static List<Guild> getTopDeath() {
        if (deaths != null && time + 300000 >= System.currentTimeMillis()) {
            return deaths;
        }
        List<Guild> list = new ArrayList<Guild>();
        List<Guild> gi = new ArrayList<Guild>();

        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            gi.add(it.next().getValue());
        }

        for (int i = 0; i <= 19; i++) {
            Guild g = null;
            for (Guild gg : gi) {
                if (g == null) {
                    g = gg;
                } else {
                    if (gg.getDeaths() > g.getDeaths()) {
                        g = gg;
                    }
                }
            }
            gi.remove(g);
            list.add(g);
        }
        return deaths = list;
    }

    public static List<Guild> getTopPoints() {
        if (points != null && time + 300000 >= System.currentTimeMillis()) {
            return points;
        }
        List<Guild> list = new ArrayList<Guild>();
        List<Guild> gi = new ArrayList<Guild>();

        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            gi.add(it.next().getValue());
        }

        for (int i = 0; i <= 19; i++) {
            Guild g = null;
            for (Guild gg : gi) {
                if (g == null) {
                    g = gg;
                } else {
                    if (gg.getPoints() > g.getPoints()) {
                        g = gg;
                    }
                }
            }
            gi.remove(g);
            list.add(g);
        }
        return points = list;
    }

}
