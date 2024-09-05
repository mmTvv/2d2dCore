package org.ggupp.command.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.Localization;
import org.ggupp.command.BaseCommand;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.translateChars;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super("help", "/help", "2d2dcore.command.help", "Displays a custom help menu");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Localization loc = Localization.getLocalization(player.locale().getLanguage());
            TextComponent helpMsg = translateChars(String.join("\n", loc.getStringList("HelpMessage").toArray(String[]::new)));
            player.sendMessage(helpMsg);
        } else sendMessage(sender, "&dYou must be a player");
    }
}
