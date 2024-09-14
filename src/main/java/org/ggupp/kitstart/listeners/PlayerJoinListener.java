package org.ggupp.kitstart.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ggupp.Main;
import org.ggupp.kitstart.KitStartSection;
import org.ggupp.kitstart.util.WorldUtils;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    private final KitStartSection main;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        final Player joiningPlayer = event.getPlayer();
        if (!joiningPlayer.hasPlayedBefore()) {
            boolean ENABLED = main.config().getBoolean("KitStart.enabled", false);

            if(!ENABLED){
                return;
            }

            Location teleportLocation = main.kitStartData().getTeleportPos();
            boolean teleport = main.config().getBoolean("KitStart.teleport", false);
            Location start = main.kitStartData().getPlatformPos1();
            Location end = main.kitStartData().getPlatformPos2();

            Bukkit.getRegionScheduler().run(Main.getInstance(), teleportLocation, (o) ->{
                if(teleport) {
                    joiningPlayer.teleportAsync(teleportLocation);
                }
                WorldUtils.copyRegion(start, end, joiningPlayer);
            });
        }
    }
}
