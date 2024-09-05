package org.ggupp.tpa.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.tpa.TPASection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;


@RequiredArgsConstructor
public class TPADenyCommand implements CommandExecutor {
    private final TPASection main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player requested) {
            if (args.length == 0) {
                Player requester = main.getLastRequest(requested);
                denyTPA(requested, requester);
            } else if (args.length == 1) {
                Player requester = main.hasRequested(Bukkit.getPlayer(args[0]), requested) ? Bukkit.getPlayer(args[0]) : null;
                denyTPA(requested, requester);
            } else sendPrefixedLocalizedMessage(requested, "tpa_syntax");
        } else sendMessage(sender, "&cYou must be a player");
        return true;
    }

    private void denyTPA(Player requested, @Nullable Player requester) {
        if (requester == null) {
            sendPrefixedLocalizedMessage(requested, "tpa_no_request_found");
            return;
        }

        sendPrefixedLocalizedMessage(requester, "tpa_request_denied_from", requested.getName());
        sendPrefixedLocalizedMessage(requested, "tpa_request_denied_to", requester.getName());
        main.removeRequest(requester, requested);
    }
}
