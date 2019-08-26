/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Msg;

import java.util.ArrayList;
import java.util.List;

public class CuboidListener implements Listener {

    private List<String> list = new ArrayList<String>();

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (list.contains(p.getName())) {
            list.remove(p.getName());
        }
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (list.contains(p.getName())) {
            list.remove(p.getName());
            return;
        }
        if (p.hasPermission("gildia.admin-tp")) {
            return;
        }
        if (e.getCause().equals(TeleportCause.ENDER_PEARL)) {
            return;
        }

        User u = User.get(p.getName());
        Guild g = Guild.get(e.getTo());
        Guild from = Guild.get(e.getFrom());
        if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
            Msg.sendMsg(p, "§cNie mozesz teleportowac sie na teren gildii.");
            e.setCancelled(true);
        } else if (from != null && !u.hasGuild() || from != null && !from.equals(u.getGuild())) {
            Msg.sendMsg(p, "§cNie mozesz wyteleportowac sie z terenu gildii.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (p.hasPermission("gildia.admin-block")) {
            return;
        }

        User u = User.get(p.getName());
        Guild g = Guild.get(e.getBlockClicked().getLocation());
        if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
            Msg.sendPlayer(p, Lang.CUB_BLOCK);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerCommandPreprocessEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getMessage().startsWith("/crafting") || e.getMessage().startsWith("/workbench") || e.getMessage().startsWith("/enderchest")) {
            if (p.hasPermission("gildia.admin-cmd")) {
                return;
            }

            User u = User.get(p.getName());
            Guild g = Guild.get(p.getLocation());
            if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (p.hasPermission("gildia.admin-block")) {
            return;
        }

        User u = User.get(p.getName());
        Guild g = Guild.get(e.getBlockClicked().getLocation());
        if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
            Msg.sendPlayer(p, Lang.CUB_BLOCK);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        User u = User.get(p.getName());
        Guild g = Guild.get(e.getTo());
        if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
            Msg.sendMsg(p, "§cPortal nalezy do gildii nie mozesz przez niego przejsc.");
            e.setCancelled(true);
        } else {
            g = Guild.get(e.getFrom());
            if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
                list.add(p.getName());
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (p.hasPermission("gildia.admin-block")) {
            return;
        }

        User u = User.get(p.getName());
        Guild g = Guild.get(e.getBlockPlaced().getLocation());
        if (g != null && !u.hasGuild() || g != null && !g.equals(u.getGuild())) {
            Msg.sendPlayer(p, Lang.CUB_BLOCK);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (p.hasPermission("gildia.admin-block")) {
            return;
        }

        User u = User.get(p.getName());
        Guild g = Guild.get(e.getBlock().getLocation());
        if (g != null) {
            if (!u.hasGuild() || !g.equals(u.getGuild())) {
                Msg.sendPlayer(p, Lang.CUB_BLOCK);
                e.setCancelled(true);
            }
        }
    }
}
