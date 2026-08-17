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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.*;

import static dev.alchemyredirected.Incridients.Ingredient.STO;

public class RecipeManager {

    private static final Map<Material, Ingredient> ingredientsByMaterial = new HashMap<>();
    public static Map<Location, List<Ingredient>> cauldronContents = new HashMap<>();
    public static Map<Location, CraftingPotion> cauldronPotions = new HashMap<>();
    public static int MAXTOXICITY = 100;




    public static boolean IsEmpty(Location location){
        return !cauldronPotions.containsKey(location);
    }

    public static void craft(Location location,Player player){
        CraftingPotion potion = cauldronPotions.get(location);
        ItemStack itemStack = GetPotion(potion);
        player.give(itemStack);
        giveExp(cauldronContents.get(location),player,potion);
        EmptyOut(location);
        ParticleUtil.synergy(location);
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
        if(!cauldronContents.containsKey(location)){
            cauldronContents.put(location,new ArrayList<>());
            cauldronPotions.put(location,new CraftingPotion());
        }
        cauldronContents.get(location).add(ingredient);
        CraftingPotion potion = cauldronPotions.get(location);
        Apply(potion,ingredient,player);
        if(potion.getToxic() >= MAXTOXICITY){
            player.sendMessage("Max toxicity reached");
            craft(location,player);
        }
        DisplayManager.Update(location, potion,player);
    }
    public static void Apply(CraftingPotion potion,Ingredient ingredient,Player player){
        ingredient.ApplyAll(potion,player);
        Print(potion);
    }

    public static void EmptyOut(Location location) {
        cauldronContents.remove(location);
        cauldronPotions.remove(location);
        DisplayManager.Delete(location);

    }
}
