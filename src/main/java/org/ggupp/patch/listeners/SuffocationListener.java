package org.ggupp.patch.listeners;

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class SuffocationListener implements Listener {

    @EventHandler
    public void onPlayerSuffocation(EntityDamageEvent event) {

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.getCause() == DamageCause.SUFFOCATION) {

                Block block = player.getLocation().getBlock();
                Material blockType = block.getType();

                if (blockType == Material.OBSIDIAN || blockType == Material.BEDROCK || blockType == Material.NETHERITE_BLOCK || blockType == Material.BARRIER  || blockType == Material.END_PORTAL_FRAME) {
                    event.setDamage(7.0);
                }
            }
        }
    }
}