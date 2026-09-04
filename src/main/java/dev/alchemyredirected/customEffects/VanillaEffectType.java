package dev.alchemyredirected.customEffects;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static dev.alchemyredirected.customEffects.EffectManager.*;

public class VanillaEffectType implements EffectType {

    private static final Set<PotionEffectType> UNAFFECTED_BY_LEVELS = Set.of(
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.WATER_BREATHING,
            PotionEffectType.GLOWING
    );

    public static boolean isAffectedByLevels(PotionEffectType type) {
        return !UNAFFECTED_BY_LEVELS.contains(type);
    }

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
        int bonusDuration;
        if(!isAffectedByLevels(potionEffectType)){
            bonusDuration = LEVEL_DURATION_UNAFFECTED_BY_LEVELS * amplifier;
            amplifier = 0;
        }
        else{
            bonusDuration = LEVEL_DURATION * amplifier;
        }

        PotionEffect effect = new PotionEffect(
                potionEffectType,
                BASE_DURATION + bonusDuration, // duration in ticks, e.g. 60 seconds — tune as needed
                amplifier,
                false,   // ambient (particles subtler if true)
                true,    // show particles
                true     // show icon
        );
        meta.addCustomEffect(effect, true); // true = overwrite existing effect of same type
    }
    @Override
    public int hashCode(){
        return  potionEffectType.hashCode();
    }
    @Override
    public boolean equals(Object other){
        if (other instanceof VanillaEffectType vanillaEffectType){
            return vanillaEffectType.potionEffectType == this.potionEffectType;
        }
        if(other instanceof PotionEffectType potionEffectType){
            return potionEffectType == this.potionEffectType;
        }
        return false;
    }
}