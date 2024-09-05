package org.ggupp.tpa.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.ggupp.tpa.TPASection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;


@RequiredArgsConstructor
public class TPAAcceptCommand implements CommandExecutor {
    private final TPASection main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player requested) {
            if (args.length == 0) {
                Player requester = main.getLastRequest(requested);
                acceptTPA(requested, requester);
            } else if (args.length == 1) {
                Player requester = main.hasRequested(Bukkit.getPlayer(args[0]), requested) ? Bukkit.getPlayer(args[0]) : null;
                acceptTPA(requested, requester);
            } else sendPrefixedLocalizedMessage(requested, "tpa_syntax");
        } else sendMessage(sender, "&dYou must be a player");
        return true;
    }

    private void acceptTPA(Player requested, @Nullable Player requester) {
        if (requester == null) {
            sendPrefixedLocalizedMessage(requested, "tpa_no_request_found");
            return;
        }

        requester.teleportAsync(requested.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        sendPrefixedLocalizedMessage(requester, "tpa_teleporting");
        sendPrefixedLocalizedMessage(requested, "tpa_teleporting");
        main.removeRequest(requester, requested);
    }
}
