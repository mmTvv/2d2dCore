package org.ggupp.chat.commands;

import org.ggupp.chat.ChatInfo;
import org.ggupp.chat.ChatSection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class IgnoreListCommand implements CommandExecutor {
    private final ChatSection manager;

    public IgnoreListCommand(ChatSection manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ChatInfo info = manager.getInfo(player);
            if (info != null) {
                if (!info.getIgnoring().isEmpty()) {
                    String ignoredList = info.getIgnoring().stream()
                            .map(uuid -> Bukkit.getServer().getOfflinePlayer(uuid).getName())
                            .filter(name -> name != null && !name.isEmpty())
                            .collect(Collectors.joining("&d, &5"));
                    sendPrefixedLocalizedMessage(player, "ignorelist_successful", ignoredList);
                } else sendPrefixedLocalizedMessage(player, "ignorelist_not_ignoring");
            } else sendPrefixedLocalizedMessage(player, "ignorelist_failed");
        } else sendMessage(sender, "You must be a player to use this command.");
        return true;
    }
}
