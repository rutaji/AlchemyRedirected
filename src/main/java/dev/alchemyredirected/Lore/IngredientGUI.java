package dev.alchemyredirected.Lore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import dev.alchemyredirected.Incridients.Ingredient;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientGUI implements Listener {

    private static final String GUI_TITLE = "Discovered Ingredients";

    public void open(Player player, List<Ingredient> allIngredients) {
        int size = ((allIngredients.size() - 1) / 9 + 1) * 9; // round up to nearest multiple of 9
        Inventory gui = Bukkit.createInventory(null, size, Component.text(GUI_TITLE));
        for (Ingredient ingredient : allIngredients) {
            boolean unlocked = ingredient.IsUnlocked(player);
            ItemStack display;

            if (unlocked) {
                display = new ItemStack(ingredient.ingredient);
                ItemMeta meta = display.getItemMeta();
                meta.displayName(Component.translatable(ingredient.ingredient.translationKey())
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false));
                List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();
                LoreManager.MarkIngridient(lore);
                meta.lore(LoreManager.getLore(ingredient,lore));
                display.setItemMeta(meta);
            } else {
                display = new ItemStack(Material.GRAY_DYE); // placeholder for locked
                ItemMeta meta = display.getItemMeta();
                meta.displayName(Component.text("???")
                        .color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(Component.text("Not yet discovered")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)));
                display.setItemMeta(meta);
            }

            gui.addItem(display);
        }

        player.openInventory(gui);
    }

    private String formatName(Material mat) { //todo delede this
        String raw = mat.name().toLowerCase().replace("_", " ");
        return Arrays.stream(raw.split(" "))
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.joining(" "));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text(GUI_TITLE))) {
            event.setCancelled(true); // prevent taking items out
        }
    }
}