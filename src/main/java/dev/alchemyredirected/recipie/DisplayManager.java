package dev.alchemyredirected.recipie;

import dev.alchemyredirected.AlchemyRedirected;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.alchemyredirected.recipie.RecipeManager.MAXTOXICITY;

public class DisplayManager {

    private static final Map<Location, TextDisplay> displays = new HashMap<>();
    private static final Map<Location, BukkitTask> deleteTasks = new HashMap<>();

    public static void Update(Location location, CraftingPotion potion, Player player) {
        TextDisplay display;
        if (!displays.containsKey(location)) {
            display = location.getWorld().spawn(location.clone().add(0.5, 1.3, 0.5), TextDisplay.class, entity -> {
                entity.setBillboard(Display.Billboard.CENTER);
                entity.setVisibleByDefault(false);
            });
            displays.put(location, display);
            player.showEntity(AlchemyRedirected.instance, display);
        } else {
            display = displays.get(location);
        }

        List<Integer> top4 = potion.GetTop4();

        Component fullText = Component.empty();
        for (int i = 0; i < top4.size() && i < 4; i++) {
            if (i > 0) {
                fullText = fullText.append(Component.newline());
            }
            fullText = fullText.append(Component.text(toLine(top4.get(i))));
        }

        display.text(fullText.color(toxicColor(potion.getToxic())));

        // Cancel any previously scheduled deletion for this location
        BukkitTask existingTask = deleteTasks.get(location);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Schedule a fresh deletion, replacing the old one
        BukkitTask newTask = Bukkit.getScheduler().runTaskLater(
                AlchemyRedirected.instance,
                () -> Delete(location),
                20L * 30 // 30 seconds
        );
        deleteTasks.put(location, newTask);
    }
    public static TextColor toxicColor(int toxicity){
        if(toxicity > MAXTOXICITY){return TextColor.color(0,255,0);}
        int c = 255 - (int)(255.0/MAXTOXICITY * toxicity);
        return TextColor.color(c,255,c);
    }

    public static void Delete(Location location){
        TextDisplay display = displays.remove(location);
        if(display != null) {
            display.remove();
        }
    }
    private static String toLine(int i){
        int line = (i%100)/10;
        return "-".repeat(line) + "■" + "-".repeat(9-line) + " " + (i/100);
    }
}
