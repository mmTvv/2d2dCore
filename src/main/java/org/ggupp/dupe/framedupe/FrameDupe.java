package org.ggupp.dupe.framedupe;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class FrameDupe implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Long> lastDuplicationTimes = new HashMap<>(); // Map to track last duplication time for each item frame
    private final Map<UUID, Integer> itemCounts = new HashMap<>(); // Map to track item counts per chunk

    public FrameDupe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFrameInteract(EntityDamageByEntityEvent event) {

        final boolean ENABLED = plugin.getConfig().getBoolean("FrameDupe.enabled", true);
        final long DUPLICATION_INTERVAL = plugin.getConfig().getLong("FrameDupe.dupeCooldown", 200L);
        final int MAX_ITEMS_IN_CHUNK = plugin.getConfig().getInt("FrameDupe.limitItemsPerChunk", 18);
        final int PROBABILITY_PERCENTAGE = plugin.getConfig().getInt("FrameDupe.probabilityPercentage", 100);

        if (!(event.getEntity() instanceof ItemFrame)) return;

        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) return;

        Player player = (Player) damager;

        if(!ENABLED){
            return;
        }

        int randomSuccess = (int)Math.round(Math.random() * 100);
        if (!(randomSuccess <= PROBABILITY_PERCENTAGE)) {
            return;
        }

        ItemFrame itemFrame = (ItemFrame) event.getEntity();
        UUID frameId = itemFrame.getUniqueId();
        Block block = itemFrame.getLocation().getBlock();
        UUID chunkId = getChunkId(block);


        if (System.currentTimeMillis() - lastDuplicationTimes.getOrDefault(chunkId /*frameId*/, 0L) < DUPLICATION_INTERVAL) {
            sendPrefixedLocalizedMessage(player, "framedupe_cooldown");
            return;
        }

        // Check item count in the chunk
        if (getItemCountInChunk(block) >= MAX_ITEMS_IN_CHUNK) {
            sendPrefixedLocalizedMessage(player, "framedupe_items_limit");
            return; // Stop duplicating if there are too many items in the chunk
        }

        // Duplicate item
        ItemStack itemStack = itemFrame.getItem();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            itemFrame.getWorld().dropItemNaturally(itemFrame.getLocation(), itemStack);
        }

        // Update duplication time
        lastDuplicationTimes.put(chunkId /*frameId*/, System.currentTimeMillis());
    }

    private UUID getChunkId(Block block) {
        // Generate a unique ID for the chunk based on its coordinates
        int x = block.getChunk().getX();
        int z = block.getChunk().getZ();

        return UUID.nameUUIDFromBytes((x + ":" + z).getBytes());
    }

    private int getItemCountInChunk(Block block) {
        return (int) Arrays.stream(block.getChunk().getEntities())
                .filter(entity -> entity instanceof Item)
                .map(entity -> (Item) entity)
                .count();
    }
}
