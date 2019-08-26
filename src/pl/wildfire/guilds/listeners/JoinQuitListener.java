/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Variables;

public class JoinQuitListener implements Listener {

    private String getDate(long time) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(time));
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent e) {
        Player p = e.getPlayer();

        User u = User.get(p.getName());
        if (u == null) {
            u = new User(p.getName(), null, false);
        }

        Guild g = u.getGuild();
        if (u.hasGuild() && g != null) {
            if (g.getBanTime() == 999) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§4Twoja gildia jest permanentnie zbanowana.\nPowod: " + g.getBanReason());
            } else if (g.getBanTime() > System.currentTimeMillis()) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§4Twoja gildia jest zbanowana do " + getDate(g.getBanTime()) + ".\nPowod: " + g.getBanReason());
            } else {
                if (g.getBanTime() != 0) {
                    g.remBan();
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User pd = User.get(p.getName());
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(sb);

        if (pd.hasGuild()) {
            long d = pd.getGuild().getDate();
            long t = System.currentTimeMillis() / 1000;
            if (d - 259200 <= t) {
                pd.sendMsg(Variables.set(Lang.JOIN_TIME, pd.getGuild()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        User pd = User.get(p.getName());

        if (pd.isTp()) {
            pd.setTp(null);
        }
    }
}
