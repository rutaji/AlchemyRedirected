package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public interface EffectType {
    String getId();
    Component getDisplayName();
    Color getColor();
    PotionEffectType.Category getEffectCategory();
}
