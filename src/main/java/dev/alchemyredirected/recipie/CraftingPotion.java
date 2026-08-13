package dev.alchemyredirected.recipie;

import dev.alchemyredirected.AlchemyRedirected;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.alchemyredirected.Incridients.Ingredient.STO;

public class CraftingPotion {
    public HashMap<PotionEffectType, Integer> effects = new HashMap<>();

    private int toxicity = 0;

    public void setToxic(int i){toxicity = i;}

    public int getToxic(){return toxicity;}

    public int getMax(){
        int max = 0;
        for( Map.Entry<PotionEffectType, Integer> entry : effects.entrySet()){
            if(entry.getValue() > max){
                max = entry.getValue();
            }
        }
        return max;
    }
    List<Integer> GetTop4(){
        return effects.entrySet().stream()
                .sorted(Map.Entry.<PotionEffectType, Integer>comparingByValue().reversed())
                .limit(4)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


    public Color GetColor(){
        int totalR = 0, totalG = 0, totalB = 0;
        int level = 0;
        for(Map.Entry<PotionEffectType, Integer> effect : effects.entrySet()){
            Color c = effect.getKey().getColor();
            int currentLevel = effect.getValue();
            totalR += c.getRed() * currentLevel;
            totalG += c.getGreen() * currentLevel;
            totalB += c.getBlue() * currentLevel;
            level += currentLevel;
        }
        if(level == 0){
            return Color.BLUE;
        }
        return Color.fromRGB(totalR /level ,totalG/level,totalB/level);
    }

}
