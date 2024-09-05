package org.ggupp.command.commands;

import org.bukkit.command.CommandSender;
import org.ggupp.Main;
import org.ggupp.command.BaseCommand;
import org.ggupp.tablist.util.Utils;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class UptimeCommand extends BaseCommand {
    private final Main plugin;
    public UptimeCommand(Main plugin) {
        super(
                "uptime",
                "/uptime",
                "2d2dcore.command.uptime",
                "Show the uptime of the server");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendMessage(sender, "&dThe server has had&r&a %s&r&3 uptime", Utils.getFormattedInterval(System.currentTimeMillis() - plugin.getStartTime()));
    }
}
