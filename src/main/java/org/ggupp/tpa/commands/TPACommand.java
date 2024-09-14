package org.ggupp.tpa.commands;

import lombok.RequiredArgsConstructor;
import org.ggupp.Localization;
import org.ggupp.tpa.TPASection;
import org.ggupp.util.GlobalUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.ggupp.util.GlobalUtils.*;


@RequiredArgsConstructor
public class TPACommand implements CommandExecutor {
    private final TPASection main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player from) {

            if (args.length == 1) {
                Player to = Bukkit.getPlayer(args[0]);
                if (to == null) {
                    sendPrefixedLocalizedMessage(from, "tpa_player_not_online", args[0]);
                    return true;
                }
                if (to == from) {
                    sendPrefixedLocalizedMessage(from, "tpa_self_tpa");
                    return true;
                }

                TextComponent acceptButton = Component.text("ACCEPT").clickEvent(ClickEvent.runCommand("/tpayes " + from.getName()));
                TextComponent denyButton = Component.text("DENY").clickEvent(ClickEvent.runCommand("/tpano " + from.getName()));
                TextReplacementConfig acceptReplace = TextReplacementConfig.builder().match("accept").replacement(acceptButton).build();
                TextReplacementConfig denyReplace = TextReplacementConfig.builder().match("deny").replacement(denyButton).build();

                Localization loc = Localization.getLocalization(to.locale().getLanguage());
                String str = String.format(loc.get("tpa_request_received"), from.getName(), "accept", "deny");
                TextComponent component = (TextComponent) GlobalUtils.translateChars(str).replaceText(acceptReplace).replaceText(denyReplace);
                if (main.hasRequested(from, to)) {
                    sendPrefixedLocalizedMessage(from, "tpa_already_sent", to.getName());
                    return true;
                } else {
                    sendPrefixedComponent(to, component);
                    sendPrefixedLocalizedMessage(from, "tpa_request_sent", to.getName());
                    main.registerRequest(from, to);
                }
            } else sendPrefixedLocalizedMessage(from, "tpa_syntax");
        } else sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
