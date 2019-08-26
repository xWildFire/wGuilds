/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.managers;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.wildfire.guilds.GuildsPlugin;

public class Bar {

    private static HashMap<String, Object> players = new HashMap<String, Object>();

    public Bar(GuildsPlugin gildie) {

    }

    public static void removeAll() {
        for (String p : players.keySet()) {
            Player b = Bukkit.getPlayer(p);
            if (b != null) {
                remove(b);
            }
        }
        players.clear();
    }

    public static void remove(Player p) {
        try {
            int id = (Integer) players.get(p.getName()).getClass().getMethod("getId").invoke(players.get(p.getName()), new Object[0]);
            Object packet = getClass("{net}:PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[]{id});
            send(p, packet);
            players.remove(p.getName());
        } catch (Exception e) {
            Msg.log(e);
        }
    }

    public static Object spawnBar(Player p, String text) {
        if (!players.containsKey(p.getName())) {
            try {
                Object ender = getClass("{net}:EntityEnderDragon").getConstructor(getClass("{net}:World")).newInstance(getClass("{org}:CraftWorld").getMethod("getHandle").invoke(p.getWorld()));
                ender.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(ender, p.getLocation().getX(), -500, p.getLocation().getZ(), 0F, 0F);
                ender.getClass().getMethod("setCustomName", String.class).invoke(ender, text);
                ender.getClass().getMethod("setCustomNameVisible", boolean.class).invoke(ender, false);
                ender.getClass().getMethod("setHealth", float.class).invoke(ender, 200F + 0.1F);
                Object packet = getClass("{net}:PacketPlayOutSpawnEntityLiving").getConstructor(getClass("{net}:EntityLiving")).newInstance(ender);
                send(p, packet);
                players.put(p.getName(), ender);
                return packet;
            } catch (Exception e) {
                Msg.log(e);
            }
        } else {
            return changeBar(p, text);
        }
        return null;
    }

    private static Object changeBar(Player p, String text) {
        try {
            Object ender = players.get(p.getName());
            int id = (Integer) ender.getClass().getMethod("getId").invoke(ender, new Object[0]);
            Object watcher = getClass("{net}:DataWatcher").getConstructor(getClass("{net}:Entity")).newInstance(ender);
            Method a = watcher.getClass().getMethod("a", int.class, Object.class);
            a.invoke(watcher, 0, (byte) 0x20);
            a.invoke(watcher, 6, (float) 200.0 + 0.1F);
            a.invoke(watcher, 8, (byte) 0);
            a.invoke(watcher, 10, text);
            a.invoke(watcher, 11, (byte) 0);
            Object packet = getClass("{net}:PacketPlayOutEntityMetadata").getConstructor(int.class, getClass("{net}:DataWatcher"), boolean.class).newInstance(id, watcher, true);
            send(p, packet);
            players.put(p.getName(), ender);
            return packet;
        } catch (Exception e) {
            Msg.log(e);
        }
        return null;
    }

    private static void send(Player p, Object packet) {
        try {
            Object craftplayer = getClass("{org}:entity.CraftPlayer").getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            Object connection = getClass("{net}:EntityPlayer").getField("playerConnection").get(craftplayer);
            Method sendpacket = getClass("{net}:PlayerConnection").getMethod("sendPacket", new Class[]{getClass("{net}:Packet")});
            sendpacket.invoke(connection, packet);
        } catch (Exception e) {
            Msg.log(e);
        }
    }

    private static Class<?> getClass(String name) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String className = null;
        if (name.split("\\:")[0].equals("{net}")) {
            className = "net.minecraft.server." + version + name.split(":")[1];
        }
        if (name.split("\\:")[0].equals("{org}")) {
            className = "org.bukkit.craftbukkit." + version + name.split(":")[1];
        }
        return Class.forName(className);
    }

    public static boolean hasBar(Player p) {
        return players.containsKey(p.getName());
    }

}
