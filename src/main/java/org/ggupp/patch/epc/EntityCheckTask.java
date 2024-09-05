package org.ggupp.patch.epc;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.ggupp.patch.PatchSection;

import java.util.Arrays;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;

@RequiredArgsConstructor
public class EntityCheckTask implements Runnable {
    private final PatchSection main;

    @Override
    public void run() {
        Bukkit.getScheduler().runTask(main.plugin(), () -> {
            for (Chunk[] chunks : Bukkit.getWorlds().stream().map(World::getLoadedChunks).toList()) {
                for (Chunk chunk : chunks) {
                    if (chunk.getEntities().length == 0) continue;
                    main.entityPerChunk().forEach((e, i) -> {
                        Entity[] entities = Arrays.stream(chunk.getEntities()).filter(en -> en.getType() == e).toList().toArray(Entity[]::new);
                        int amt = entities.length;
                        if (amt >= i) {
                            log(Level.INFO, "Removed %d entities from chunk %d,%d in world %s", amt-i, chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
                            for (int j = 0; j < amt-i; j++) {
                                Entity entity = entities[j];
                                // Schedule the removal of each entity on the main thread
                                Bukkit.getScheduler().runTask(main.plugin(), entity::remove);
                            }
                        }
                    });
                }
            }
        });
    }
}
