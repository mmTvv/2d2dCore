package org.ggupp.patch.epc;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;


public class ChestLimiter implements Listener {
    final int MAX_CHEST_PER_CHUNK;

    public ChestLimiter(JavaPlugin plugin) {
        this.MAX_CHEST_PER_CHUNK = plugin.getConfig().getInt("Patch.ChestLimitPerChunk", 192);
    }

    @EventHandler
    public void onEntitySpawn(BlockPlaceEvent event) {

        Block bloque = event.getBlockPlaced();
        if(bloque.getType() == Material.CHEST || bloque.getType() == Material.TRAPPED_CHEST || bloque.getType() == Material.BARREL){
            Chunk chunk = bloque.getChunk();

            int yUpperLimit = chunk.getWorld().getMaxHeight();
            int yLowerLimit = chunk.getWorld().getMinHeight() + 5;
            if(chunk.getWorld().getEnvironment() == World.Environment.NETHER){
                yUpperLimit = 125;
            }

            int chestCount = 0;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = yLowerLimit; y < yUpperLimit; y++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.BARREL) {
                            chestCount++;
                        }
                    }
                }
            }

            if(chestCount >= MAX_CHEST_PER_CHUNK){
                event.setCancelled(true);
            }
        }
    }

//    @EventHandler
//    public void onChunkLoad(ChunkLoadEvent event) {

//        if(event.isNewChunk()) return;
//        Chunk chunk = event.getChunk();

//        int yUpperLimit = event.getWorld().getMaxHeight();
//        int yLowerLimit = event.getWorld().getMinHeight() + 5;

//        if(event.getWorld().getEnvironment() == World.Environment.NETHER){
//            yUpperLimit = 125;
//        }

//        int chestCount = 0;

//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                for (int y = yLowerLimit; y < yUpperLimit; y++) {
//                    Block block = chunk.getBlock(x, y, z);
//
//                    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.BARREL) {
//                        chestCount++;
//                        if(chestCount >= MAX_CHEST_PER_CHUNK) {
//                            block.setType(Material.AIR);
//                        }
//                    }
//                }
//            }
//        }
//
//        if(chestCount >= MAX_CHEST_PER_CHUNK) {
//            Collection<Player> players = chunk.getWorld().getPlayers();
//
//            List<Player> playersInChunk = players.stream()
//                    .filter(player -> player.getLocation().getChunk().equals(chunk))
//                    .toList();
//
//            for (Player player : playersInChunk) {
//                sendPrefixedLocalizedMessage(player, "chestlimiter_deleted_chest", chestCount - MAX_CHEST_PER_CHUNK, MAX_CHEST_PER_CHUNK);
//            }
//        }
//    }
}
