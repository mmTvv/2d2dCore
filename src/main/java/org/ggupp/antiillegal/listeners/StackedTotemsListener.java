package org.ggupp.antiillegal.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StackedTotemsListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            var player = (Player) event.getWhoClicked();
            var inventory = player.getInventory();
            cleanTotemStacks(inventory);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            var player = (Player) event.getEntity();
            var item = event.getItem().getItemStack();
            cleanTotemStack(item, player);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        var item = event.getItem();
        if (item.getType() == Material.TOTEM_OF_UNDYING) {
            var player = event.getPlayer();
            var inventory = player.getInventory();
            cleanTotemStacks(inventory);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        var player = event.getPlayer();
        var inventory = player.getInventory();

        cleanTotemStacks(inventory);
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            var player = (Player) event.getWhoClicked();
            var inventory = player.getInventory();
            cleanTotemStacks(inventory);
        }
    }

    @EventHandler
    public void onInventoryPickup(InventoryPickupItemEvent event) {
        var inventory = event.getInventory();
        cleanTotemStacks(inventory);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var inventory = player.getInventory();
        cleanTotemStacks(inventory);
    }

    private void cleanTotemStacks(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                int amount = item.getAmount();
                if (amount > 1) {
                    item.setAmount(1); // Keep only one totem
                    //inventory.addItem(new ItemStack(Material.TOTEM_OF_UNDYING, amount - 1)); // Add the rest to the inventory
                }
            }
        }
    }

    private void cleanTotemStack(ItemStack item, Player player) {
        if (item.getType() == Material.TOTEM_OF_UNDYING) {
            int amount = item.getAmount();
            if (amount > 1) {
                item.setAmount(1);
                //player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, amount - 1));
            }
        }
    }

    private void cleanItemStack(ItemStack item) {
        int amount = item.getAmount();
        if (amount > 1) {
            item.setAmount(1);
        }
    }

}
