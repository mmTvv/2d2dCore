package org.ggupp.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.ggupp.command.BaseTabCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class SpawnCommand extends BaseTabCommand {
    private final List<String> entityTypes;

    public SpawnCommand() {
        super("spawn", "/spawn <EntityType> <amount>", "2d2dcore.command.spawnshit");
        entityTypes = getEntityTypes();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getSenderAsPlayer(sender).ifPresentOrElse(player -> {
            if (args.length >= 1 && args.length <= 2) {
                try {
                    if (entityTypes.contains(args[0])) {
                        int count = (args.length == 2) ? Integer.parseInt(args[1]) : 1;
                        EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
                        for (int i = 0; i < count; i++) player.getWorld().spawnEntity(player.getLocation(), entityType);
                    } else sendMessage(sender, "&5Invalid entity&r&a %s", args[0]);
                } catch (NumberFormatException e) {
                    sendErrorMessage(sender, "Invalid argument type the argument " + args[0] + " must be a number");
                }
            } else sendErrorMessage(sender, getUsage());
        }, () -> sendMessage(sender, "&c%s", PLAYER_ONLY));

    }

    public List<String> getEntityTypes() {
        List<String> entityTypes = new ArrayList<>();
        for (EntityType entityType : EntityType.values()) {
            entityTypes.add(entityType.toString().toLowerCase());
        }
        return entityTypes;
    }

    @Override
    public List<String> onTab(String[] args) {
        if (args.length == 0) return entityTypes;
        return entityTypes.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
    }
}
