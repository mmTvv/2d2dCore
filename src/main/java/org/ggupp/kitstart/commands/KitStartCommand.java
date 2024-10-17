package org.ggupp.kitstart.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.ggupp.kitstart.KitStartSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.ggupp.util.GlobalUtils.translateChars;

@RequiredArgsConstructor
public class KitStartCommand implements TabExecutor  {
    private final KitStartSection main;
    private final List<String> tabCompletes = List.of("setSpawn", "setCopyPos1", "setCopyPos2");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!sender.isOp()){
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(translateChars("&3You must be a player to use this command"));
            return false;
        }

        try {
            if (args.length < 4) {
                sender.sendMessage(translateChars("&cUsage: /command <setspawn|setCopyPos1|setCopyPos2> <x> <y> <z>"));
                return true;
            }
            double x, y, z;
            try {
                x = Double.parseDouble(args[1]);
                y = Double.parseDouble(args[2]);
                z = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(translateChars("&cPlease provide valid numerical coordinates."));
                return true;
            }

            Player player = (Player) sender;
            Location location = new Location(player.getWorld(), x, y, z);

            if(args[0].equalsIgnoreCase("setspawn")){
                main.kitStartData().setTeleportPos(location);
            } else if(args[0].equalsIgnoreCase("setCopyPos1")) {
                main.kitStartData().setPlatformPos1(location);
            } else if(args[0].equalsIgnoreCase("setCopyPos2")) {
                main.kitStartData().setPlatformPos2(location);
            }

            sender.sendMessage(translateChars("&3Successfully changed"));
	    
        } catch (NumberFormatException e) {
            sender.sendMessage(translateChars("Error format. Please see the stacktrace below for more info"));
            e.printStackTrace();
	    return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!sender.isOp()){
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(tabCompletes);
        } else if (args.length == 2 && sender instanceof Player) {
            Player player = (Player) sender;
            completions.add(String.format("%d %d %d",
                    (int) player.getLocation().getX(),
                    (int) player.getLocation().getY(),
                    (int) player.getLocation().getZ()));
        }

        return completions;
    }
}
