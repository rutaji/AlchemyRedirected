package dev.alchemyredirected.customEffects;

import dev.alchemyredirected.PersistentData.TagHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Optional;

import static dev.alchemyredirected.customEffects.EffectManager.*;
import static dev.alchemyredirected.helpers.TextUtil.*;

public enum CustomEffectType implements EffectType {
    LIFESTEAL("LIFESTEAL", Component.text("Lifesteal", NamedTextColor.RED),Color.RED,PotionEffectType.Category.BENEFICIAL,true,1),
    BOMB("BOMB", Component.text("Bomb", NamedTextColor.DARK_GRAY),Color.GRAY,PotionEffectType.Category.HARMFUL,true,0.02){
        @Override
        public void Start(Player player, int amplifier,long duration) {
             CustomEffectsCode.bomb(player,amplifier, duration);
        }
        @Override
        public void Cleared(Player player, int amplifier) {
            CustomEffectsCode.bombClear(player);
        }
    };
    protected BukkitTask task;
    private final String id;
    private final Component displayName;
    private final Color color;
    private final PotionEffectType.Category category;
    private final boolean AffectedByLevels;
    private final double DurationModifier;

    CustomEffectType(String id, Component displayName,Color color,PotionEffectType.Category category,boolean affectedByLevels,double durationModifier) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.category = category;
        this.AffectedByLevels = affectedByLevels;
        this.DurationModifier = durationModifier;
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

        int finalDuration = (int)((BASE_DURATION + bonusDuration) * DurationModifier);
        TagHelper.AddCustomEffect(meta,id,amplifier,finalDuration );
        Component line = displayName
                .append(Component.text(" " + toRomanNumeral(amplifier+1) + " ("  + ticksToTime(finalDuration) + ")"))
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

    public void Start(Player player, int amplifier,long duration){};
    public void Cleared(Player player, int amplifier){};

}
