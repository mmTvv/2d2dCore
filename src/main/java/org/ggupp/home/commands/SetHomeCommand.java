package org.ggupp.home.commands;

import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ggupp.home.Home;
import org.ggupp.home.HomeData;
import org.ggupp.home.HomeManager;
import org.jetbrains.annotations.NotNull;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@AllArgsConstructor
public class SetHomeCommand implements CommandExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 1) {
                sendPrefixedLocalizedMessage(player, "sethome_include_name");
                return true;
            }
            int maxHomes = main.getMaxHomes(player);
            HomeData homes = main.homes().get(player);
            if (homes.stream().anyMatch(h -> h.getName().equals(args[0]))) {
                Home home = homes.stream().filter(h -> h.getName().equals(args[0])).findAny().get();
                sendPrefixedLocalizedMessage(player, "sethome_home_already_exists", home.getName());
                return true;
            }
            if (homes.getHomes().size() >= maxHomes && !player.isOp()) {
                sendPrefixedLocalizedMessage(player, "sethome_max_reached");
                return true;
            }
            homes.addHome(new Home(args[0], player.getLocation()));
            sendPrefixedLocalizedMessage(player, "sethome_success", args[0]);
        } else sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}

