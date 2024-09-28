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
import org.ggupp.eglow.io.EGlowCustomEffectsJsonStoroge;
import org.ggupp.eglow.io.EGlowPlayerJsonStorage;
import org.ggupp.eglow.io.EGlowStorege;
import org.ggupp.eglow.listener.EGlowListener;
import org.ggupp.util.GlobalUtils;

import java.io.File;
import java.util.logging.Level;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class EGlowSection implements Section {
    private final Main plugin;
    private GlowingEntities glowingEntities;
    private ConfigurationSection config;
    private EGlowStorege<EGlowPlayer> storage;
    private EGlowCustomEffectsJsonStoroge customEffectsStoroge;

    @Override
    public void enable() {
        loadStorage();
        config = plugin.getConfig();
        glowingEntities = new GlowingEntities(this.plugin);
        DataManager.init(this);
        plugin.register(new EGlowListener(this));
        plugin.getCommand("eglow").setExecutor(new EGlowCommand(this));
    }

    private void loadStorage(){
        GlobalUtils.log(Level.INFO, "&cLoad player storage");
        File glowsFolder = new File(plugin.getSectionDataFolder(this), "PlayerEGlows");
        if (!glowsFolder.exists()) glowsFolder.mkdir();
        storage = new EGlowPlayerJsonStorage(glowsFolder);

        GlobalUtils.log(Level.INFO, "&cLoad custom effect");
        File customGlowsFolder = new File(plugin.getSectionDataFolder(this), "custom.json");
        if(!customGlowsFolder.exists()) GlobalUtils.unpackResource("custom.json", new File(plugin.getSectionDataFolder(this), "custom.json"));
        customEffectsStoroge = new EGlowCustomEffectsJsonStoroge(customGlowsFolder, this);
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
        loadStorage();
        DataManager.init(this);
        this.config = plugin.getConfig();
    }
}
