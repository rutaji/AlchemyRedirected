package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

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
}