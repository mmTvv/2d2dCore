package org.ggupp.chat.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class OpWhiteListListener implements Listener {
    private final JavaPlugin plugin;

    public OpWhiteListListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkOps(event.getPlayer());
    }

    @EventHandler
    public void onCommandEvent(PlayerCommandPreprocessEvent event) {
        if(checkOps(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractionEvent(PlayerInteractEvent event) {
        if(checkOps(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        checkOps(event.getPlayer());
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(checkOps(player)){
            event.setCancelled(true);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if(checkOps(player)){
            if(event.getNewGameMode() == GameMode.CREATIVE){
                event.setCancelled(true);
            }
        }
    }

    public boolean checkOps(Player player) {
        List<String> opUsersWhiteList = plugin.getConfig().getStringList("OpUsersWhiteList");

        if ((player.isOp() || player.getGameMode() == GameMode.CREATIVE || player.hasPermission("*")) && !opUsersWhiteList.contains(player.getName())) {
            player.setOp(false);
            sendPrefixedLocalizedMessage(player, "op_not_allowed");
            log(Level.SEVERE, "The player %s has had operator permissions revoked because it is not allowed.", player.getName());
            return true;
        }
        return false;
    }
}
