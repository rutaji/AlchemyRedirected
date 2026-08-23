package dev.alchemyredirected.recipie;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.Incridients.Ingredient;
import dev.alchemyredirected.PersistentData.TagHelper;
import dev.alchemyredirected.aura.AuraUtil;
import dev.alchemyredirected.customEffects.EffectType;
import dev.alchemyredirected.helpers.ParticleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.*;

import static dev.alchemyredirected.Incridients.Ingredient.STO;

public class RecipeManager {

    private static final Map<Material, Ingredient> ingredientsByMaterial = new HashMap<>();
    public static Map<Location, Map<UUID, List<Ingredient>>> cauldronContents = new HashMap<>();
    public static Map<Location, Map<UUID, CraftingPotion>> cauldronPotions = new HashMap<>();
    public static int MAXPOTIONTOXICITY = 100;
    public static int MAXTOXICITY = 200;



    public static boolean IsEmpty(Location location, Player player){
        return !cauldronPotions.getOrDefault(location, Collections.emptyMap()).containsKey(player.getUniqueId());
    }

    public static void craft(Location location,Player player){
        UUID uuid = player.getUniqueId();
        CraftingPotion potion = cauldronPotions.getOrDefault(location, Collections.emptyMap()).get(uuid);
        List<Ingredient> contents = cauldronContents.getOrDefault(location, Collections.emptyMap()).get(uuid);
        if(potion == null || contents == null){return;}
        ItemStack itemStack = GetPotion(potion);
        player.give(itemStack);
        giveExp(contents,player,potion);
        EmptyOut(location,player);
        player.playSound(location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
        ParticleUtil.brew(location);
    }
    public static void giveExp(List<Ingredient> ingredients,Player player,CraftingPotion potion){
        Set<Ingredient> uniqueByReference = Collections.newSetFromMap(new IdentityHashMap<>());
        uniqueByReference.addAll(ingredients);
        double penalty = 1;
        if( potion.getMax() < 100){
            penalty *= 0.25;
            player.sendMessage("exp reduced by 75% for zero effect potions");
        }
        /*if(potion.getToxic() > MAXTOXICITY){
            penalty *= 0.5;
            player.sendMessage("exp reduced by 50% for high toxicity");
        }*/
        double exp = 0;
        for(Ingredient ingredient : uniqueByReference){
            exp += ingredient.exp;
        }
        AuraUtil.GiveAlchemyEXP(player,  exp * penalty);
    }

    public static void register(Material item,Ingredient ingredient){
        ingredientsByMaterial.put(item,ingredient);
    }

    public static void clearIngredients(){
        ingredientsByMaterial.clear();
    }

    public static Ingredient Convert(Material item){
        return ingredientsByMaterial.getOrDefault(item,null);
    }

    public static ItemStack GetPotion(CraftingPotion potion){
        ItemStack result = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) result.getItemMeta();
        List<Component> lore = new ArrayList<>();

        meta.setBasePotionType(PotionType.AWKWARD);
        meta.setColor(potion.GetColor());

        for (Map.Entry<EffectType, Integer> entry : potion.effects.entrySet()) {
            EffectType type = entry.getKey();
            int level = entry.getValue();
            int amplifier = level/STO-1; // convert amplifier -> 0-indexed amplifier
            if(amplifier < 0){continue;}
            type.applyPotion(meta,lore,amplifier);
            AlchemyRedirected.Print("recipieManager" + amplifier);
        }
        // Store toxicity as PDC
        TagHelper.setToxicity(meta, potion.getToxic());

        // Display toxicity in lore

        lore.add(Component.text("Toxicity: " + potion.getToxic(), NamedTextColor.DARK_GREEN)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(lore);

        result.setItemMeta(meta);
        return result;
    }
    public static void Print(CraftingPotion potion){
        for (Map.Entry<EffectType, Integer> entry : potion.effects.entrySet()) {
            AlchemyRedirected.getPlugin(AlchemyRedirected.class).getLogger().info(entry.getKey().getId() + " -> amplifier " + entry.getValue());
        }

    }
    public static List<Ingredient> GetIngredients(){
        return ingredientsByMaterial.values().stream().toList();
    }

    public static void throwIn(Location location, Ingredient ingredient, Player player){
        UUID uuid = player.getUniqueId();
        cauldronContents.computeIfAbsent(location, k -> new HashMap<>())
                .computeIfAbsent(uuid, k -> new ArrayList<>())
                .add(ingredient);
        CraftingPotion potion = cauldronPotions.computeIfAbsent(location, k -> new HashMap<>())
                .computeIfAbsent(uuid, k -> new CraftingPotion());
        player.playSound(location, Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
        Apply(potion,ingredient,player);
        if(potion.getToxic() >= MAXPOTIONTOXICITY){
            player.sendMessage("Max toxicity reached");
            craft(location,player);
            return;
        }
        DisplayManager.Update(location, potion,player);
    }
    public static void Apply(CraftingPotion potion,Ingredient ingredient,Player player){
        ingredient.ApplyAll(potion,player);
        Print(potion);
    }

    public static void EmptyOut(Location location, Player player) {
        UUID uuid = player.getUniqueId();
        Map<UUID, List<Ingredient>> contents = cauldronContents.get(location);
        if(contents != null){
            contents.remove(uuid);
        }
        Map<UUID, CraftingPotion> potions = cauldronPotions.get(location);
        if(potions != null){
            potions.remove(uuid);
        }
        DisplayManager.Delete(location,player);
    }
}
