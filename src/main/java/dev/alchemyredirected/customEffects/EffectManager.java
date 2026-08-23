package dev.alchemyredirected.customEffects;

import dev.alchemyredirected.PersistentData.TagHelper;
import dev.alchemyredirected.Toxicity.ToxicityManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static dev.alchemyredirected.AlchemyRedirected.Print;

public class EffectManager {

    public static int BASE_DURATION = 20*60*3;
    public static int LEVEL_DURATION = 20*30;
    public static int LEVEL_DURATION_UNAFFECTED_BY_LEVELS = 20*60*2;

    static HashMap<UUID,HashMap<EffectType ,EffectValues>> effectPerPlayer = new HashMap<>();

    public static long getDuration(Player player,EffectType effect){
        HashMap<EffectType ,EffectValues> playerEffects = effectPerPlayer.get(player.getUniqueId());
        if(playerEffects == null){return 0;}
        EffectValues values = playerEffects.get(effect);
        if(values == null){return 0;}
        long duration = values.timestamp() - player.getWorld().getFullTime();
        if(duration < 0){
            playerEffects.remove(effect);
            return 0L;
        }
        return duration;
    }
    public static EffectValues getEffect(Player player,EffectType effect){
        HashMap<EffectType ,EffectValues> playerEffects = effectPerPlayer.get(player.getUniqueId());
        if(playerEffects == null){return null;}
        EffectValues values = playerEffects.get(effect);
        if(values == null){return null;}
        long duration = values.timestamp() - player.getWorld().getFullTime();
        if(duration < 0){
            playerEffects.remove(effect);
            return null;
        }
        return values;
    }

    public static void setDuration(Player player,EffectType effect,long duration,int amplifier){
        UUID uuid = player.getUniqueId();
        if(!effectPerPlayer.containsKey(uuid)){
            effectPerPlayer.put(uuid,new HashMap<>());
        }
        HashMap<EffectType ,EffectValues> playerEffects = effectPerPlayer.get(uuid);
        EffectValues values = playerEffects.get(effect);
        long newTimestamp = player.getWorld().getFullTime() + duration;
        if(values == null || values.amplifier() < amplifier || values.timestamp() <  newTimestamp){
            playerEffects.put(effect, new EffectValues(newTimestamp,amplifier));//todo tohle null checking by mohlo bejt lepší
            if(effect instanceof  CustomEffectType customEffectType){
                customEffectType.Start(player,amplifier,duration);
            }
        }
    }
    public static void deleteEffect(Player player,EffectType effect){
        HashMap<EffectType ,EffectValues> playerEffects = effectPerPlayer.get(player.getUniqueId());
        if(playerEffects == null){return;}
        EffectValues values = playerEffects.remove(effect);
        if(effect instanceof  CustomEffectType customEffectType){
            customEffectType.Cleared(player,values.amplifier());
        }

    }
    public static void clense(Player player){
        deleteEffects(player);
        ToxicityManager.reset(player);
    }

    private static void deleteEffects(Player player){
        HashMap<EffectType, EffectValues> playerEffects = effectPerPlayer.remove(player.getUniqueId());
        if(playerEffects == null){return;}
        for(var entry : playerEffects.entrySet()){
            EffectType effect = entry.getKey();
            EffectValues values = entry.getValue();
            if(effect instanceof CustomEffectType customEffectType){
                customEffectType.Cleared(player, values.amplifier());
            }
        }
    }

    public static void drink(Player player, ItemStack item) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        List<CustomEffect<CustomEffectType>> effects = TagHelper.LoadCustomEffects(meta);
        for(CustomEffect<CustomEffectType> effect : effects){
            setDuration(player,effect.effect(),effect.duration(), effect.amplifier());
            Print("drink" + effect.amplifier());
        }
    }

    public static void applyInstant(Player player, ItemStack item)
    {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        List<CustomEffect<InstantEffectType>> effects = TagHelper.LoadCustomEffectsInstant(meta);
        for(CustomEffect<InstantEffectType> effect : effects){
            effect.effect().applyInstant(player, effect.amplifier());
        }
    }
}
