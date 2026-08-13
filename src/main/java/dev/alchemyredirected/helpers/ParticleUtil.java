package dev.alchemyredirected.helpers;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;


public class ParticleUtil {

    public static void splash(Location location){
        location.getWorld().spawnParticle(Particle.SPLASH, location, 6, 0.4, 0.4, 0.4);
    }
    public static void synergy(Location location){
        location.getWorld().spawnParticle(Particle.ENTITY_EFFECT, location, 20, 0.4, 0.4, 0.4, 1, Color.fromRGB(250, 250, 60));
    }
}
