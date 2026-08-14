package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public interface EffectType {
    String getId();
    Component getDisplayName();
    Color getColor();
    PotionEffectType.Category getEffectCategory();

    void applyPotion(PotionMeta meta, List<Component> lore,int amplifier);
}
