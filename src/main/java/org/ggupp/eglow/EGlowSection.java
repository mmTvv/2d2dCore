package org.ggupp.eglow;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.eglow.commands.EGlowCommand;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowPlayer;
import org.ggupp.eglow.io.EGlowJsonStorage;
import org.ggupp.eglow.io.EGlowStorege;
import org.ggupp.eglow.listener.EGlowListener;

import java.io.File;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class EGlowSection implements Section {
    private final Main plugin;
    private GlowingEntities glowingEntities;
    private ConfigurationSection config;
    private EGlowStorege<EGlowPlayer> storage;

    @Override
    public void enable() {
        File homesFolder = new File(plugin.getSectionDataFolder(this), "PlayerEGlows");
        if (!homesFolder.exists()) homesFolder.mkdir();
        storage = new EGlowJsonStorage(homesFolder);


        config = plugin.getConfig();
        glowingEntities = new GlowingEntities(this.plugin);
        DataManager.init(this);
        plugin.register(new EGlowListener(this));
        plugin.getCommand("eglow").setExecutor(new EGlowCommand(this));
    }

    @Override
    public void disable() {
        DataManager.disable();
    }

    @Override
    public String getName() {
        return "eglow";
    }

    @Override
    public void reloadConfig() {
        DataManager.init(this);
        this.config = plugin.getConfig();
    }
}
