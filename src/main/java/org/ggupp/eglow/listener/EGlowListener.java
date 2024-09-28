package org.ggupp.eglow.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ggupp.eglow.EGlowSection;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowPlayer;

import java.util.UUID;

@RequiredArgsConstructor
public class EGlowListener implements Listener {
    private final EGlowSection main;

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        playerConnect(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerKickedEvent(PlayerKickEvent event) {
        playerDisconnect(event.getPlayer());
    }

    @EventHandler
    public void PlayerDisconnectEvent(PlayerQuitEvent event) {
        playerDisconnect(event.getPlayer());
    }

    public void playerDisconnect(Player player) {
        EGlowPlayer eGlowPlayer = DataManager.getEGlowPlayer(player);

        if (eGlowPlayer == null)
            return;


        Bukkit.getRegionScheduler().run(main.plugin(), player.getLocation(), (o) -> {
            if (eGlowPlayer.getGlowEffect() != null && player.hasPermission("2d2dcore.eglow.on_join_glow")) main.storage().save(eGlowPlayer);
            if(eGlowPlayer.getGlowEffect() != null) eGlowPlayer.getGlowEffect().removePlayer(eGlowPlayer);
            DataManager.removeEGlowPlayer(eGlowPlayer.getPlayer());
        });
    }

    public void playerConnect(Player player, UUID uuid){
        if(player.isGlowing()){
            player.setGlowing(false);
        }

        EGlowPlayer eGlowPlayer = DataManager.addPlayer(player, uuid.toString());

        if(player.hasPermission("2d2dcore.eglow.on_join_glow")) {
            Bukkit.getRegionScheduler().run(main.plugin(), player.getLocation(), (o) -> {
                main.storage().load(eGlowPlayer);
                if (eGlowPlayer.getGlowEffect() != null) {
                    if (main.config().getBoolean("Eglow.enableEffectOnJoin")) {
                        eGlowPlayer.activateGlow();
                    }
                }
            });
        }
        //eGlowPlayer.setEffect(DataManager.getEGlowEffect("rainbowfast"));
    }
}
