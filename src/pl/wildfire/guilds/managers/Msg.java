/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.managers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.wildfire.guilds.data.Guild;

import java.util.ArrayList;
import java.util.List;
import pl.wildfire.guilds.GuildsPlugin;

public class Msg {

    public static GuildsPlugin plugin;

    public Msg(GuildsPlugin pg) {
        plugin = pg;
    }

    public static boolean sendAll(CommandSender p, String key, String... var) {
        List<String> esc = new ArrayList<String>();
        if ((Config.getConfig("lang").getStringList(key) != null) && (Config.getConfig("lang").getStringList(key).size() > 0)) {
            esc = Config.getConfig("lang").getStringList(key);
        } else if (Config.getConfig("lang").getString(key) != null) {
            esc.add(Config.getConfig("lang").getString(key));
        }
        if (esc.size() > 0) {
            for (String s : esc) {
                String c = "";
                if (s != null) {
                    c = s;
                }
                for (int i = 0; i < var.length; i++) {
                    if (var[i].contains("=")) {
                        String[] spl = var[i].split("=");
                        c = c.replace(spl[0], spl[1]);
                    }
                }
                Bukkit.broadcastMessage(c(c));
            }
        }
        return true;
    }

    public static boolean sendMsg(CommandSender p, String key, String... var) {
        List<String> esc = new ArrayList<String>();
        if ((Config.getConfig("lang").getStringList(key) != null) && (Config.getConfig("lang").getStringList(key).size() > 0)) {
            esc = Config.getConfig("lang").getStringList(key);
        } else if (Config.getConfig("lang").getString(key) != null) {
            esc.add(Config.getConfig("lang").getString(key));
        }
        if (esc.size() > 0) {
            for (String s : esc) {
                String c = "";
                if (s != null) {
                    c = s;
                }
                for (int i = 0; i < var.length; i++) {
                    if (var[i].contains("=")) {
                        String[] spl = var[i].split("=");
                        c = c.replace(spl[0], spl[1]);
                    }
                }
                p.sendMessage(c(c));
            }
        }
        return true;
    }

    public static boolean sendGuild(Guild p, String key, String... var) {
        List<String> esc = new ArrayList<String>();
        if ((Config.getConfig("lang").getStringList(key) != null) && (Config.getConfig("lang").getStringList(key).size() > 0)) {
            esc = Config.getConfig("lang").getStringList(key);
        } else if (Config.getConfig("lang").getString(key) != null) {
            esc.add(Config.getConfig("lang").getString(key));
        }
        if (esc.size() > 0) {
            for (String s : esc) {
                String c = "";
                if (s != null) {
                    c = s;
                }
                for (int i = 0; i < var.length; i++) {
                    if (var[i].contains("=")) {
                        String[] spl = var[i].split("=");
                        c = c.replace(spl[0], spl[1]);
                    }
                }
                p.sendMessage(c(c));
            }
        }
        return true;
    }

    public static void log(Exception e) {
        sendConsole("<====================> ERROR <====================>");
        sendConsole("Error: " + e.getMessage().toString());
        e.printStackTrace();
        sendConsole("<====================> ERROR <====================>");
    }

    public static void sendConsole(List<String> l) {
        for (String s : l) {
            Bukkit.getConsoleSender().sendMessage(c("[wGuilds] " + s));
        }
    }

    public static void sendConsole(String s) {
        Bukkit.getConsoleSender().sendMessage(c("[wGuilds] " + s));
    }

    public static void sendAll(String s) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MESSAGE");
        out.writeUTF("ALL");
        out.writeUTF(s);
        Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public static void sendAll(List<String> l, Guild g) {
        l = g != null ? Variables.set(l, g) : l;
        for (String s : l) {
            Bukkit.broadcastMessage(s);
        }
    }

    public static void sendPlayer(Player p, String wiadomosc) {
        p.sendMessage(c(wiadomosc));
    }

    public static boolean sendSender(CommandSender sender, String wiadomosc) {
        sender.sendMessage(c(wiadomosc));
        return true;
    }

    public static String c(String wiadomosc) {
        return ChatColor.translateAlternateColorCodes('&', wiadomosc);
    }

    public static void sendPlayer(Player p, List<String> c) {
        for (String s : c) {
            p.sendMessage(s);
        }
    }
}
