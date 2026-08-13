package dev.alchemyredirected.Toxicity;

import dev.alchemyredirected.AlchemyRedirected;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ToxicityManager {


    public static HashMap<UUID,Integer> ToxicityPerPlayer = new HashMap<>();

    public static final Set<UUID> toxicityDeaths = new HashSet<>();

    public static void add(Player player, Integer integer) {
        AlchemyRedirected.instance.getLogger().info(integer + "bruhhhhg");
        UUID uuid = player.getUniqueId();
        int  newToxic = ToxicityPerPlayer.getOrDefault(uuid,0) + integer;
        ToxicityPerPlayer.put(uuid,newToxic);
        effect(player,newToxic);
    }
    public static void effect(Player player,int amount){
        if(amount > 100){
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*60*3,2));
        }
        if(amount > 200){
            toxicityDeaths.add(player.getUniqueId());
            player.kill();
        }
    }

    public static void reset(Player player) {
        ToxicityPerPlayer.put(player.getUniqueId(),0);
    }
}
