package dev.alchemyredirected.Incridients;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.customEffects.CustomEffectsCode;
import dev.alchemyredirected.customEffects.CustomEffectType;
import dev.alchemyredirected.customEffects.EffectManager;
import dev.alchemyredirected.customEffects.EffectType;
import dev.alchemyredirected.customEffects.InstantEffectType;
import dev.alchemyredirected.customEffects.VanillaEffectType;
import dev.alchemyredirected.exp.AuraUtil;
import dev.alchemyredirected.exp.ExpManager;
import dev.alchemyredirected.exp.MnoCoreUtil;
import dev.alchemyredirected.recipie.RecipeManager;
import org.bukkit.Bukkit;
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

    public int loadAll() {
        File file = new File(plugin.getDataFolder(), "ingredients.yml");
        if (!file.exists()) {
            plugin.saveResource("ingredients.yml", false); // copies from src/main/resources if bundled
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        EffectManager.BASE_DURATION = config.getInt("baseDuration", EffectManager.BASE_DURATION);
        EffectManager.LEVEL_DURATION = config.getInt("levelDuration", EffectManager.LEVEL_DURATION);
        EffectManager.LEVEL_DURATION_UNAFFECTED_BY_LEVELS = config.getInt("levelDurationUnaffectedByLevels", EffectManager.LEVEL_DURATION_UNAFFECTED_BY_LEVELS);
        RecipeManager.MAXPOTIONTOXICITY = config.getInt("maxPotionToxicity", RecipeManager.MAXPOTIONTOXICITY);
        RecipeManager.MAXTOXICITY = config.getInt("maxToxicity", RecipeManager.MAXTOXICITY);

        CustomEffectsCode.LIFESTEAL_PER_LEVEL = config.getDouble("lifestealPerLevel", CustomEffectsCode.LIFESTEAL_PER_LEVEL);
        CustomEffectsCode.BOMB_POWER_PER_LEVEL = (float) config.getDouble("bombPowerPerLevel", CustomEffectsCode.BOMB_POWER_PER_LEVEL);

        String expSetting = config.getString("expMod","none");
        if(expSetting.equals("AuraSkills") && Bukkit.getPluginManager().getPlugin("AuraSkills") != null){
            ExpManager.SetAdapter(new AuraUtil());
        } else if (expSetting.equals("MMOCore") && Bukkit.getPluginManager().getPlugin("MMOCore") != null) {
            ExpManager.SetAdapter(new MnoCoreUtil());
        }


        ConfigurationSection section = config.getConfigurationSection("ingredients");

        if (section == null) {
            plugin.getLogger().warning("No 'ingredients' section found in ingredients.yml");
            return 0;
        }

        for (String key : section.getKeys(false)) {
            AlchemyRedirected.Print(key);
            Material material = Material.matchMaterial(key);
            if (material == null) {
                plugin.getLogger().warning("Unknown material in ingredients.yml: " + key);
                continue;
            }

            ConfigurationSection entry = section.getConfigurationSection(key);
            int toxicity = entry.getInt("toxicity");
            int loreLevel = entry.getInt("loreLevel");
            double exp =  entry.getDouble("exp");


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

            register(material, toxicity, loreLevel, effects.toArray(new IngredientEffect[0]),exp);
        }

        int count = section.getKeys(false).size();
        plugin.getLogger().info("Loaded " + count + " ingredients from config.");
        return count;
    }

    private void register(Material material, int toxicity, int loreLevel, IngredientEffect[] effects,double exp) {
        RecipeManager.register(material,new Ingredient(material,effects,toxicity,loreLevel,exp));
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