package org.ggupp.command.commands;


import org.ggupp.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class OpenInv extends BaseCommand {
    public OpenInv() {
        super(
                "open",
                "/open <inv | ender> <player>",
                "2d2dcore.command.openinv",
                "Open peoples inventories",
                new String[]{"inv::Open the inventory of the specified player", "ender::Open the ender chest of the specified player"}
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender).orElse(null);
        if (player != null) {
            if (args.length < 2) {
                sendErrorMessage(sender, getUsage());
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendMessage(sender, "&dPlayer&r&5 %s&r&5 not online&r", args[1]);
                    return;
                }
                switch (args[0]) {
                    case "ender":
                        player.openInventory(target.getEnderChest());
                        break;
                    case "inv":
                    case "inventory":
                        player.openInventory(target.getInventory());
                        break;
                    default:
                        sendErrorMessage(sender, "Unknown argument " + args[0]);
                }
            }
        } else sendErrorMessage(sender, PLAYER_ONLY);
    }
}
