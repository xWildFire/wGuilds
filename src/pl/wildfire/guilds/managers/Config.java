/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.wildfire.guilds.GuildsPlugin;

public class Config {

    private static HashMap<String, YamlConfiguration> configs = new HashMap<String, YamlConfiguration>();
    private static GuildsPlugin plugin = null;

    public Config(GuildsPlugin gildie) {
        plugin = gildie;
    }

    public static boolean registerConfig(String id) {
        File file = new File(plugin.getDataFolder(), id + ".yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                copy(plugin.getResource(id + ".yml"), file);
                Msg.sendConsole("Stworzono " + id + ".yml!");
            } catch (IOException e) {
                Msg.log(e);
            }
        }

        configs.put(id.toLowerCase(), YamlConfiguration.loadConfiguration(file));
        return true;
    }

    public static YamlConfiguration getConfig(String id) {
        return configs.get(id.toLowerCase());
    }

    public static void load(String id) {
        File file = new File(plugin.getDataFolder(), id + ".yml");
        try {
            configs.get(id.toLowerCase()).load(file);
        } catch (Exception e) {
            Msg.log(e);
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        out.close();
        in.close();
    }

    public static void save(String id) {
        File file = new File(plugin.getDataFolder(), id + ".yml");
        try {
            configs.get(id.toLowerCase()).save(file);
        } catch (Exception e) {
            Msg.log(e);
        }
    }

}
