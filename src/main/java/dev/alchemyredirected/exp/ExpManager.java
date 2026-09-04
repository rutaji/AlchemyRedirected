package dev.alchemyredirected.exp;

import org.bukkit.entity.Player;

public class ExpManager {

    public static void SetAdapter(ExpAdapter adapter){
        expAdapter = adapter;
    }
    static ExpAdapter expAdapter = null;
    public static void addExp(Player player,double amount){
        if(expAdapter != null){
            expAdapter.GiveAlchemyEXP(player,amount);
        }
    }
    public static int getLevel(Player player){
        if(expAdapter != null){
            return expAdapter.getAlchemyLevel(player);
        }
        return 1;
    }
}
