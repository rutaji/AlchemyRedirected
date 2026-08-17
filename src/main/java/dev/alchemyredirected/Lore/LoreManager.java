package dev.alchemyredirected.Lore;

import dev.alchemyredirected.Incridients.Ingredient;
import dev.alchemyredirected.Incridients.IngredientEffect;
import dev.alchemyredirected.PersistentData.TagHelper;
import dev.alchemyredirected.aura.AuraUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class LoreManager {
    private static final Component MARKER = Component.text("[INGRIDIENT]").color(NamedTextColor.RED).decorate(TextDecoration.BOLD);

    public static void UpdateItemStack(ItemStack stack, Player player, Ingredient ingredient) {
        ItemMeta meta = stack.getItemMeta();
        if (meta.getPersistentDataContainer().has(TagHelper.INGREDIENT_LORE_KEY, PersistentDataType.BOOLEAN)) {
            if (AuraUtil.getAlchemyLevel(player) < ingredient.loreLevel) {
                removeAlchemyLore(meta, ingredient);
                stack.setItemMeta(meta);
            }
            return;
        }

        meta.getPersistentDataContainer().set(TagHelper.INGREDIENT_LORE_KEY, PersistentDataType.BOOLEAN, true);
        List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
        MarkIngridient(lore);

        if(AuraUtil.getAlchemyLevel(player) >= ingredient.loreLevel)
        {
            meta.lore(getLore(ingredient,lore));
        }
        stack.setItemMeta(meta);
    }

    private static void removeAlchemyLore(ItemMeta meta, Ingredient ingredient) {
        List<Component> lore = meta.lore();
        if (lore == null) return;
        List<Component> effectLines = getLore(ingredient, new ArrayList<>());
        lore.removeIf(line -> line.equals(MARKER) || effectLines.contains(line));
        meta.lore(lore);
    }

    public static List<Component> getLore(Ingredient ingredient,List<Component> lore){
        for(IngredientEffect effect : ingredient.effects){
            lore.add(effect.effect().getDisplayName()
                    .append(Component.text(": " + effect.value()))
                    .append(Component.text("    max: " + effect.max()))
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, true));
        }
        lore.add(Component.text("toxicity: " + ingredient.getToxicity()).color(NamedTextColor.DARK_GREEN));
        lore.add((Component.text("exp: " + ingredient.exp)).color(NamedTextColor.WHITE)
        );
        return lore;
    }
    public static void MarkIngridient( List<Component> lore){
        lore.add(MARKER);
    }
}
