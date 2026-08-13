package dev.alchemyredirected.customEffects;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class EffectManager {
    public static String Lifesteal = "Lifesteal";

    static HashMap<UUID,HashMap<String ,Long>> effectPerPlayer = new HashMap<>();

    public static long getDuration(Player player,String effect){
        HashMap<String,Long> playerEffects = effectPerPlayer.get(player.getUniqueId());
        if(playerEffects == null){return 0;}
        long timestamp = playerEffects.getOrDefault(effect,-1L);
        if(timestamp == -1){return 0;}
        long duration = timestamp - player.getWorld().getFullTime();
        if(duration < 0){
            playerEffects.remove(effect);
            return 0L;
        }
        return duration;
    }

    public static void setDuration(Player player,String effect,long duration){
        UUID uuid = player.getUniqueId();
        if(!effectPerPlayer.containsKey(uuid)){
            effectPerPlayer.put(uuid,new HashMap<>());
        }
        HashMap<String,Long> playerEffects = effectPerPlayer.get(uuid);
        long timestamp = playerEffects.getOrDefault(effect,-1L);
        long newTimestamp = player.getWorld().getFullTime() + duration;
        if(newTimestamp > timestamp){playerEffects.put(effect,newTimestamp);}

    }
    public static void deleteEffects(Player player,String effect){
        HashMap<String,Long> playerEffects = effectPerPlayer.get(player.getUniqueId());
        if(playerEffects == null){return;}
        playerEffects.remove(effect);
    }

}
