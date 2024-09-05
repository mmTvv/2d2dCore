package org.ggupp.tablist.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ggupp.tablist.TabSection;


@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    private final TabSection main;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        main.setTab(event.getPlayer());
    }
}
