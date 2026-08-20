package dev.alchemyredirected.PersistentData;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.customEffects.CustomEffect;
import dev.alchemyredirected.customEffects.CustomEffectType;
import dev.alchemyredirected.customEffects.InstantEffectType;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TagHelper {
    public static final NamespacedKey TOXICITY_KEY = new NamespacedKey(AlchemyRedirected.instance, "toxicity");

    public static  final NamespacedKey EFFECTS_ARRAY_KEY =  new NamespacedKey(AlchemyRedirected.instance, "customEffects");
    public static  final NamespacedKey EFFECT_ID_KEY =  new NamespacedKey(AlchemyRedirected.instance, "customEffectsID");
    public static  final NamespacedKey EFFECT_VALUE_KEY =  new NamespacedKey(AlchemyRedirected.instance, "customEffectsValue");
    public static  final NamespacedKey EFFECT_DURATION_KEY =  new NamespacedKey(AlchemyRedirected.instance, "customEffectsDuration");

    public static final NamespacedKey INSTANT_ARRAY_KEY =  new NamespacedKey(AlchemyRedirected.instance, "instantEffects");
    public static  final NamespacedKey INSTANT_ID_KEY =  new NamespacedKey(AlchemyRedirected.instance, "instantEffectsID");
    public static  final NamespacedKey INSTANT_VALUE_KEY =  new NamespacedKey(AlchemyRedirected.instance, "instantEffectsKey");

    public static final NamespacedKey INGREDIENT_LORE_KEY = new NamespacedKey(AlchemyRedirected.instance, "ingredientLore");

    public static void setToxicity(PotionMeta potion, int toxicity) {
        potion.getPersistentDataContainer().set(TOXICITY_KEY, PersistentDataType.INTEGER, toxicity);
    }


    public static Optional<Integer> getToxicity(ItemStack potion) {
        ItemMeta meta = potion.getItemMeta();
        Integer value = meta.getPersistentDataContainer().get(TOXICITY_KEY, PersistentDataType.INTEGER);
        AlchemyRedirected.instance.getLogger().info(value + "");
        return Optional.ofNullable(value);
    }
    public  static void AddCustomEffect(PotionMeta meta,String id,int amplifier,int duration) {

        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = container.getAdapterContext();

        // Load old ones
        List<PersistentDataContainer> existing = container.get(EFFECTS_ARRAY_KEY, PersistentDataType.LIST.dataContainers());
        if(existing == null){
            existing = new ArrayList<>();
        }

        // Build this effect's entry
        PersistentDataContainer entry = context.newPersistentDataContainer();
        entry.set(EFFECT_ID_KEY, PersistentDataType.STRING, id);
        entry.set(EFFECT_VALUE_KEY, PersistentDataType.INTEGER, amplifier);
        entry.set(EFFECT_DURATION_KEY, PersistentDataType.INTEGER,duration);
        existing.add(entry);


        container.set(EFFECTS_ARRAY_KEY, PersistentDataType.LIST.dataContainers(), existing);
    }

    public static List<CustomEffect<CustomEffectType>> LoadCustomEffects(PotionMeta meta){
        List<CustomEffect<CustomEffectType>> result = new ArrayList<>();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<PersistentDataContainer> effectList = container.get(EFFECTS_ARRAY_KEY, PersistentDataType.LIST.dataContainers());
        if (effectList == null) {
            return result;
        }

        for (PersistentDataContainer entry : effectList) {
            String id = entry.get(EFFECT_ID_KEY, PersistentDataType.STRING);
            Integer amplifier = entry.get(EFFECT_VALUE_KEY, PersistentDataType.INTEGER);
            Integer duration = entry.get(EFFECT_DURATION_KEY,PersistentDataType.INTEGER);
            Optional<CustomEffectType> effect = CustomEffectType.fromId(id);
            if (effect.isEmpty()  || amplifier == null || duration == null) {
                continue;
            }
                result.add(new CustomEffect<>(effect.get(), amplifier,duration));
        }
        return result;
    }

    public  static void AddCustomEffectInstant(PotionMeta meta,String id,int amplifier) {

        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = container.getAdapterContext();

        // Load old ones
        List<PersistentDataContainer> existing = container.get(INSTANT_ARRAY_KEY, PersistentDataType.LIST.dataContainers());
        if(existing == null){
            existing = new ArrayList<>();
        }

        // Build this effect's entry
        PersistentDataContainer entry = context.newPersistentDataContainer();
        entry.set(INSTANT_ID_KEY, PersistentDataType.STRING, id);
        entry.set(INSTANT_VALUE_KEY, PersistentDataType.INTEGER, amplifier);
        existing.add(entry);


        container.set(INSTANT_ARRAY_KEY, PersistentDataType.LIST.dataContainers(), existing);
    }

    public static List<CustomEffect<InstantEffectType>> LoadCustomEffectsInstant(PotionMeta meta){
        List<CustomEffect<InstantEffectType>> result = new ArrayList<>();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<PersistentDataContainer> effectList = container.get(INSTANT_ARRAY_KEY, PersistentDataType.LIST.dataContainers());
        if (effectList == null) {
            return result;
        }

        for (PersistentDataContainer entry : effectList) {
            String id = entry.get(INSTANT_ID_KEY, PersistentDataType.STRING);
            Integer amplifier = entry.get(INSTANT_VALUE_KEY, PersistentDataType.INTEGER);
            Optional<InstantEffectType> effect = InstantEffectType.fromId(id);
            if (effect.isEmpty()  || amplifier == null) {
                continue;
            }
            result.add(new CustomEffect<>(effect.get(), amplifier,0));
        }
        return result;
    }
}
