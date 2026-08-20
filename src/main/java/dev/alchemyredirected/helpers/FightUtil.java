package dev.alchemyredirected.helpers;

import dev.alchemyredirected.AlchemyRedirected;
import org.bukkit.Location;


public class FightUtil {

    public static void Explosion(Location location, float power){
        AlchemyRedirected.instance.getLogger().info(power + "");
        location.getWorld().createExplosion(location,power);
    }
}
