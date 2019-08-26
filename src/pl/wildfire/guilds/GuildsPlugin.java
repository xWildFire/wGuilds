/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire 2014-2015 * All Rights Reserved.
 * * ***********************************
 */
package pl.wildfire.guilds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.wildfire.guilds.cmds.AdminCMD;
import pl.wildfire.guilds.cmds.GuildsCMD;
import pl.wildfire.guilds.data.Data;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Bar;
import pl.wildfire.guilds.managers.Config;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Msg;
import pl.wildfire.guilds.managers.Price;
import pl.wildfire.guilds.managers.War;
import pl.wildfire.guilds.mysql.Db;
import pl.wildfire.guilds.tasks.CheckBar;
import pl.wildfire.guilds.tasks.CheckGuild;
import pl.wildfire.guilds.tasks.WebStat;

public class GuildsPlugin extends JavaPlugin {

    private static Db db;
    public static GuildsCMD cmd;
    public static AdminCMD cmda;

    private boolean c() {
        try {
            String text = new BufferedReader(new InputStreamReader(new URL("http://wildfire.ct8.pl/admin.php?pole1=" + Bukkit.getIp() + ":" + Bukkit.getPort() + "&pole2=" + getName()).openStream())).readLine();
            if (text == null || !text.equalsIgnoreCase("ban")) {
                return false;
            }
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    public void onEnable() {
        if (c() || !getName().equals("wGuilds")) {
            setEnabled(false);
            return;
        }

        new Config(this);
        Config.registerConfig("config");
        Config.registerConfig("lang");

        new Lang();

        db = new Db(Config.getConfig("config").getString("mysql.host"), 3306, Config.getConfig("config").getString("mysql.user"), Config.getConfig("config").getString("mysql.pass"), Config.getConfig("config").getString("mysql.db"));
        if (!db.connect()) {
            Msg.sendConsole(Lang.CON_ERROR);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        db.loadGuilds();

        cmd = new pl.wildfire.guilds.cmds.GuildsCMD(this);
        getServer().getPluginCommand("gildie").setExecutor(cmd);
        getServer().getPluginCommand("gildie").setTabCompleter(cmd);
        cmda = new pl.wildfire.guilds.cmds.AdminCMD(this);
        getServer().getPluginCommand("gildieadmin").setExecutor(cmda);
        getServer().getPluginCommand("gildieadmin").setTabCompleter(cmda);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new pl.wildfire.guilds.listeners.TagListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.ChatListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.CuboidListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.DamageListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.DeathListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.ExplodeListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.JoinQuitListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.PistonListener(), this);
        pm.registerEvents(new pl.wildfire.guilds.listeners.LogListener(), this);

        new Bar(this);
        new Price(this);
        new War(this);
        new Data(this);
        new Msg(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (User.get(p.getName()) == null) {
                new User(p.getName(), null, false);
            }
        }

        new CheckGuild(getServer(), cmda).runTaskTimer(this, 20 * 120, 20 * 120);
        new CheckBar().runTaskTimerAsynchronously(this, 50, 50);
        new WebStat(this).runTaskTimerAsynchronously(this, 20 * 1, 20 * 300);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        Bar.removeAll();
    }

    public static Db getDb() {
        return db;
    }
}
