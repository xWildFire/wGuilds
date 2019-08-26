/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import pl.wildfire.guilds.data.User;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        User pd = User.get(p.getName());

        if (pd.hasGuild()) {
            pd.getGuild().addDeath(pd);
        }

        if (p.getKiller() instanceof Player) {
            User dd = User.get(p.getKiller().getName());

            if (dd.hasGuild()) {
                dd.getGuild().addKill(dd);
            }
        }
    }
}
