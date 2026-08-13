package dev.alchemyredirected.PersistentData;

import dev.alchemyredirected.AlchemyRedirected;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class TagHelper {
    public static final NamespacedKey TOXICITY_KEY = new NamespacedKey(AlchemyRedirected.instance, "toxicity");

    public static void setToxicity(PotionMeta potion, int toxicity) {
        potion.getPersistentDataContainer().set(TOXICITY_KEY, PersistentDataType.INTEGER, toxicity);
    }


    public static Optional<Integer> getToxicity(ItemStack potion) {
        ItemMeta meta = potion.getItemMeta();
        Integer value = meta.getPersistentDataContainer().get(TOXICITY_KEY, PersistentDataType.INTEGER);
        AlchemyRedirected.instance.getLogger().info(value + "");
        return Optional.ofNullable(value);
    }
}
