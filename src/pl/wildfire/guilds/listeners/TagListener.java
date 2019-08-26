package pl.wildfire.guilds.listeners;

import pl.wildfire.guilds.data.User;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

public class TagListener implements Listener {

    @EventHandler
    public void onTag(AsyncPlayerReceiveNameTagEvent e) {
        Player n = e.getNamedPlayer();
        Player p = e.getPlayer();
        User un = User.get(n.getName());
        User up = User.get(p.getName());

        String tag = null;

        if (!up.hasGuild() && un.hasGuild()) {
            tag = "§c[" + un.getGuild().getTag() + "] ";
        } else if (up.hasGuild() && un.hasGuild()) {
            tag = up.getGuild().getColor(un.getGuild()) + "[" + un.getGuild().getTag() + "] ";
        }

        if (tag != null) {
            Scoreboard sb = p.getScoreboard();
            String c = n.getName().toLowerCase();

            Team t = sb.getTeam(c);
            if (t == null) {
                t = sb.registerNewTeam(c);
            }
            t.addPlayer(n);
            t.setPrefix(tag);
        }
    }

}
