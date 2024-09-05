package org.ggupp.chat.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ggupp.chat.ChatSection;

@RequiredArgsConstructor
public class JoinLeaveListener implements Listener {
    private final ChatSection manager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.registerPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        manager.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        manager.removePlayer(event.getPlayer());
    }
}
