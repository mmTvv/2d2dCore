package org.ggupp.chat.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.chat.ChatInfo;
import org.ggupp.chat.ChatSection;
import org.jetbrains.annotations.NotNull;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class IgnoreCommand implements CommandExecutor {
    private final ChatSection manager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                ChatInfo info = manager.getInfo((Player) sender);
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!info.isIgnoring(target.getUniqueId())) {
                    info.ignorePlayer(target.getUniqueId());
                    sendPrefixedLocalizedMessage(player, "ignore_successful", target.getName());
                } else sendPrefixedLocalizedMessage(player, "already_ignoring");
            } else sendPrefixedLocalizedMessage(player, "ignore_command_syntax");
        } else sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
