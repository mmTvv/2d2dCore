package org.ggupp.dupe.zombiedupe;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class ZombieDupe implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> lastDuplicationTimes = new HashMap<>();

    public ZombieDupe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityHitByArrow(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();

            if (event.getEntity() instanceof Zombie) {
                Zombie zombie = (Zombie) event.getEntity();

                if (arrow.getShooter() instanceof Player) {
                    Player player = (Player) arrow.getShooter();

                    final boolean ENABLED = plugin.getConfig().getBoolean("ZombieDupe.enabled", true);
                    if (!ENABLED) return;

                    if (player.isGliding()) {

                        zombie.damage(0, player);
                        event.setDamage(0);
                        arrow.remove();

                        final int PROBABILITY_PERCENTAGE = plugin.getConfig().getInt("ZombieDupe.probabilityPercentage", 100);
                        int randomSuccess = random.nextInt(100);
                        if (randomSuccess >= PROBABILITY_PERCENTAGE) return;

                        Block block = zombie.getLocation().getBlock();
                        UUID chunkId = getChunkId(block);
                        final long DUPLICATION_INTERVAL = plugin.getConfig().getLong("ZombieDupe.dupeCooldown", 200L);

                        if (System.currentTimeMillis() - lastDuplicationTimes.getOrDefault(chunkId, 0L) < DUPLICATION_INTERVAL) {
                            sendPrefixedLocalizedMessage(player, "framedupe_cooldown");
                            return;
                        }

                        final int MAX_ITEMS_IN_CHUNK = plugin.getConfig().getInt("ZombieDupe.limitItemsPerChunk", 18);
                        if (getItemCountInChunk(block) >= MAX_ITEMS_IN_CHUNK) {
                            sendPrefixedLocalizedMessage(player, "framedupe_items_limit");
                            return;
                        }

                        ItemStack itemInHand = zombie.getEquipment().getItemInMainHand();
                        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
                            ItemStack duplicateItem = itemInHand.clone();
                            zombie.getWorld().dropItemNaturally(zombie.getLocation(), duplicateItem);

                            final int ROTTEN_FLESH_DROP_PERCENTAGE = plugin.getConfig().getInt("ZombieDupe.rottenFleshDropPercentage", 50);
                            if (random.nextInt(100) < ROTTEN_FLESH_DROP_PERCENTAGE) {
                                ItemStack rottenFlesh = new ItemStack(Material.ROTTEN_FLESH, 1);
                                zombie.getWorld().dropItemNaturally(zombie.getLocation(), rottenFlesh);
                            }

                            lastDuplicationTimes.put(chunkId, System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }

    private UUID getChunkId(Block block) {
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
