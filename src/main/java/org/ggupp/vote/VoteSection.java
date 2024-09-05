package org.ggupp.vote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.util.GlobalUtils;
import org.ggupp.vote.command.VoteCommand;
import org.ggupp.vote.io.VoteIO;
import org.ggupp.vote.listeners.VoteListener;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.executeCommand;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class VoteSection implements Section {
    private final Main plugin;
    private VoteIO io;
    @Getter private HashMap<String, Integer> toReward;
    @Getter private ConfigurationSection config;

    @Override
    public void enable() {
        config = plugin.getSectionConfig(this);
        plugin.getCommand("vote").setExecutor(new VoteCommand());
        try {
            File votesFile = new File(plugin.getSectionDataFolder(this), "OfflineVotes.json");
            if (!votesFile.exists()) votesFile.createNewFile();
            io = new VoteIO(votesFile);
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "Failed to create OfflineVotes.json. Please see stacktrace below for more info");
            t.printStackTrace();
        }
        toReward = io.load();
        plugin.register(new VoteListener(this));
    }

    @Override
    public void disable() {
        io.save(toReward);
        toReward.clear();
    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public String getName() {
        return "Vote";
    }

    public void rewardPlayer(Player player) {
        if (!player.hasPermission("Leee.ic")) {
            executeCommand("lp user %s group add voter", player.getName());
        } else {
            executeCommand("lp user %s group remove voter", player.getName());
        }
        executeCommand("lp user %s group remove default", player.getName());
        //int maxHomes = ((HomeManager) plugin.getSectionByName("Home")).getMaxHomes(player);
        //if ((maxHomes + 2) >= config.getInt("MaxHomesByVoting")) return;
        sendPrefixedLocalizedMessage(player, "vote_thanks");
        //executeCommand(config.getString("AddHomePermission"), (maxHomes + 2));
    }

    public void registerOfflineVote(String username) {
        toReward.putIfAbsent(username, 1);
        toReward.computeIfPresent(username, (n, votes) -> votes + 1);
    }
    public void markAsRewarded(String username) {
        toReward.remove(username);
    }
    public Optional<Integer> getToRewardEntry(Player player) {
        return toReward.containsKey(player.getName()) ? Optional.of(toReward.get(player.getName())) : Optional.empty();
    }
}
