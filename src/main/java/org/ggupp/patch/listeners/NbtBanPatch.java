package org.ggupp.patch.listeners;

import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class NbtBanPatch implements Listener {
    private final JavaPlugin plugin;
    private final int MAX_ITEM_SIZE_BYTES;

    public NbtBanPatch(JavaPlugin plugin) {
        this.plugin = plugin;
        this.MAX_ITEM_SIZE_BYTES = plugin.getConfig().getInt("NbtBanItemChecker.maxItemSizeAllowed", 50000);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Iterate through the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {
            int itemSize;
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
                    player.getInventory().remove(item);
                    getLogger().warn("Cleared item in " + player.getName() + "'s inventory with size " + itemSize + " bytes named '" + getItemName(item) + "'");
                    sendPrefixedLocalizedMessage(player, "nbtPatch_deleted_item", getItemName(item));
                }
            }
        }
    }

    // Process box and its contents
    public static int processContainerItem(ItemStack containerItem) {
        int totalSize = 0;

        BlockStateMeta blockStateMeta = (BlockStateMeta) containerItem.getItemMeta();
        if (blockStateMeta != null && blockStateMeta.getBlockState() instanceof Container) {
            Container container = (Container) blockStateMeta.getBlockState();
            Inventory containerInventory = container.getInventory();

            // Iterate through the shulker box's inventory
            for (ItemStack item : containerInventory.getContents()) {
                if (item != null) {
                    if (
                            item.getType().toString().endsWith("SHULKER_BOX") ||
                            item.getType().toString().endsWith("CHEST") ||
                            item.getType().toString().endsWith("TRAPPED_CHEST") ||
                            item.getType().toString().endsWith("BARREL")
                    ) {
                        totalSize += processContainerItem(item);
                    } else {
                        // Calculate the size of the item's metadata
                        totalSize += calculateStringSizeInBytes(item.toString());

                    }
                }
            }
        }
        return totalSize;
    }

    // Method to calculate the size of the given string in bytes
    public static int calculateStringSizeInBytes(String data) {
        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        return byteArray.length;
    }

    public static String getItemName(ItemStack itemStack) {
        if (itemStack == null) {
            return "";
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            return String.valueOf(itemStack.getItemMeta().getDisplayName());
        } else {
            return itemStack.getType().toString().replace("_", " ").toLowerCase();
        }
    }
}
