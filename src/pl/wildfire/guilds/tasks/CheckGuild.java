package pl.wildfire.guilds.tasks;

import pl.wildfire.guilds.cmds.AdminCMD;
import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.managers.Lang;
import pl.wildfire.guilds.managers.Msg;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckGuild extends BukkitRunnable {

    public CheckGuild(Server s, AdminCMD a) {

    }

    @Override
    public void run() {
        Iterator<Entry<String, Guild>> it = Guild.iterator();
        while (it.hasNext()) {
            Guild g = it.next().getValue();

            if (g.getDate() <= ((System.currentTimeMillis()) / 1000)) {
                Msg.sendAll(Lang.GUILD_TIME, g);
                g.getHome().getBlock().getRelative(BlockFace.DOWN).setType(Material.DIRT);
                g.remGuild();
                run();
                return;
            }
        }
    }

}
