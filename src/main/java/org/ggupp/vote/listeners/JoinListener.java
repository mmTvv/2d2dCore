package org.ggupp.vote.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ggupp.vote.VoteSection;


@RequiredArgsConstructor
public class JoinListener implements Listener {
    private final VoteSection main;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        main.getToRewardEntry(player).ifPresent(i -> {
            for (int j = 0; j < i; j++) main.rewardPlayer(player);
        });
        /**main.markAsRewarded(player.getName());*/
    }
}
