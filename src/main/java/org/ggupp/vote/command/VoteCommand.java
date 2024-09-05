package org.ggupp.vote.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;


public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            sendPrefixedLocalizedMessage(player, "vote_info");
        } else sender.sendMessage("This command is player only");
        return true;
    }
}
