package pl.wildfire.guilds.tasks;

import pl.wildfire.guilds.GuildsPlugin;
import pl.wildfire.guilds.data.Data;
import pl.wildfire.guilds.data.Guild;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.scheduler.BukkitRunnable;

public class WebStat extends BukkitRunnable {

    private GuildsPlugin plugin;

    public WebStat(GuildsPlugin g) {
        plugin = g;
    }

    @Override
    public void run() {
        clear();
        int i = 0;
        for (Guild g : Data.getTopPoints()) {
            if (g != null) {
                if (i >= 5) {
                    return;
                }
                String s = "$startTAG NAZWA PKT KILL DEATH LIDER LGRACZY CUBxCUB$end";
                s = s.replaceAll("TAG", g.getTag()).replaceAll("NAZWA", g.getName());
                s = s.replaceAll("PKT", g.getPoints() + "").replaceAll("KILL", g.getKills() + "");
                s = s.replaceAll("DEATH", g.getDeaths() + "").replaceAll("LIDER", g.getLider().getName() + "");
                s = s.replaceAll("LGRACZY", g.getUsers().size() + "").replaceAll("CUB", g.getCuboid() + "");
                log(s);
                i++;
            }
        }
    }

    private void clear() {
        File f = new File(plugin.getDataFolder(), "top.txt");
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            PrintWriter writer = new PrintWriter(f);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    private void log(String s) {
        File f = new File(plugin.getDataFolder(), "top.txt");
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter(f, true));
            BufferedReader b = new BufferedReader(new FileReader(f));
            if (b != null) {
                String l = b.readLine();
                if (l != null && l.length() > 0) {
                    write.newLine();
                }
            }

            write.write(s);

            write.flush();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
