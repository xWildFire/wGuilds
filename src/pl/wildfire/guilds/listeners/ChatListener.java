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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Msg;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        User d = User.get(p.getName());

        String form = "";
        if (d.hasGuild()) {
            Guild g = d.getGuild();

            String tryb = d.getChat() != null ? d.getChat().toLowerCase() : e.getMessage().startsWith("!!") ? "sojusz" : e.getMessage().startsWith("!") ? "gildia" : "";

            if (tryb.equalsIgnoreCase("sojusz") && g.getAllies().size() <= 0) {
                d.setChat("gildia");
                tryb = "gildia";
            }

            if (tryb.equalsIgnoreCase("gildia")) {
                e.setCancelled(true);
                g.sendMessage("&a[" + g.getTag() + "] " + p.getName() + " -> GILDIA:&7 " + e.getMessage());
                return;
            } else if (tryb.equalsIgnoreCase("sojusz")) {
                e.setCancelled(true);

                String s = "&9[" + g.getTag() + "]" + p.getName() + " -> SOJUSZ:&7 " + e.getMessage();
                for (Guild tag : g.getAllies()) {
                    tag.sendMessage(s);
                }
                g.sendMessage(s);
                return;
            }

            if (!p.hasPermission("gildie.notag")) {
                String prefix = "";
                if (d.isLider()) {
                    prefix = "*";
                } else if (d.isMod()) {
                    prefix = "+";
                }
                form = Msg.c("&8[&2" + prefix + g.getTag() + "&8]");
            }
        }
        e.setFormat(e.getFormat().replace("{G}", form));
    }
}
