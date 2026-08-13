package dev.alchemyredirected;

import dev.alchemyredirected.Incridients.IngredientLoader;
import dev.alchemyredirected.Listeners.MainListener;
import dev.alchemyredirected.Lore.IngredientGUI;
import dev.alchemyredirected.commands.MenuCommand;
import dev.alchemyredirected.papi.AlchemyPlaceholders;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import org.bukkit.plugin.java.JavaPlugin;

public final class AlchemyRedirected extends JavaPlugin {
    public static AlchemyRedirected instance;
    public static AuraSkillsApi auraSkills;
    private IngredientGUI gui;

    @Override
    public void onEnable() {
        instance = this;
        auraSkills = AuraSkillsApi.get();
        gui = new IngredientGUI();

        getLogger().info("Alchemy Redirected has been enabled.");
        getServer().getPluginManager().registerEvents(new MainListener(), this);
        getServer().getPluginManager().registerEvents(gui, this);

        registerCommand("Ingredients",new MenuCommand(gui));

        getLogger().info("Registering PAPI expansion...");
        boolean success = new AlchemyPlaceholders().register();
        getLogger().info("PAPI expansion registered: " + success);
        new IngredientLoader(this).loadAll();
    }
    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor) {
        var command = getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
        }
    }


    @Override
    public void onDisable() {
        getLogger().info("Alchemy Redirected has been disabled.");
    }
}
