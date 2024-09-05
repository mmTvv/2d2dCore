package org.ggupp.vote.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.ggupp.util.GlobalUtils;
import org.ggupp.vote.VoteSection;


@RequiredArgsConstructor
public class VoteListener implements Listener {
    private final VoteSection main;

    @EventHandler
    public void onVote(VotifierEvent event) {
        String username = event.getVote().getUsername();
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(username)).forEach(p -> GlobalUtils.sendPrefixedLocalizedMessage(p, "vote_announcement", username));
        Player player = Bukkit.getPlayer(username);
        if (player == null) {
            main.registerOfflineVote(username);
            return;
        }
        main.rewardPlayer(player);
    }
}
