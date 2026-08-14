package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class VanillaEffectType implements EffectType {
    private final PotionEffectType potionEffectType;

    public VanillaEffectType(PotionEffectType type) {
        this.potionEffectType = type;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    @Override
    public String getId() {
        return potionEffectType.getKey().getKey(); // e.g. "speed"
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(potionEffectType.translationKey());
    }

    @Override
    public Color getColor() {
        return potionEffectType.getColor();
    }

    @Override
    public PotionEffectType.Category getEffectCategory() {
        return potionEffectType.getEffectCategory();
    }

    @Override
    public void applyPotion(PotionMeta meta, List<Component> lore, int amplifier) {
        PotionEffect effect = new PotionEffect(
                potionEffectType,
                20 * 60, // duration in ticks, e.g. 60 seconds — tune as needed
                amplifier,
                false,   // ambient (particles subtler if true)
                true,    // show particles
                true     // show icon
        );
        meta.addCustomEffect(effect, true); // true = overwrite existing effect of same type
    }
}