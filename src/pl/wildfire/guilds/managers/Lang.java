package pl.wildfire.guilds.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {

    public static List<String> NO_GUILD = Arrays.asList("§cNie mozesz tego zrobic, nie masz gildii.");
    public static List<String> YES_GUILD = Arrays.asList("§cNie mozesz tego zrobic, masz gildie.");
    public static List<String> USER_NO_GUILD = Arrays.asList("§cNie mozesz tego zrobic, gracz nie ma gildii.");
    public static List<String> USER_YES_GUILD = Arrays.asList("§cNie mozesz tego zrobic, gracz ma gildie.");
    public static List<String> JOIN_TIME = Arrays.asList("§4Uwaga: §cCzas waznosci twojej gildii minie $guild_date", "§7Przedluz swoja gildie jak najszybciej: §6/g przedluz");
    public static List<String> CUB_BLOCK = Arrays.asList("§cTeren nalezy do gildii.");
    public static List<String> CUB_INTRUDER = Arrays.asList("§4Uwaga! Na teren twojej gildii wkroczyl intruz.");
    public static List<String> CUB_LEAVE = Arrays.asList("§7Opuszczasz teren gildii §c$guild_name[$guild_tag]");
    public static List<String> CUB_JOIN = Arrays.asList("§7Wkraczasz na teren gildii §c$guild_name[$guild_tag]");
    public static List<String> GUILD_TIME = Arrays.asList("§7Czas waznosci gildii §c$guild_name §7uplynal. Jej stare koordy to X: §6$guild_home-x§7, Z: §6$guild_home-z");
    public static List<String> TP_SUCCESS = Arrays.asList("§2Zostales przeteleportowany do domu gildii.");
    public static List<String> TP_MOVE = Arrays.asList("§cRuszyles sie, teleportacja przerwana.");
    public static List<String> CON_MYSQL = Arrays.asList("Polaczono z MySQL!");
    public static List<String> CON_SQLITE = Arrays.asList("Polaczono z SQLite!");
    public static List<String> CON_ERROR = Arrays.asList("Blad podczas laczenia z baza danych!");

    public Lang() {
        YamlConfiguration c = Config.getConfig("lang");
        Class<?> string = List.class;
        for (Field f : getClass().getFields()) {
            String name = f.getName().toUpperCase();
            String msg = c.getString(name);
            List<String> list = c.getStringList(name);
            try {
                if (list != null && list.size() > 0) {
                    List<String> he = new ArrayList<String>();
                    for (String s : list) {
                        he.add(ChatColor.translateAlternateColorCodes('&', s));
                    }
                    f.set(string, he);
                } else if (msg != null) {
                    f.set(string, Arrays.asList(ChatColor.translateAlternateColorCodes('&', msg)));
                } else {
                    c.set(name, f.get(string));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Msg.log(e);
            }
        }
        Config.save("lang");
    }
}
