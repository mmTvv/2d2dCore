package org.ggupp.chat.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.chat.ChatCommand;
import org.ggupp.chat.ChatInfo;
import org.ggupp.chat.ChatSection;
import org.jetbrains.annotations.NotNull;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class ReplyCommand extends ChatCommand {
    private final ChatSection manager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                ChatInfo senderInfo = manager.getInfo(player);
                if (senderInfo.getReplyTarget() != null) {
                    Player target = senderInfo.getReplyTarget();
                    if (target.isOnline()) {
                        ChatInfo targetInfo = manager.getInfo(target);
                        String msg = ChatColor.stripColor(String.join(" ", args));
                        sendWhisper(player, senderInfo, target, targetInfo, msg);
                    } else sendPrefixedLocalizedMessage(player, "reply_player_offline", target.getName());
                } else sendPrefixedLocalizedMessage(player, "reply_no_target");
            } else sendPrefixedLocalizedMessage(player, "reply_command_syntax");
        } else sendMessage(sender, "&dYou must be a player");
        return true;
    }
}
