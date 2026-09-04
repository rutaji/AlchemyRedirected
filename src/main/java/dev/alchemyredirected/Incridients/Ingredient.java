package dev.alchemyredirected.Incridients;

import dev.alchemyredirected.exp.AuraUtil;
import dev.alchemyredirected.exp.ExpManager;
import dev.alchemyredirected.recipie.CraftingPotion;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static dev.alchemyredirected.AlchemyRedirected.Print;

public class Ingredient {
    public static final int STO = 100;
    public static final double DISCOUNT_MODIFIER = 0.01;

    public final Material ingredient;

    public final IngredientEffect[] effects;

    public final int loreLevel;

    final int toxicity;
    public final double exp;


    Ingredient(Material ingredient, IngredientEffect[] effects, int toxicity, int loreLevel,double exp){
        this.ingredient = ingredient;
        this.effects = effects;
        this.toxicity = toxicity;
        this.loreLevel = loreLevel;
        this.exp = exp;
    }

    public int getToxicity(){
        return toxicity;
    }

    public void ApplyAll(CraftingPotion potion,Player player){
        for(IngredientEffect effect : effects){
            AddEffect(potion,effect,1);
        }
        aplytoxic(potion,player);
    }
    public void aplytoxic(CraftingPotion potion,Player player){
        int level = ExpManager.getLevel(player);
        int discountedToxicity = Math.max((int)(getToxicity() * (100 - level) * DISCOUNT_MODIFIER),1);
        potion.setToxic(potion.getToxic() + discountedToxicity);
    }

    public void AddEffect(CraftingPotion potion,IngredientEffect effect,int modifier){
         int old = potion.effects.getOrDefault(effect.effect(),0);
         Print("old " + old);
         int toMax = Math.max(effect.max() * STO - old,0);
         Print("tomax " + toMax);
         int tobuff = Math.min(effect.value() * modifier,toMax);//todo test
         Print("tobuff " + tobuff);
         potion.effects.put(effect.effect(),Math.max(old + tobuff,0));
    }
    public boolean IsUnlocked(Player player){
        return ExpManager.getLevel(player) >= loreLevel;
    }

}
