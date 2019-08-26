package pl.wildfire.guilds.cmds;

import pl.wildfire.guilds.managers.Msg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommand {

    private String permission = "";
    private List<String> alias = new ArrayList<>();

    public SubCommand(List<String> a) {
        alias = a;
    }

    public SubCommand(String p, List<String> a) {
        permission = p;
        alias = a;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!permission.equalsIgnoreCase("") && !sender.hasPermission(permission)) {
            return Msg.sendMsg(sender, "other.no-perms");
        }
        return onCommand(sender instanceof Player ? ((Player) sender) : sender, args);
    }

    public boolean onCommand(CommandSender sender, String[] args) {
        return Msg.sendMsg(sender, "other.no-console");
    }

    public boolean onCommand(Player sender, String[] args) {
        return false;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAlias() {
        return alias;
    }
}
