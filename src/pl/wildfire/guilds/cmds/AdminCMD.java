/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire 2014-2015 * All Rights Reserved.
 * * ***********************************
 */
package pl.wildfire.guilds.cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.wildfire.guilds.GuildsPlugin;
import pl.wildfire.guilds.cmds.admin.AddACMD;
import pl.wildfire.guilds.cmds.admin.BanACMD;
import pl.wildfire.guilds.cmds.admin.DeleteACMD;
import pl.wildfire.guilds.cmds.admin.InfoACMD;
import pl.wildfire.guilds.cmds.admin.KickACMD;
import pl.wildfire.guilds.cmds.admin.LiderACMD;
import pl.wildfire.guilds.cmds.admin.LoadACMD;
import pl.wildfire.guilds.cmds.admin.ReloadACMD;
import pl.wildfire.guilds.cmds.admin.TempBanACMD;
import pl.wildfire.guilds.cmds.admin.TpACMD;
import pl.wildfire.guilds.cmds.admin.UnBanACMD;
import pl.wildfire.guilds.data.Data;
import pl.wildfire.guilds.managers.Config;
import pl.wildfire.guilds.managers.Msg;

public class AdminCMD implements CommandExecutor, TabCompleter {

    private List<SubCommand> l = new ArrayList<SubCommand>();

    public AdminCMD(GuildsPlugin plug) {
        YamlConfiguration cfg = Config.getConfig("lang");

        l.add(new AddACMD("guilds.admin.add", s(cfg.getString("cmd-admin.add"))));
        l.add(new BanACMD("guilds.admin.ban", s(cfg.getString("cmd-admin.ban"))));
        l.add(new DeleteACMD("guilds.admin.delete", s(cfg.getString("cmd-admin.delete"))));
        l.add(new InfoACMD("guilds.admin.info", s(cfg.getString("cmd-admin.info"))));
        l.add(new KickACMD("guilds.admin.kick", s(cfg.getString("cmd-admin.kick"))));
        l.add(new LiderACMD("guilds.admin.lider", s(cfg.getString("cmd-admin.lider"))));
        l.add(new LoadACMD("guilds.admin.load", s(cfg.getString("cmd-admin.load"))));
        l.add(new ReloadACMD("guilds.admin.reload", s(cfg.getString("cmd-admin.reload")), plug));
        l.add(new TempBanACMD("guilds.admin.tempban", s(cfg.getString("cmd-admin.tempban"))));
        l.add(new TpACMD("guilds.admin.tp", s(cfg.getString("cmd-admin.tp"))));
        l.add(new UnBanACMD("guilds.admin.unban", s(cfg.getString("cmd-admin.unban"))));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if (args.length > 0) {
            SubCommand sc = getSub(args[0]);
            // Arrays.copyOfRange(args, 1, args.length)
            if (sc != null) {
                return sc.execute(sender, args);
            } else if (args[0].equalsIgnoreCase("teamclear")) {
                Data.getSb().getTeams().clear();
            }
        }

        if (!sender.hasPermission("guilds.admin.info-cmd")) {
            return Msg.sendMsg(sender, "other.no-perms");
        }

        return Msg.sendMsg(sender, "admin.usage");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            ArrayList<String> t = new ArrayList<String>();

            for (SubCommand c : l) {
                for (String s : c.getAlias()) {
                    if (s.startsWith(args[0]) && sender.hasPermission(c.getPermission())) {
                        t.add(s);
                    }
                }
            }

            Collections.sort(t);

            return t;
        }
        return null;
    }

    public boolean exe(String s, CommandSender se, String[] args) {
        if (getSub(s) != null) {
            return getSub(s).execute(se, args);
        }
        return false;
    }

    private SubCommand getSub(String p) {
        for (SubCommand s : l) {
            if (s.getAlias().contains(p.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    private List<String> s(String s) {
        List<String> l = new ArrayList<String>();
        if (s != null) {
            if (s.contains(",")) {
                for (String b : s.split(",")) {
                    l.add(b.toLowerCase().replace(" ", ""));
                }
            } else {
                l.add(s.toLowerCase());
            }
        }
        return l;
    }

    public void load(GuildsPlugin plugin) {
        l.clear();
        YamlConfiguration cfg = Config.getConfig("lang");

        l.add(new AddACMD("guilds.admin.add", s(cfg.getString("cmd-admin.add"))));
        l.add(new BanACMD("guilds.admin.ban", s(cfg.getString("cmd-admin.ban"))));
        l.add(new DeleteACMD("guilds.admin.delete", s(cfg.getString("cmd-admin.delete"))));
        l.add(new InfoACMD("guilds.admin.info", s(cfg.getString("cmd-admin.info"))));
        l.add(new KickACMD("guilds.admin.kick", s(cfg.getString("cmd-admin.kick"))));
        l.add(new LiderACMD("guilds.admin.lider", s(cfg.getString("cmd-admin.lider"))));
        l.add(new LoadACMD("guilds.admin.load", s(cfg.getString("cmd-admin.load"))));
        l.add(new ReloadACMD("guilds.admin.reload", s(cfg.getString("cmd-admin.reload")), plugin));
        l.add(new TempBanACMD("guilds.admin.tempban", s(cfg.getString("cmd-admin.tempban"))));
        l.add(new TpACMD("guilds.admin.tp", s(cfg.getString("cmd-admin.tp"))));
        l.add(new UnBanACMD("guilds.admin.unban", s(cfg.getString("cmd-admin.unban"))));
    }
}
