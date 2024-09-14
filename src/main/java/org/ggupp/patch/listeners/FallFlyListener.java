package org.ggupp.patch.listeners;

import org.ggupp.Main;
import org.ggupp.ViolationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import static org.ggupp.util.GlobalUtils.removeElytra;

public class FallFlyListener extends ViolationManager implements Listener {
    public FallFlyListener(Main main) {
        super(1, main);
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player) {
            int vls = getVLS(player);
            increment(player);
            if (vls > 10) {
                removeElytra(player);
                remove(player);
            }
        }
    }
}
