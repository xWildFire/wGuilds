package pl.wildfire.guilds.tasks;

import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Bar;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Variables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckBar extends BukkitRunnable {

    public CheckBar() {

    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            User d = User.get(p.getName());
            Guild g = Guild.get(p.getLocation());
            if (g != null && d.getIncub() == null) {
                Bar.spawnBar(p, Variables.set(Lang.CUB_JOIN, g).get(0));

                d.setIncub(g);
                if (!(p.hasPermission("gildia.admin-tp"))) {
                    if (!d.hasGuild() || d.hasGuild() && !d.getGuild().getTag().equalsIgnoreCase(g.getTag())) {
                        g.sendMsg(Lang.CUB_INTRUDER);
                    }
                }
                continue;
            } else if (g == null && d.getIncub() != null) {
                Bar.spawnBar(p, Variables.set(Lang.CUB_LEAVE, d.getIncub()).get(0));
                d.setIncub(null);
                continue;
            }
            if (Bar.hasBar(p)) {
                Bar.remove(p);
            }
        }
    }

}
