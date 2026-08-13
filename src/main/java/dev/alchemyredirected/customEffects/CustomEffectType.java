package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public enum CustomEffectType implements EffectType {
    LIFESTEAL("lifesteal", Component.text("Lifesteal", NamedTextColor.RED),Color.RED,PotionEffectType.Category.BENEFICIAL);

    private final String id;
    private final Component displayName;
    private final Color color;
    private final PotionEffectType.Category category;

    CustomEffectType(String id, Component displayName,Color color,PotionEffectType.Category category) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.category = category;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public PotionEffectType.Category getEffectCategory() {
        return category;
    }
}
