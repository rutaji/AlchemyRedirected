package dev.alchemyredirected.commands;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.Incridients.IngredientLoader;
import dev.alchemyredirected.recipie.RecipeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("Usage: /alchemyredirected reload");
            return true;
        }
        RecipeManager.clearIngredients();
        int count = new IngredientLoader(AlchemyRedirected.instance).loadAll();
        sender.sendMessage("Reloaded " + count + " ingredients from ingredients.yml.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && "reload".startsWith(args[0].toLowerCase())) {
            return List.of("reload");
        }
        return List.of();
    }
}
