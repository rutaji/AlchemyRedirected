package dev.alchemyredirected.papi;

import dev.alchemyredirected.customEffects.EffectManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

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

        if (params.equals("lifesteal_duration")) {
            return  String.valueOf(EffectManager.getDuration(player,EffectManager.Lifesteal));
        }

        return null; // unknown placeholder
    }
}
