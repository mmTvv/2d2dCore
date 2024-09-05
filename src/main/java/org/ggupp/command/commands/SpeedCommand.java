package org.ggupp.command.commands;

import org.bukkit.command.CommandSender;
import org.ggupp.command.BaseCommand;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand() {
        super(
                "speed",
                "/speed <number>",
                "2d2dcore.command.speed",
                "Turn up your fly speed");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getSenderAsPlayer(sender).ifPresentOrElse(player -> {
            try {
                if (args.length > 0) {
                    float speed = Float.parseFloat(args[0]);
                    if (speed <= 1) {
                        player.setFlySpeed(speed);
                        sendMessage(player, "&dFly speed set to&r&a %f", speed);
                    } else sendMessage(player, "Flying speed must not be above 1");
                } else {
                    sendMessage(sender, "&dPlease note that the default flight speed is&r&a 0.1");
                    sendErrorMessage(sender, getUsage());
                }
            } catch (NumberFormatException e) {
                sendErrorMessage(player, getUsage());
            }
        }, () -> sendErrorMessage(sender, PLAYER_ONLY));
    }
}
