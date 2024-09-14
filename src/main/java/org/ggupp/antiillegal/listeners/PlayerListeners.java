package org.ggupp.antiillegal.listeners;

import lombok.RequiredArgsConstructor;
import org.ggupp.antiillegal.AntiIllegalMain;
import org.ggupp.antiillegal.check.Check;
import org.ggupp.util.GlobalUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.logging.Level;

import static org.ggupp.antiillegal.util.Utils.checkStand;

@RequiredArgsConstructor
public class PlayerListeners implements Listener {
    private final AntiIllegalMain main;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        for (ItemStack item : inventory.getContents()) {
            main.checkFixItem(item, null);
        }
        for (ItemStack item : inventory.getArmorContents()) {
            main.checkFixItem(item, null);
        }
        for (ItemStack item : inventory.getExtraContents()) {
            main.checkFixItem(item, null);
        }
        for (ItemStack item : event.getPlayer().getEnderChest()) {
            main.checkFixItem(item, null);
        }
        main.checkFixItem(inventory.getItemInOffHand(), null);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        for (Check check : main.checks()) {
            if (!check.check(itemStack)) continue;
            if (!event.isCancelled()) event.setCancelled(true);
            GlobalUtils.log(Level.INFO, "Item %s failed the %s check and has been fixed.", itemStack, check.getClass().getSimpleName());
            check.fix(itemStack);
        }
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        main.checkFixItem(event.getMainHandItem(), event);
        main.checkFixItem(event.getOffHandItem(), event);
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        main.checkFixItem(event.getItem().getItemStack(), event);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand)) return;
        checkStand((ArmorStand) event.getRightClicked(), main);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        main.checkFixItem(event.getItem(), event);
    }
}
