package dev.alchemyredirected.Toxicity;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static dev.alchemyredirected.recipie.RecipeManager.MAXPOTIONTOXICITY;
import static dev.alchemyredirected.recipie.RecipeManager.MAXTOXICITY;

public class ToxicityManager {


    public static HashMap<UUID,ToxicityTimestamp> ToxicityPerPlayer = new HashMap<>();

    public static final double LOSTTOXICITYPERSECOND = 0.002;

    public static final Set<UUID> toxicityDeaths = new HashSet<>();

    public static void add(Player player, Integer integer) {
        UUID uuid = player.getUniqueId();
        int currentToxicity = get(player) + integer;
        ToxicityTimestamp newToxic = new ToxicityTimestamp(player.getWorld().getGameTime(),currentToxicity) ;
        ToxicityPerPlayer.put(uuid,newToxic);
        ontoxicityIncreased(player,currentToxicity);
    }

    public static void reset(Player player) {
        ToxicityPerPlayer.remove(player.getUniqueId());
    }
    public static int get(Player player){
        UUID uuid = player.getUniqueId();
        if(!ToxicityPerPlayer.containsKey(uuid)){return 0;}
        ToxicityTimestamp toxicityTimestamp = ToxicityPerPlayer.get(uuid);
        long since =  player.getWorld().getGameTime() - toxicityTimestamp.timestamp();
        return (int)Math.ceil(Math.max(toxicityTimestamp.toxicity() - since* LOSTTOXICITYPERSECOND,0));

    }

    public static void ontoxicityIncreased(Player player,int amount){
        if(amount > MAXPOTIONTOXICITY){
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*60*3,2));
        }
        if(amount >= MAXTOXICITY){
            toxicityDeaths.add(player.getUniqueId());
            player.kill();
        }
    }
}
