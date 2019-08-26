package pl.wildfire.guilds.managers;

import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;

import java.util.ArrayList;
import java.util.List;

public class Variables {

    public static String set(String s, Guild g) {
        if (g == null) {
            return s;
        }
        String p = "$guild_";
        s = s.replace(p + "tag", g.getTag() + "");
        s = s.replace(p + "name", g.getName() + "");
        s = s.replace(p + "kills", g.getKills() + "");
        s = s.replace(p + "deaths", g.getDeaths() + "");
        s = s.replace(p + "points", g.getPoints() + "");
        s = s.replace(p + "lider", g.getLider().getName() + "");
        List<String> mods = new ArrayList<String>();
        for (User u : g.getMods()) {
            mods.add(u.getName());
        }
        s = s.replace(p + "mods", mods.toString().replace("[", "").replace("]", "") + "");
        List<String> users = new ArrayList<String>();
        for (User u : g.getUsers()) {
            users.add(u.getName());
        }
        s = s.replace(p + "users", users.toString().replace("[", "").replace("]", "") + "");
        List<String> allies = new ArrayList<String>();
        for (Guild u : g.getAllies()) {
            allies.add(u.getTag());
        }
        s = s.replace(p + "allies", allies.toString().replace("[", "").replace("]", "") + "");
        s = s.replace(p + "ban-time", Util.getDate(g.getBanTime()) + "");
        s = s.replace(p + "ban-reason", g.getBanReason() + "");
        s = s.replace(p + "tnt", (g.getTnt() - System.currentTimeMillis()) / 1000 + "");
        s = s.replace(p + "cuboid", g.getCuboid() + "");
        s = s.replace(p + "home-x", Math.round(g.getHome().getX()) + "");
        s = s.replace(p + "home-y", Math.round(g.getHome().getY()) + "");
        s = s.replace(p + "home-z", Math.round(g.getHome().getZ()) + "");
        s = s.replace(p + "date", Util.getDate(g.getDate() * 1000));
        return s;
    }

    public static List<String> set(List<String> j, Guild g) {
        if (g == null) {
            return j;
        }
        String p = "$guild_";
        List<String> niu = new ArrayList<String>();
        for (String s : j) {
            s = s.replace(p + "tag", g.getTag() + "");
            s = s.replace(p + "name", g.getName() + "");
            s = s.replace(p + "kills", g.getKills() + "");
            s = s.replace(p + "deaths", g.getDeaths() + "");
            s = s.replace(p + "points", g.getPoints() + "");
            s = s.replace(p + "lider", g.getLider().getName() + "");
            List<String> mods = new ArrayList<String>();
            for (User u : g.getMods()) {
                mods.add(u.getName());
            }
            s = s.replace(p + "mods", mods.toString().replace("[", "").replace("]", "") + "");
            List<String> users = new ArrayList<String>();
            for (User u : g.getUsers()) {
                users.add(u.getName());
            }
            s = s.replace(p + "users", users.toString().replace("[", "").replace("]", "") + "");
            List<String> allies = new ArrayList<String>();
            for (Guild u : g.getAllies()) {
                allies.add(u.getTag());
            }
            s = s.replace(p + "allies", allies.toString().replace("[", "").replace("]", "") + "");
            s = s.replace(p + "ban-time", Util.getDate(g.getBanTime()) + "");
            s = s.replace(p + "ban-reason", g.getBanReason() + "");
            s = s.replace(p + "tnt", (g.getTnt() - System.currentTimeMillis()) / 1000 + "");
            s = s.replace(p + "cuboid", g.getCuboid() + "");
            s = s.replace(p + "home-x", Math.round(g.getHome().getX()) + "");
            s = s.replace(p + "home-y", Math.round(g.getHome().getY()) + "");
            s = s.replace(p + "home-z", Math.round(g.getHome().getZ()) + "");
            s = s.replace(p + "date", Util.getDate(g.getDate() * 1000));
            niu.add(s);
        }
        return niu;
    }

}
