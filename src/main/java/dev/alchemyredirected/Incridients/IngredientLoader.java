package dev.alchemyredirected.Incridients;

import dev.alchemyredirected.customEffects.CustomEffectType;
import dev.alchemyredirected.customEffects.EffectManager;
import dev.alchemyredirected.customEffects.EffectType;
import dev.alchemyredirected.customEffects.InstantEffectType;
import dev.alchemyredirected.customEffects.VanillaEffectType;
import dev.alchemyredirected.recipie.RecipeManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IngredientLoader {

    private final JavaPlugin plugin;

    public IngredientLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        File file = new File(plugin.getDataFolder(), "ingredients.yml");
        if (!file.exists()) {
            plugin.saveResource("ingredients.yml", false); // copies from src/main/resources if bundled
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        EffectManager.BASE_DURATION = config.getInt("baseDuration", EffectManager.BASE_DURATION);
        EffectManager.LEVEL_DURATION = config.getInt("levelDuration", EffectManager.LEVEL_DURATION);
        EffectManager.LEVEL_DURATION_UNAFFECTED_BY_LEVELS = config.getInt("levelDurationUnaffectedByLevels", EffectManager.LEVEL_DURATION_UNAFFECTED_BY_LEVELS);
        RecipeManager.MAXTOXICITY = config.getInt("maxToxicity", RecipeManager.MAXTOXICITY);

        ConfigurationSection section = config.getConfigurationSection("ingredients");

        if (section == null) {
            plugin.getLogger().warning("No 'ingredients' section found in ingredients.yml");
            return;
        }

        for (String key : section.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material == null) {
                plugin.getLogger().warning("Unknown material in ingredients.yml: " + key);
                continue;
            }

            ConfigurationSection entry = section.getConfigurationSection(key);
            int toxicity = entry.getInt("toxicity");
            int loreLevel = entry.getInt("loreLevel");
            double exp =  entry.getDouble("exp");
            double synergyExp =  entry.getDouble("synergyExp");


            List<IngredientEffect> effects = new ArrayList<>();
            List<Map<?, ?>> effectMaps = entry.getMapList("effects");

            for (Map<?, ?> effectMap : effectMaps) {
                String typeName = (String) effectMap.get("type");

                EffectType effectType = EffectFromString(typeName);
                if (effectType == null) {
                    plugin.getLogger().warning("Unknown effect type '" + typeName + "' for ingredient " + key);
                    continue;
                }

                int value = (int) effectMap.get("value");
                int max = (int) effectMap.get("max");


                effects.add(new IngredientEffect(effectType, value, max));
            }

            register(material, toxicity, loreLevel, effects.toArray(new IngredientEffect[0]),exp,synergyExp);
        }

        plugin.getLogger().info("Loaded " + section.getKeys(false).size() + " ingredients from config.");
    }

    private void register(Material material, int toxicity, int loreLevel, IngredientEffect[] effects,double exp,double synergyexp) {
        RecipeManager.register(material,new Ingredient(material,effects,toxicity,loreLevel,exp,synergyexp));
    }

    private EffectType EffectFromString(String name){
        Optional<CustomEffectType> result = CustomEffectType.fromId(name);
        if(result.isPresent()){
            return result.get();
        }
        Optional<InstantEffectType> instant = InstantEffectType.fromId(name);
        if(instant.isPresent()){
            return instant.get();
        }
        PotionEffectType vanila = PotionEffectType.getByName(name);
        if(vanila == null){return null;}
        return new VanillaEffectType(vanila);
    }

}