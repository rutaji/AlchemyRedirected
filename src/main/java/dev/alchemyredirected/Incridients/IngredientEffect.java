package dev.alchemyredirected.Incridients;

import dev.alchemyredirected.customEffects.CustomEffectType;
import dev.alchemyredirected.customEffects.EffectType;
import org.bukkit.potion.PotionEffectType;

public record IngredientEffect(EffectType effect, int value, int max) {
}
