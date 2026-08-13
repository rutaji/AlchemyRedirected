package dev.alchemyredirected.Incridients;

import org.bukkit.potion.PotionEffectType;

public record IngredientEffect(PotionEffectType effect, int value, int max) {
}
