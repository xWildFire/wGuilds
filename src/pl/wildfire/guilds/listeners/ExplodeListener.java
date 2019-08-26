/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import pl.wildfire.guilds.data.Guild;

public class ExplodeListener implements Listener {

    private String getDateTnt(long time) {
        return new SimpleDateFormat("HHmmss").format(new Date(time));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void EntityExplode(EntityExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (Integer.parseInt(getDateTnt(System.currentTimeMillis())) >= 240000 || Integer.parseInt(getDateTnt(System.currentTimeMillis())) <= 050000) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() != null && !e.getEntityType().equals(EntityType.CREEPER)) {
            if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
                Guild g = Guild.get(e.getLocation());
                if (g != null) {
                    g.setTnt(System.currentTimeMillis());
                }
            }
        }
    }
}
