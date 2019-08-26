/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.wildfire.guilds.GuildsPlugin;
import pl.wildfire.guilds.data.Guild;

public class Price {

    public Price(GuildsPlugin gildie) {

    }

    public static void usunItemyNa(Inventory inv, String na, Guild g) {
        for (String linia : Config.getConfig("config").getStringList("cena-" + na)) {
            String[] splits = linia.split(":");
            if (splits.length != 2) {
                continue;
            }

            try {
                Material material = Material.getMaterial(splits[0]);
                if (material == null) {
                    continue;
                }

                int amount = Integer.parseInt(splits[1]);
                if (amount == 0) {
                    continue;
                }
                if (g != null) {
                    amount *= g.getUsers().size();
                }

                usunItem(inv, material, amount);
            } catch (Exception ex) {
                Msg.sendConsole("&4Blad > &c" + splits[0] + ":" + splits[1]);
            }
        }
    }

    public static boolean maItemyNa(Inventory inv, String na, Guild g) {
        for (String linia : Config.getConfig("config").getStringList("cena-" + na)) {
            String[] splits = linia.split(":");
            if (splits.length != 2) {
                continue;
            }

            try {
                Material material = Material.getMaterial(splits[0]);
                if (material == null) {
                    continue;
                }

                int amount = Integer.parseInt(splits[1]);
                if (amount == 0) {
                    continue;
                }
                if (g != null) {
                    amount *= g.getUsers().size();
                }

                if (!inv.contains(material, amount)) {
                    return false;
                }
            } catch (Exception ex) {
                Msg.sendConsole("&4Blad > &c" + splits[0] + ":" + splits[1]);
            }
        }
        return true;
    }

    public static String itemy(Inventory inv, String na, Guild g) {
        List<String> itemy = new ArrayList<String>();
        for (String linia : Config.getConfig("config").getStringList("cena-" + na)) {
            String[] splits = linia.split(":");
            if (splits.length != 2) {
                continue;
            }

            try {
                Material material = Material.getMaterial(splits[0]);
                if (material == null) {
                    continue;
                }

                int amount = Integer.parseInt(splits[1]);
                if (amount == 0) {
                    continue;
                }
                if (g != null) {
                    amount *= g.getUsers().size();
                }

                int ilema = 0;
                for (ItemStack it : inv.getContents()) {
                    if (it != null) {
                        if (it.getType() == material) {
                            ilema += it.getAmount();
                        }
                    }
                }

                if (inv.contains(material, amount)) {
                    itemy.add("&2" + material + "(" + ilema + "/" + amount + ")");
                } else {
                    itemy.add("&4" + material + "(" + ilema + "/" + amount + ")");
                }
            } catch (Exception ex) {
                Msg.sendConsole("&4Blad > &c" + splits[0] + ":" + splits[1]);
            }
        }
        return itemy.toString().replace("[", "").replace("]", "");
    }

    private static void usunItem(Inventory inv, Material mat, int amount) {
        if (inv.contains(mat)) {
            int remaining = amount;
            ItemStack[] contents = inv.getContents();
            for (ItemStack is : contents) {
                if (is != null && is.getType().equals(mat) && is.getDurability() <= 0) {
                    if (is.getAmount() > remaining) {
                        is.setAmount(is.getAmount() - remaining);
                        remaining = 0;
                    } else if (is.getAmount() <= remaining) {
                        if (remaining > 0) {
                            remaining -= is.getAmount();
                            is.setType(Material.AIR);
                        }
                    }
                }
            }
            inv.setContents(contents);
        }
    }

}
