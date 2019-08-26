package pl.wildfire.guilds.tasks;

import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeTP extends BukkitRunnable {

    private Location from;
    private User user;

    public HomeTP(Location f, User u) {
        from = f;
        user = u;
    }

    @Override
    public void run() {
        if (from != null) {
            Player p = Bukkit.getPlayer(user.getName());
            if (p != null && user.hasGuild() && p.getLocation().distance(from) <= 0.5) {
                p.teleport(user.getGuild().getHome());
                user.sendMsg(Lang.TP_SUCCESS);
            } else {
                user.sendMsg(Lang.TP_MOVE);
            }
        }
        user.setTp(null);
    }

}
