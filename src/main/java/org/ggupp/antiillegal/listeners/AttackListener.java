package org.ggupp.antiillegal.listeners;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import static org.apache.logging.log4j.LogManager.getLogger;
import java.nio.charset.StandardCharsets;

public class AttackListener implements Listener {
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getDamage() > 30D) {
            event.setCancelled(true);
            ((Player) event.getDamager()).damage(event.getDamage());
        }
    }
}
