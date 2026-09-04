package dev.alchemyredirected.customEffects;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.helpers.FightUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class CustomEffectsCode {
   public static double LIFESTEAL_PER_LEVEL = 0.1;
   public static float BOMB_POWER_PER_LEVEL = 3f;

    public static void lifesteal(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        EffectValues lifesteal = EffectManager.getEffect(player, CustomEffectType.LIFESTEAL);
        if (lifesteal == null) return;

        double healAmount = event.getFinalDamage() * (lifesteal.amplifier() + 1) * LIFESTEAL_PER_LEVEL;
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        player.setHealth(Math.min(player.getHealth() + healAmount, maxHealth));
    }

    private static HashMap<UUID,BukkitTask> bombTask = new HashMap<>();
    public static void bomb(Player player, int amplifier,long duration) {
        AlchemyRedirected.instance.getLogger().info(amplifier + " bragggg " + BOMB_POWER_PER_LEVEL);
        UUID uuid = player.getUniqueId();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(
                AlchemyRedirected.instance,
                () -> {
                    if(!player.isOnline()){return;}//todo this is temporary, dont store player object
                    Location location = player.getLocation();
                    location.getWorld().createExplosion(location,BOMB_POWER_PER_LEVEL*(amplifier+1));
                },
                duration
        );
        BukkitTask oldTask = bombTask.get(uuid);
        if(oldTask != null){
            oldTask.cancel();
        }
        bombTask.put(uuid,task);
    }
    public static void bombClear(Player player){
        UUID uuid = player.getUniqueId();
        BukkitTask task = bombTask.remove(uuid);
        if(task != null){
            task.cancel();
        }
    }
}
