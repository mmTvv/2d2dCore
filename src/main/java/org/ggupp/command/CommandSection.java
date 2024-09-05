package org.ggupp.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.ggupp.Main;
import org.ggupp.Section;


@Getter
@RequiredArgsConstructor
public class CommandSection implements Section {
    private final Main plugin;
    private CommandHandler commandHandler;
    private ConfigurationSection config;

    @Override
    public void enable() {
        commandHandler = new CommandHandler(this);
        config = plugin.getSectionConfig(this);
        commandHandler.registerCommands();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Commands";
    }

    @Override
    public void reloadConfig() {
        this.config = plugin.getSectionConfig(this);
    }

}
