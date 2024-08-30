package org.ggupp.home;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.ggupp.IStorage;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.home.commands.DelHomeCommand;
import org.ggupp.home.commands.HomeCommand;
import org.ggupp.home.commands.SetHomeCommand;
import org.ggupp.home.io.HomeJsonStorage;
import org.ggupp.home.listeners.JoinListener;
import org.ggupp.util.GlobalUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class HomeManager implements Section {
    private final HashMap<Player, HomeData> homes = new HashMap<>();
    private final Main plugin;
    private IStorage<HomeData, Player> storage;
    private ConfigurationSection config;

    @Override
    public void enable() {
        File homesFolder = new File(plugin.getSectionDataFolder(this), "PlayerHomes");
        if (!homesFolder.exists()) homesFolder.mkdir();
        storage = new HomeJsonStorage(homesFolder);
        config = plugin.getSectionConfig(this);
        if (!Bukkit.getOnlinePlayers().isEmpty()) Bukkit.getOnlinePlayers().forEach(p -> homes.put(p, storage.load(p)));
        plugin.register(new JoinListener(this));
        plugin.getCommand("home").setExecutor(new HomeCommand(this));
        plugin.getCommand("sethome").setExecutor(new SetHomeCommand(this));
        plugin.getCommand("delhome").setExecutor(new DelHomeCommand(this));
    }

    @Override
    public void disable() {
        homes.forEach((p, d) -> storage.save(d, p));
        homes.clear();
    }

    @Override
    public void reloadConfig() {
        this.config = plugin.getSectionConfig(this);
    }

    @Override
    public String getName() {
        return "Home";
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            HomeData homes = homes().getOrDefault(player, null);
            if (homes == null) return Collections.emptyList();
            if (args.length < 1) {
                return homes.stream().map(Home::getName).sorted(String::compareToIgnoreCase).collect(Collectors.toList());
            } else {
                return homes.stream().map(Home::getName).filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).sorted(String::compareToIgnoreCase).collect(Collectors.toList());
            }
        } else return Collections.emptyList();
    }

    public int getMaxHomes(Player player) {
        Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();
        List<Integer> maxL = perms.stream().map(PermissionAttachmentInfo::getPermission).filter(p -> p.startsWith("8b8tcore.home.max.")).map(s -> Integer.parseInt(s.substring(s.lastIndexOf('.') + 1))).toList();
        return (!maxL.isEmpty()) ? Collections.max(maxL) : config().getInt("MaxHomes");
    }
}
