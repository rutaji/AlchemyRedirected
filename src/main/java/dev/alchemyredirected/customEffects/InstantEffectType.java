package dev.alchemyredirected.customEffects;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.PersistentData.TagHelper;
import dev.alchemyredirected.helpers.FightUtil;
import dev.alchemyredirected.helpers.ParticleUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static dev.alchemyredirected.helpers.TextUtil.colorToTextColor;
import static dev.alchemyredirected.helpers.TextUtil.toRomanNumeral;

public enum InstantEffectType implements EffectType {
    SMOKE("SMOKE", Component.text("Smoke", TextColor.color(64, 68, 63)), Color.BLACK, PotionEffectType.Category.NEUTRAL) {
        @Override
        public void applyInstant(Player player, int amplifier) {
            for(int i = 0; i <= amplifier ; i++  ) {
                ParticleUtil.smokeBomb(player.getLocation().clone().add(ThreadLocalRandom.current().nextDouble(-1.0,1.0),ThreadLocalRandom.current().nextDouble(-1.0,1.0),ThreadLocalRandom.current().nextDouble(-1.0,1.0)));
            }
        }
    };


    private final String id;
    private final Component displayName;
    private final Color color;
    private final PotionEffectType.Category category;

    InstantEffectType(String id, Component displayName, Color color, PotionEffectType.Category category) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.category = category;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public PotionEffectType.Category getEffectCategory() {
        return category;
    }

    @Override
    public void applyPotion(PotionMeta meta, List<Component> lore, int amplifier) {
        TagHelper.AddCustomEffectInstant(meta, id, amplifier);
        Component line = displayName
                .append(Component.text(" " + toRomanNumeral(amplifier + 1)))
                .color(displayName.color())
                .decoration(TextDecoration.ITALIC, false);
        lore.add(line);
    }

    public void applyInstant(Player player, int amplifier) {
        player.sendMessage("dev forgot to override");
    }

    public static Optional<InstantEffectType> fromId(String id) {
        for (InstantEffectType type : values()) {
            if (type.getId().equals(id)) return Optional.of(type);
        }
        return Optional.empty();
    }
}
