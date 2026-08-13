package dev.alchemyredirected.commands;


import dev.alchemyredirected.Lore.IngredientGUI;
import dev.alchemyredirected.recipie.RecipeManager;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor {

    private final IngredientGUI gui;
    public MenuCommand(IngredientGUI gui) {
        this.gui = gui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            gui.open(player, RecipeManager.GetIngredients());
        }
        return true;
    }
}
