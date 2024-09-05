package org.ggupp.kitstart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.kitstart.commands.KitStartCommand;
import org.ggupp.kitstart.io.KitStartJsonStorage;
import org.ggupp.kitstart.listeners.PlayerJoinListener;

import java.io.File;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class KitStartSection implements Section {
    private final Main plugin;
    private KitStartData kitStartData;
    private ConfigurationSection config;
    private KitStartJsonStorage storage;

    @Override
    public void enable() {
        File homesFolder = new File(plugin.getSectionDataFolder(this), "kitStartPos");
        if (!homesFolder.exists()) homesFolder.mkdir();

        config = plugin.getConfig();
        storage = new KitStartJsonStorage(homesFolder);
        kitStartData = storage.load();
        plugin.register(new PlayerJoinListener(this));
        plugin.getCommand("kit").setExecutor(new KitStartCommand(this));
    }

    @Override
    public void disable() {
        storage.save(kitStartData);
    }

    @Override
    public String getName() {
        return "kitstart";
    }

    @Override
    public void reloadConfig() {
        config = plugin.getSectionConfig(this);
    }
}
