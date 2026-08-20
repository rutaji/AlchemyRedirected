package dev.alchemyredirected.papi;

import dev.alchemyredirected.Toxicity.ToxicityManager;
import dev.alchemyredirected.customEffects.CustomEffectType;
import dev.alchemyredirected.customEffects.EffectManager;
import dev.alchemyredirected.helpers.TextUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static dev.alchemyredirected.AlchemyRedirected.Print;

public class AlchemyPlaceholders extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "alchemyredirected"; // becomes %alchemyredirected_...%
    }

    @Override
    public String getAuthor() {
        return "rutaji";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist() {
        return true; // keep loaded across /papi reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NonNull String params) {
        if (player == null) return "";
        Print(params);
        if (params.equals("LIFESTEAL_duration")) {
            String s = TextUtil.ticksToTime((int) EffectManager.getDuration(player, CustomEffectType.LIFESTEAL));
            Print(s);
            return s;
        }
        if(params.equals("BOMB_duration")){
            String s = TextUtil.ticksToTime((int) EffectManager.getDuration(player, CustomEffectType.BOMB));
            Print(s);
            return s;
        }
        if(params.equals("toxicity")){
            return String.valueOf(ToxicityManager.get(player));
        }

        return null; // unknown placeholder
    }
}
