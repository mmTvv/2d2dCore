package org.ggupp.customexperience;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;

public class PlayerSimulationDistance implements Listener {
    private final JavaPlugin plugin;

    public PlayerSimulationDistance(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setSimulationDistance(player);
    }

    private void setSimulationDistance(Player player) {
        int simulationDistance = plugin.getConfig().getInt("simulationdistance.default", 4);
        int maxChunks = 2;

        for (String permission : player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList()) {
            if (permission.startsWith("2d2dcore.simulationdistance.")) {
                String chunksStr = permission.replace("2d2dcore.simulationdistance.", "");
                try {
                    int chunks = Integer.parseInt(chunksStr);
                    chunks = Math.max(2, Math.min(32, chunks));

                    if (chunks > maxChunks) {
                        maxChunks = chunks;
                    }
                    simulationDistance = maxChunks;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        player.setSimulationDistance(simulationDistance);
        log(Level.INFO,"Simulation distance set to " + simulationDistance + " chunks for player: " + player.getName());
    }
}
