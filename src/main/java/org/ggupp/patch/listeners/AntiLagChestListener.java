package org.ggupp.patch.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.ggupp.patch.listeners.NbtBanPatch.getItemName;
import static org.ggupp.patch.listeners.NbtBanPatch.calculateStringSizeInBytes;
import static org.ggupp.patch.listeners.NbtBanPatch.processContainerItem;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;
import static org.apache.logging.log4j.LogManager.getLogger;


public class AntiLagChestListener implements Listener {
    private final int MAX_ITEM_SIZE_BYTES;
    private final JavaPlugin plugin;
    private final Map<UUID, Long> lastOpenTimes = new HashMap<>(); // Map to track last chest open time for each player

    public AntiLagChestListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.MAX_ITEM_SIZE_BYTES = plugin.getConfig().getInt("AntiLagChest.maxItemSizeAllowed", 50000);
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!isCheckedInventory(event.getInventory().getType())) return;

        Player player = (Player) event.getPlayer();

        final boolean ENABLED = plugin.getConfig().getBoolean("AntiLagChest.enabled", true);
        final long OPEN_COOLDOWN = plugin.getConfig().getLong("AntiLagChest.openCooldown", 1000L); // Default one second

        if (!ENABLED) return;

        if (System.currentTimeMillis() - lastOpenTimes.getOrDefault(player.getUniqueId(), 0L) < OPEN_COOLDOWN) {
            event.setCancelled(true);
            sendPrefixedLocalizedMessage(player, "chest_cooldown");
            return;
        }

        // Update last open time
        lastOpenTimes.put(player.getUniqueId(), System.currentTimeMillis());

        // Optional: Access and iterate through the chest inventory
        Inventory inventory = event.getInventory();

        for (ItemStack item : inventory.getContents()) {
            int itemSize = 0;
            if (item != null) {

                if (item.getType().toString().endsWith("SHULKER_BOX") ||
                        item.getType().toString().endsWith("CHEST") ||
                        item.getType().toString().endsWith("TRAPPED_CHEST") ||
                        item.getType().toString().endsWith("BARREL")) {

                    itemSize = processContainerItem(item);
                } else {

                    itemSize = calculateStringSizeInBytes(item.toString());
                }
                if (itemSize > MAX_ITEM_SIZE_BYTES) {
                    // Clear the item
                    inventory.remove(item);
                    getLogger().warn("Cleared a " + getItemName(item) + " with a size of " + itemSize + " bytes from a " + inventory.getType() + " triggered by " + player.getName());
                    sendPrefixedLocalizedMessage(player, "nbtPatch_deleted_item", getItemName(item));
                }
            }

        }
    }

    private boolean isCheckedInventory(InventoryType type) {
        return switch (type) {
            case CHEST, HOPPER, SHULKER_BOX, DISPENSER, DROPPER -> true;
            default -> false;
        };
    }
}
