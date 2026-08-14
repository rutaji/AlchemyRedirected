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
    public static void smokeBomb(Location location){
        location.getWorld().spawnParticle(Particle.LARGE_SMOKE, location, 15, 0.6, 0.6, 0.6, 0);
        for (double radius = 0.5; radius <= 2.5; radius += 0.5) {
            for (int i = 0; i < 12; i++) {
                double angle = i * (2 * Math.PI / 12);
                Location offset = location.clone().add(
                        Math.cos(angle) * radius,
                        0.2,
                        Math.sin(angle) * radius
                );
                offset.getWorld().spawnParticle(Particle.SMOKE, offset, 1, 0.1, 0.2, 0.1, 0);
            }
        }
    }
}
