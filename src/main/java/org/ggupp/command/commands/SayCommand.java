package org.ggupp.command.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.ggupp.command.BaseCommand;
import org.ggupp.util.GlobalUtils;

public class SayCommand extends BaseCommand {

    public SayCommand() {
        super("say", "/say <message>", "2d2dcore.command.say", "Configurable say command");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            TextComponent msg = GlobalUtils.translateChars(String.join(" ", args));
            Bukkit.getOnlinePlayers().forEach(p -> GlobalUtils.sendPrefixedComponent(p, msg));
            GlobalUtils.sendPrefixedComponent(Bukkit.getConsoleSender(), msg);
        } else sendErrorMessage(sender, "Message cannot be blank");
    }
}