package dev.alchemyredirected.Incridients;

import dev.alchemyredirected.aura.AuraUtil;
import dev.alchemyredirected.recipie.CraftingPotion;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class Ingredient {
    public static final int STO = 100;

    public final Material ingredient;

    public final IngredientEffect[] effects;

    public final int loreLevel;

    final int toxicity;
    public final double synergyExp;
    public final double exp;

    Ingredient(Material ingredient, IngredientEffect[] effects, int toxicity, int loreLevel,double exp,double synergyExp){
        this.ingredient = ingredient;
        this.effects = effects;
        this.toxicity = toxicity;
        this.loreLevel = loreLevel;
        this.synergyExp = synergyExp;
        this.exp = exp;
    }

    public int getToxicity(){
        return toxicity;
    }

    public void ApplyAll(CraftingPotion potion,int modifier){
        for(IngredientEffect effect : effects){
            AddEffect(potion,effect,modifier);
        }
        aplytoxic(potion);
    }
    public void aplytoxic(CraftingPotion potion){
        potion.setToxic(potion.getToxic() + getToxicity());
    }

    public boolean AddEffect(CraftingPotion potion,IngredientEffect effect,int modifier){
         int old = potion.effects.getOrDefault(effect.effect(),0);
         int buffed = Math.min(old + effect.value() * modifier,effect.max() * STO);
         if(old < buffed){
             potion.effects.put(effect.effect(),buffed);
             return true;
         }
         return false;

    }
    public boolean IsUnlocked(Player player){
        return AuraUtil.getAlchemyLevel(player) >= loreLevel;
    }


    public boolean checkSynergy(List<Ingredient> ingredients) {
        //todo
        return false;
    }
}
