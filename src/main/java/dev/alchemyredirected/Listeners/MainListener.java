package dev.alchemyredirected.Listeners;

import dev.alchemyredirected.AlchemyRedirected;
import dev.alchemyredirected.Incridients.Ingredient;
import dev.alchemyredirected.Lore.LoreManager;
import dev.alchemyredirected.PersistentData.TagHelper;
import dev.alchemyredirected.Toxicity.ToxicityManager;
import dev.alchemyredirected.customEffects.EffectManager;
import dev.alchemyredirected.helpers.ParticleUtil;
import dev.alchemyredirected.recipie.DisplayManager;
import dev.alchemyredirected.recipie.RecipeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static dev.alchemyredirected.PersistentData.TagHelper.getToxicity;
import static dev.alchemyredirected.Toxicity.ToxicityManager.toxicityDeaths;
import static dev.alchemyredirected.recipie.RecipeManager.cauldronContents;

public class MainListener implements Listener {

    // Track ingredients per cauldron location


    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        Ingredient ingredient = RecipeManager.Convert(item.getItemStack().getType());
        if(ingredient != null) {
            new BukkitRunnable() {
                int ticksWaited = 0;

                @Override
                public void run() {
                    // Give up if the item died or too much time passed (e.g. 5 seconds)
                    if (!item.isValid() || item.isDead() || ticksWaited > 100) {
                        cancel();
                        return;
                    }
                    if (item.isOnGround()) {
                        Block below = item.getLocation().getBlock();
                        if (isCauldron(below)) {
                            Location key = below.getLocation();
                            RecipeManager.throwIn(key,ingredient, event.getPlayer());
                            item.remove();
                            ParticleUtil.splash(below.getLocation().add(0.5,1,0.5));

                        }
                        cancel(); // stop checking either way once it's landed
                    }

                    ticksWaited++;
                }
            }.runTaskTimer(AlchemyRedirected.instance, 1L, 5L);
        }
    }

    @EventHandler
    public void onCauldronRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;


        Block block = event.getClickedBlock();
        if (!isCauldron(block)) return;
        Location location = block.getLocation();

        Player player = event.getPlayer();
        if(RecipeManager.IsEmpty(location)){
            player.sendMessage(SendEmptyMessage());
            return;
        }
        RecipeManager.craft(location,player);
    }
    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if (item.getType() != Material.POTION) return;
        Optional<Integer> toxic = TagHelper.getToxicity(item);
        if(toxic.isEmpty()){return;}
        Player player = event.getPlayer();
        ToxicityManager.add(player,toxic.get());
        EffectManager.drink(player,item);
        EffectManager.applyInstant(player,item);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack stack =  event.getItem().getItemStack();
        Ingredient ingredient = RecipeManager.Convert(stack.getType());
        if(ingredient == null){return;}
        LoreManager.UpdateItemStack(stack, player,ingredient);
        // no need to call setItemStack — the ItemStack is mutated in place via getItemMeta()/setItemMeta()
        // but Bukkit merges it into the player's inventory on its own after this event, so it's safe
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ToxicityManager.reset(player);

        if (toxicityDeaths.remove(player.getUniqueId())) {
            event.deathMessage(Component.text(player.getName() + " succumbed to intoxication.", NamedTextColor.DARK_GREEN));
        }
    }

    public String SendEmptyMessage(){
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 5);
        return switch(randomNumber){
            case 1 ->  "Cauldron seems empty...";
            case 2 ->  "Try trowing something inside";
            case 3 ->  "Full of nothing";
            case 4 ->  "dhuunn...";
            default -> "Cauldron seems empty...";
        };

    }

    public boolean isCauldron(Block block){
        if (block == null){return false;}
        return block.getType() == Material.WATER_CAULDRON;
    }

}
