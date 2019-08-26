/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;

public class PistonListener implements Listener {

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e) {
        if (e.isCancelled()) {
            return;
        }

        boolean cancel = false;
        for (Block b : e.getBlocks()) {
            if (b.getType().equals(Material.TNT)) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.TNT));
                b.setType(Material.AIR);
                cancel = true;
            }
        }
        e.setCancelled(cancel);
    }

}
