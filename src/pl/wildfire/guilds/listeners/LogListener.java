package pl.wildfire.guilds.listeners;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.wildfire.guilds.data.User;

public class LogListener implements Listener {

    @EventHandler
    public void onLog(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        User u = User.get(p.getName());

        if (p.getWorld().getName().equalsIgnoreCase("world") && u.hasGuild() && u.isLider()) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && e.getItem().getType().equals(Material.WOOD_PICKAXE)) {
                if (Bukkit.getPluginManager().isPluginEnabled("LogBlock")) {
                    LogBlock logblock = (LogBlock) Bukkit.getPluginManager().getPlugin("LogBlock");
                    QueryParams params = new QueryParams(logblock);
                    params.bct = BlockChangeType.ALL;
                    params.loc = e.getClickedBlock().getLocation();
                    params.world = p.getWorld();
                    params.needDate = true;
                    params.needType = true;
                    params.needPlayer = true;
                    params.radius = 1;
                    params.limit = 100;
                    params.needChestAccess = true;
                    params.needMessage = true;

                    try {
                        Location loc = e.getClickedBlock().getLocation();
                        p.sendMessage("§3Zmiany w bloku na: " + Math.round(loc.getX()) + "," + Math.round(loc.getY()) + "," + Math.round(loc.getZ()));
                        int i = 0;
                        for (BlockChange bc : logblock.getBlockChanges(params)) {
                            p.sendMessage("§6" + bc.toString());
                            i++;
                        }
                        if (i <= 0) {
                            p.sendMessage("§cBrak wynikow.");
                        }
                    } catch (SQLException ex) {

                    }
                }
            }
        }
    }

}
