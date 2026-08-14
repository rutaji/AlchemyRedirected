package dev.alchemyredirected.Lore;

import dev.alchemyredirected.Incridients.Ingredient;
import dev.alchemyredirected.Incridients.IngredientEffect;
import dev.alchemyredirected.aura.AuraUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreManager {
    public static void UpdateItemStack(ItemStack stack, Player player, Ingredient ingredient) {
        ItemMeta meta = stack.getItemMeta();
        List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
        MarkIngridient(lore);

        if(AuraUtil.getAlchemyLevel(player) < ingredient.loreLevel){return;}
        meta.lore(getLore(ingredient,lore));
        stack.setItemMeta(meta);
    }
    public static List<Component> getLore(Ingredient ingredient,List<Component> lore){
        for(IngredientEffect effect : ingredient.effects){
            lore.add(effect.effect().getDisplayName()
                    .append(Component.text(": " + effect.value()))
                    .append(Component.text("    max: " + effect.max()))
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, true));
        }
        return lore;
    }
    public static void MarkIngridient( List<Component> lore){
        lore.add(Component.text("[INGRIDIENT]").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
    }
}
