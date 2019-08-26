/**
 * ***********************************
 * Plugin: wGuilds * Autor: WildFire * WildFire§ § 2014-2015 * All Rights
 * Reserved. * ***********************************
 */
package pl.wildfire.guilds.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.wildfire.guilds.data.Guild;
import pl.wildfire.guilds.data.User;

public class DamageListener implements Listener {

    /*   private final Random _random = new Random();
     private final HashMap<Integer, Float> _entityPowerMap = new HashMap<>();

     @EventHandler(priority = EventPriority.HIGHEST)
     public void OnExplosionPrime(ExplosionPrimeEvent event) {
     if (!event.isCancelled()) {
     this._entityPowerMap.put(event.getEntity().getEntityId(), event.getRadius());
     }
     }

     @EventHandler
     public void OnEntityExplode(EntityExplodeEvent event) {
     if ((!event.isCancelled()) && (event.getEntityType() != EntityType.ENDER_DRAGON) && (this._entityPowerMap.containsKey(event.getEntity().getEntityId()))) {
     CorrectExplosion(event, (this._entityPowerMap.get(event.getEntity().getEntityId())));
     this._entityPowerMap.remove(event.getEntity().getEntityId());
     }
     }

     protected void CorrectExplosion(EntityExplodeEvent event, float power) {
     World world = event.getEntity().getWorld();
     event.blockList().clear();

     for (int i = 0; i < 16; i++) {
     for (int j = 0; j < 16; j++) {
     for (int k = 0; k < 16; k++) {
     if ((i == 0) || (i == 15) || (j == 0) || (j == 15) || (k == 0) || (k == 15)) {
     double d3 = i / 15.0F * 2.0F - 1.0F;
     double d4 = j / 15.0F * 2.0F - 1.0F;
     double d5 = k / 15.0F * 2.0F - 1.0F;
     double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

     d3 /= d6;
     d4 /= d6;
     d5 /= d6;
     float f1 = power * (0.7F + this._random.nextFloat() * 0.6F);

     double d0 = event.getLocation().getX();
     double d1 = event.getLocation().getY();
     double d2 = event.getLocation().getZ();

     for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
     int l = MathHelper.floor(d0);
     int i1 = MathHelper.floor(d1);
     int j1 = MathHelper.floor(d2);
     int k1 = world.getBlockTypeIdAt(l, i1, j1);

     if ((k1 > 0) && (k1 != 8) && (k1 != 9) && (k1 != 10) && (k1 != 11)) {
     f1 -= (net.minecraft.server.v1_7_R4.Block.getById(k1).a(((CraftEntity) event.getEntity()).getHandle()) + 0.3F) * f2;
     }

     if ((f1 > 0.0F) && (i1 < 256) && (i1 >= 0) && (k1 != 8) && (k1 != 9) && (k1 != 10) && (k1 != 11)) {
     org.bukkit.block.Block block = world.getBlockAt(l, i1, j1);

     if ((block.getType() != Material.AIR) && (!event.blockList().contains(block))) {
     event.blockList().add(block);
     }
     }

     d0 += d3 * f2;
     d1 += d4 * f2;
     d2 += d5 * f2;
     }
     }
     }
     }
     }
     }
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDm(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player d = (Player) arrow.getShooter();
                Player p = (Player) e.getEntity();
                User dd = User.get(d.getName());
                User pd = User.get(p.getName());

                if (d.getName().equalsIgnoreCase(p.getName())) {
                    e.setCancelled(false);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDmg(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player d = (Player) e.getDamager();
            User dd = User.get(d.getName());
            Guild gd = dd.getGuild();

            if (dd.hasGuild()) {
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    User pd = User.get(p.getName());
                    Guild pg = pd.getGuild();

                    if (pd.hasGuild()) {
                        if (pg.getTag().equalsIgnoreCase(gd.getTag()) || pg.getAllies().contains(gd)) {
                            if (!pd.isFf()) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } else if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player d = (Player) arrow.getShooter();
                Player p = (Player) e.getEntity();
                User dd = User.get(d.getName());
                User pd = User.get(p.getName());

                if (d.getName().equalsIgnoreCase(p.getName())) {
                    e.setCancelled(false);
                    return;
                }
                if (dd.hasGuild() && pd.hasGuild()) {
                    if (dd.getGuild().getTag().equalsIgnoreCase(pd.getGuild().getTag()) || pd.getGuild().getAllies().contains(dd.getGuild())) {
                        if (!pd.isFf()) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
