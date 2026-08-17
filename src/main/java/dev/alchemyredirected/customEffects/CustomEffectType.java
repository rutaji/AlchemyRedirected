package dev.alchemyredirected.customEffects;

import dev.alchemyredirected.PersistentData.TagHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Optional;

import static dev.alchemyredirected.customEffects.EffectManager.*;
import static dev.alchemyredirected.helpers.TextUtil.*;

public enum CustomEffectType implements EffectType {
    LIFESTEAL("LIFESTEAL", Component.text("Lifesteal", NamedTextColor.RED),Color.RED,PotionEffectType.Category.BENEFICIAL,true);

    private final String id;
    private final Component displayName;
    private final Color color;
    private final PotionEffectType.Category category;
    private final boolean AffectedByLevels;

    CustomEffectType(String id, Component displayName,Color color,PotionEffectType.Category category,boolean affectedByLevels) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.category = category;
        this.AffectedByLevels = affectedByLevels;
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

    @Override
    public void applyPotion(PotionMeta meta, List<Component> lore, int amplifier) {
        int bonusDuration;
        if(!isAffectedByLevels()){
            bonusDuration = LEVEL_DURATION_UNAFFECTED_BY_LEVELS * amplifier;
            amplifier = 0;
        }
        else{
            bonusDuration = LEVEL_DURATION * amplifier;
        }

        TagHelper.AddCustomEffect(meta,id,amplifier, BASE_DURATION + bonusDuration);
        Component line = displayName
                .append(Component.text(" " + toRomanNumeral(amplifier+1) + " ("  + ticksToTime(BASE_DURATION + bonusDuration) + ")"))
                .color(displayName.color())
                .decoration(TextDecoration.ITALIC, false);
        lore.add(line);
    }
    public static Optional<CustomEffectType> fromId(String id) {
        for (CustomEffectType type : CustomEffectType.values()) {
            if (type.getId().equals(id)) return Optional.of(type);
        }
        return Optional.empty();
    }

    public boolean isAffectedByLevels(){
        return AffectedByLevels;
    }

}
