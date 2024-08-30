package org.ggupp.customexperience;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import static org.ggupp.util.GlobalUtils.log;


public class PlayerViewDistance implements Listener {
    private final JavaPlugin plugin;

    public PlayerViewDistance(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setRenderDistance(player);
    }

    private void setRenderDistance(Player player) {
        int renderDistance = plugin.getConfig().getInt("viewdistance.default", 2);
        int maxChunks = 2;

        for (String permission : player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).toList()) {
            if (permission.startsWith("2d2dcore.viewdistance.")) {
                String chunksStr = permission.replace("2d2dcore.viewdistance.", "");
                try {
                    int chunks = Integer.parseInt(chunksStr);
                    chunks = Math.max(2, Math.min(32, chunks));

                    if (chunks > maxChunks) {
                        maxChunks = chunks;
                    }
                    renderDistance = maxChunks;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        player.setSendViewDistance(renderDistance);
        player.setViewDistance(renderDistance);
        log(Level.INFO,"View distance set to " + renderDistance + " chunks for player: " + player.getName());
    }
}
