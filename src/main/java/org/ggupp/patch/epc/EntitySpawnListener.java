package org.ggupp.patch.epc;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.ggupp.patch.PatchSection;

import java.util.Arrays;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;

@RequiredArgsConstructor
public class EntitySpawnListener implements Listener {
    private final PatchSection main;

    @EventHandler
    public void onEntitySpawn(EntityAddToWorldEvent event) {
        Entity entity = event.getEntity();
        Chunk chunk = entity.getChunk();
        if (!main.entityPerChunk().containsKey(entity.getType())) return;
        int amt = enumerate(entity.getLocation().getChunk(), entity.getType());
        int max = main.entityPerChunk().get(entity.getType());
        if (amt >= max) {
            entity.getScheduler().run(main.plugin(), (t) -> entity.remove(), () -> {});
            log(Level.INFO, "Prevented %s from spawning (%d/%d) in chunk %d,%d in world %s", entity.getType().toString().toLowerCase(), amt, max, chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
        }
    }

    private int enumerate(Chunk chunk, EntityType entityType) {
        return (int) Arrays.stream(chunk.getEntities()).filter(e -> e.getType() == entityType).count();
    }
}
