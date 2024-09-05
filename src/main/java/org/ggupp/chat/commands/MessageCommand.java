package org.ggupp.chat.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.chat.ChatCommand;
import org.ggupp.chat.ChatInfo;
import org.ggupp.chat.ChatSection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class MessageCommand extends ChatCommand {
    private final ChatSection manager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(target)) {
                    ChatInfo senderInfo = manager.getInfo(player);
                    ChatInfo targetInfo = manager.getInfo(target);
                    String msg = ChatColor.stripColor(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                    sendWhisper(player, senderInfo, target, targetInfo, msg);
                } else sendPrefixedLocalizedMessage(player, "msg_could_not_find_player", args[0]);
            } else sendPrefixedLocalizedMessage(player, "msg_command_syntax");
        } else sendMessage(sender, "&dYou must be a player");
        return true;
    }
}
