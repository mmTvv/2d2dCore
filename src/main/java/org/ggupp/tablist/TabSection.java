package org.ggupp.tablist;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.ggupp.Localization;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.customexperience.util.PrefixManager;
import org.ggupp.tablist.listeners.PlayerJoinListener;
import org.ggupp.tablist.util.Utils;
import org.ggupp.tablist.worker.TabWorker;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TabSection implements Section {
    private final Main plugin;
    private ConfigurationSection config;
    private final PrefixManager prefixManager = new PrefixManager();

    @Override
    public void enable() {
        config = plugin.getSectionConfig(this);
        plugin.getExecutorService().scheduleAtFixedRate(new TabWorker(this), 0, 1, TimeUnit.SECONDS);
        plugin.register(new PlayerJoinListener(this));
    }

    @Override
    public void disable() {

    }

    @Override
    public void reloadConfig() {
        config = plugin.getSectionConfig(this);
    }

    @Override
    public String getName() {
        return "TabList";
    }

    public void setTab(Player player) {
        String tag = ChatColor.translateAlternateColorCodes('&', prefixManager.getPrefix(player));
        player.setPlayerListName(String.format("%s%s", tag, player.getDisplayName()));

        Localization loc = Localization.getLocalization(player.locale().getLanguage());
        Utils.parsePlaceHolders(String.join("\n", loc.getStringList("TabList.Header")), player, plugin.getStartTime()).thenAccept(player::sendPlayerListHeader);
        Utils.parsePlaceHolders(String.join("\n", loc.getStringList("TabList.Footer")), player, plugin.getStartTime()).thenAccept(player::sendPlayerListFooter);
    }
}