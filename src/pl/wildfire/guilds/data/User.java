/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class User {

    private String name;
    private Guild guild;
    private String chat;
    private boolean ff;
    private BukkitTask tp;
    private List<Guild> inv;
    private Guild incub;
    private Scoreboard sb;

    public User(String n, Guild g, boolean f) {
        name = n;
        guild = g;
        tp = null;
        ff = f;
        inv = new ArrayList<Guild>();
        incub = null;

        Player p = Bukkit.getPlayer(n);
        if (p != null) {
            Score xd = null;
            if (p.getScoreboard().getObjective("pkt") != null) {
                xd = p.getScoreboard().getObjective("pkt").getScore(p);
            }

            if (xd != null) {
                Score s = p.getScoreboard().getObjective("pkt").getScore(p);
                s.setScore(xd.getScore());
            }
        }
        users.put(name.toUpperCase(), this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }

    public boolean sendMsg(List<String> list) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            for (String s : list) {
                p.sendMessage(s);
            }
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean hasGuild() {
        if (guild != null && guild.getUsers().contains(this)) {
            return true;
        }
        guild = null;
        return false;
    }

    public Guild getGuild() {
        if (guild != null && guild.getUsers().contains(this)) {
            return guild;
        }
        guild = null;
        return null;
    }

    public void setGuild(Guild g, boolean b) {
        guild = g;
        chat = null;
    }

    public Scoreboard getSb() {
        return sb;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String c) {
        chat = c;
    }

    public boolean isFf() {
        return ff;
    }

    public void setFf(boolean f) {
        ff = f;
    }

    public boolean isTp() {
        return tp != null;
    }

    public void setTp(BukkitTask t) {
        tp = t;
    }

    public List<Guild> getInv() {
        return inv;
    }

    public void addInv(Guild g) {
        inv.add(g);
    }

    public void remInv(Guild g) {
        inv.remove(g);
    }

    public Guild getIncub() {
        if (incub == null) {
            return null;
        }
        return incub;
    }

    public void setIncub(Guild in) {
        incub = in;
    }

    public boolean isLiderOrMod() {
        if (isLider() || isMod()) {
            return true;
        }
        return false;
    }

    public boolean isLider() {
        if (guild != null && guild.getLider().equals(this)) {
            return true;
        }
        return false;
    }

    public boolean isMod() {
        if (guild != null && guild.getMods().contains(this)) {
            return true;
        }
        return false;
    }

    private static HashMap<String, User> users = new HashMap<String, User>();

    public static User get(String t) {
        t = t.toUpperCase();

        if (users.containsKey(t)) {
            return users.get(t);
        }
        return null;
    }
}
