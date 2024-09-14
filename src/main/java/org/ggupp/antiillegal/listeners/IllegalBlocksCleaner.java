package org.ggupp.antiillegal.listeners;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.block.Block;


public class IllegalBlocksCleaner implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        Chunk chunk = event.getChunk();

        int yUpperLimit = event.getWorld().getMaxHeight();
        int yLowerLimit = event.getWorld().getMinHeight() + 5;

        if(event.getWorld().getEnvironment() == World.Environment.NETHER){
            yUpperLimit = 125;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = yLowerLimit; y < yUpperLimit; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (
                            block.getType() == Material.BEDROCK ||
                            block.getType() == Material.END_PORTAL_FRAME ||
                            block.getType() == Material.REINFORCED_DEEPSLATE ||
                            block.getType() == Material.BARRIER
                    ) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}