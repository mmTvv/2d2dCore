package org.ggupp.home.commands;

import lombok.RequiredArgsConstructor;
import org.ggupp.home.Home;
import org.ggupp.home.HomeData;
import org.ggupp.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.ggupp.util.GlobalUtils.sendMessage;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class HomeCommand implements TabExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            int radius = main.config().getInt("Radius");
            HomeData homes = main.homes().get(player);
            if (!homes.hasHomes()) {
                sendPrefixedLocalizedMessage(player, "home_no_homes");
                return true;
            }
            String names = String.join(", ", homes.stream().map(Home::getName).toArray(String[]::new));
            if (args.length < 1) {
                sendPrefixedLocalizedMessage(player, "home_specify_home", names);
                return true;
            }
            if (isSpawn(player, radius)) {
                sendPrefixedLocalizedMessage(player, "home_too_close", radius);
                return true;
            }
            boolean homeFound = false;
            for (Home home : homes.getHomes()) {
                if (!home.getName().equals(args[0])) continue;
                vanish(player);
                player.teleportAsync(home.getLocation());
                unVanish(player);
                sendPrefixedLocalizedMessage(player, "home_success", home.getName());
                homeFound = true;
                break;
            }
            if (!homeFound) sendPrefixedLocalizedMessage(player, "home_not_found", args[0]);
        } else sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }

    private boolean isSpawn(Player player, int range) {
        if (player.isOp()) return false;
        Location loc = player.getLocation();
        return loc.getBlockX() < range && loc.getBlockX() > -range && loc.getBlockZ() < range && loc.getBlockZ() > -range;
    }

    private void vanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.hidePlayer(main.plugin(), player);
            }
        }
    }

    public void unVanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.showPlayer(main.plugin(), player);
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return main.tabComplete(sender, args);
    }
}