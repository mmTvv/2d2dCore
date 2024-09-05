package org.ggupp.antiillegal.listeners;

import io.papermc.paper.event.block.BlockPreDispenseEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.ggupp.antiillegal.AntiIllegalMain;

import static org.ggupp.antiillegal.util.Utils.checkStand;

@RequiredArgsConstructor
public class MiscListeners implements Listener {
    private final AntiIllegalMain main;

    @EventHandler
    public void onDispenser(BlockPreDispenseEvent event) {
        main.checkFixItem(event.getItemStack(), event);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk()) return;
        Entity[] entities = event.getChunk().getEntities();
        for (Entity entity : entities) {
            if (entity instanceof ItemFrame frame) {
                main.checkFixItem(frame.getItem(), null);
            } else if (entity instanceof ArmorStand stand) {
                checkStand(stand, main);
            }
        }
    }
}
