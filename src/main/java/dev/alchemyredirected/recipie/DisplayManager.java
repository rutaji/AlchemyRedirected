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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.alchemyredirected.recipie.RecipeManager.MAXPOTIONTOXICITY;

public class DisplayManager {

    private static final Map<Location, Map<UUID, TextDisplay>> displays = new HashMap<>();
    private static final Map<Location, Map<UUID, BukkitTask>> deleteTasks = new HashMap<>();

    public static void Update(Location location, CraftingPotion potion, Player player) {
        UUID uuid = player.getUniqueId();
        TextDisplay display;
        Map<UUID, TextDisplay> inner = displays.computeIfAbsent(location, k -> new HashMap<>());
        if (!inner.containsKey(uuid)) {
            display = location.getWorld().spawn(location.clone().add(0.5, 1.3, 0.5), TextDisplay.class, entity -> {
                entity.setBillboard(Display.Billboard.CENTER);
                entity.setVisibleByDefault(false);
            });
            inner.put(uuid, display);
            player.showEntity(AlchemyRedirected.instance, display);
        } else {
            display = inner.get(uuid);
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

        BukkitTask existingTask = deleteTasks.getOrDefault(location, Collections.emptyMap()).get(uuid);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Schedule a fresh deletion, replacing the old one
        BukkitTask newTask = Bukkit.getScheduler().runTaskLater(
                AlchemyRedirected.instance,
                () -> Delete(location, player),
                20L * 30 // 30 seconds
        );
        deleteTasks.computeIfAbsent(location, k -> new HashMap<>()).put(uuid, newTask);
    }
    public static TextColor toxicColor(int toxicity){
        if(toxicity > MAXPOTIONTOXICITY){return TextColor.color(0,255,0);}
        int c = 255 - (int)(255.0/ MAXPOTIONTOXICITY * toxicity);
        return TextColor.color(c,255,c);
    }

    public static void Delete(Location location, Player player) {
        UUID uuid = player.getUniqueId();
        Map<UUID, TextDisplay> inner = displays.remove(location);
        if (inner != null) {
            TextDisplay display = inner.remove(uuid);
            if (display != null) {
                display.remove();
            }
            if (!inner.isEmpty()) {
                displays.put(location, inner);
            }
        }
        Map<UUID, BukkitTask> tasks = deleteTasks.remove(location);
        if (tasks != null) {
            BukkitTask task = tasks.remove(uuid);
            if (task != null) {
                task.cancel();
            }
            if (!tasks.isEmpty()) {
                deleteTasks.put(location, tasks);
            }
        }
    }

    public static void DeleteForPlayer(UUID uuid) {
        Iterator<Map.Entry<Location, Map<UUID, TextDisplay>>> displayIt = displays.entrySet().iterator();
        while (displayIt.hasNext()) {
            Map.Entry<Location, Map<UUID, TextDisplay>> entry = displayIt.next();
            TextDisplay display = entry.getValue().remove(uuid);
            if (display != null) {
                display.remove();
            }
            if (entry.getValue().isEmpty()) {
                displayIt.remove();
            }
        }
        Iterator<Map.Entry<Location, Map<UUID, BukkitTask>>> taskIt = deleteTasks.entrySet().iterator();
        while (taskIt.hasNext()) {
            Map.Entry<Location, Map<UUID, BukkitTask>> entry = taskIt.next();
            BukkitTask task = entry.getValue().remove(uuid);
            if (task != null) {
                task.cancel();
            }
            if (entry.getValue().isEmpty()) {
                taskIt.remove();
            }
        }
    }
    private static String toLine(int i){
        int line = (i%100)/10;
        return "-".repeat(line) + "■" + "-".repeat(9-line) + " " + (i/100);
    }
}
