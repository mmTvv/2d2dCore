package org.ggupp.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.util.GlobalUtils;

import java.util.Optional;

import static org.ggupp.util.GlobalUtils.sendMessage;

@Getter
public abstract class BaseCommand {
    protected final String CONSOLE_ONLY = "This command is console only";
    protected final String PLAYER_ONLY = "This command is player only";
    protected final String PREFIX = GlobalUtils.getPREFIX();
    private final String name;
    private final String usage;
    private final String permission;
    private final String description;
    private final String[] subCommands;

    public BaseCommand(String name, String usage, String permission) {
        this(name, usage, permission, null, null);
    }

    public BaseCommand(String name, String usage, String permission, String description) {
        this(name, usage, permission, description, null);
    }

    public BaseCommand(String name, String usage, String permission, String description, String[] subCommands) {
        this.name = name;
        this.usage = usage;
        this.permission = permission;
        this.description = description;
        this.subCommands = subCommands;
    }
    public void sendNoPermission(CommandSender sender) {
        sendMessage(sender, "&5You are lacking the permission&r&d %s", getPermission());
    }

    public void sendErrorMessage(CommandSender sender, String message) {
        sendMessage(sender, String.format("&5%s", message));
    }

    public Optional<Player> getSenderAsPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return Optional.of((Player) sender);
        } else return Optional.empty();
    }

    public abstract void execute(CommandSender sender, String[] args);
}