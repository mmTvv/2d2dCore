package org.ggupp.customexperience;

import org.ggupp.customexperience.util.PrefixManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import static org.apache.logging.log4j.LogManager.getLogger;


public class PlayerPrefix implements Listener {
    private final JavaPlugin plugin;
    private final PrefixManager prefixManager = new PrefixManager();

    public PlayerPrefix(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String tag = prefixManager.getPrefix(event.getPlayer());
        setupTag(event.getPlayer(), tag);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String tag = prefixManager.getPrefix(event.getPlayer());
        setupTag(event.getPlayer(), tag);
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        String tag = prefixManager.getPrefix(event.getPlayer());
        setupTag(event.getPlayer(), tag);
    }

    public void setupTag(Player player, String tag) {
        player.setPlayerListName(String.format("%s%s",ChatColor.translateAlternateColorCodes('&', tag), player.getDisplayName()));
    }

}

