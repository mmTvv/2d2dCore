package org.ggupp.command.commands;

import org.ggupp.command.BaseCommand;
import org.ggupp.command.CommandSection;
import org.bukkit.command.CommandSender;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class DiscordCommand extends BaseCommand {
    private final CommandSection main;

    public DiscordCommand(CommandSection main) {
        super(
                "discord",
                "/discord",
                "2d2dcore.command.discord",
                "Shows a discord link");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendMessage(sender, main.getConfig().getString("Discord"));
    }
}
