package org.ggupp.antiillegal.listeners;

import lombok.RequiredArgsConstructor;
import org.ggupp.antiillegal.AntiIllegalMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class InventoryListeners implements Listener {
    private final AntiIllegalMain main;

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        inventoryEvent(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        inventoryEvent(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        main.checkFixItem(event.getCursor(), event);
    }

    @EventHandler
    public void onHopper(InventoryMoveItemEvent event) {
        main.checkFixItem(event.getItem(), event);
    }

    private void inventoryEvent(InventoryEvent event) {
        for (ItemStack itemStack : event.getInventory()) main.checkFixItem(itemStack, null);
    }
}
