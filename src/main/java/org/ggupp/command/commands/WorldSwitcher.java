package org.ggupp.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.command.BaseCommand;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class WorldSwitcher extends BaseCommand {

    public WorldSwitcher() {
        super(
                "world",
                "/world <worldName>",
                "2d2dcore.command.world",
                "Switch worlds",
                new String[]{"overworld::Teleport your self to the overworld", "nether::Teleport your self to the nether", "end::Teleport your self to the end"});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender).orElse(null);
        if (player != null) {
            if (args.length > 0) {
                String worldName = Bukkit.getWorlds().get(0).getName();
                double x = player.getLocation().getX(), y = player.getLocation().getY(), z = player.getLocation().getZ();
                switch (args[0]) {
                    case "overworld":
                        World overWorld = Bukkit.getWorld(worldName);
                        player.teleportAsync(new Location(overWorld, x, y, z));
                        sendMessage(player, "&dTeleporting to &r&a%s", args[0]);
                        break;
                    case "nether":
                        World netherWorld = Bukkit.getWorld(worldName.concat("_nether"));
                        if (y > 128) {
                            player.teleportAsync(new Location(netherWorld, x, 125, z));
                        } else player.teleportAsync(new Location(netherWorld, x, y, z));
                        sendMessage(player, "&dTeleporting to &r&a%s", args[0]);
                        break;
                    case "end":
                        World endWorld = Bukkit.getWorld(worldName.concat("_the_end"));
                        player.teleportAsync(new Location(endWorld, x, y, z));
                        sendMessage(player, "&dTeleporting to &r&a%s", args[0]);
                        break;
                    default:
                        sendMessage(sender, "&4Error:&r&c Unknown world");
                        break;
                }
            } else sendErrorMessage(sender, "Please include one argument /world <end | overworld | nether>");
        } else sendErrorMessage(sender, PLAYER_ONLY);
    }
}