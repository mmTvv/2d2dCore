package org.ggupp.patch.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.ggupp.Main;
import org.ggupp.util.GlobalUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;

@RequiredArgsConstructor
public class EntitySwitchWorldListener implements Listener {
    private final Main main;

    private final HashSet<EntityType> blacklist = new HashSet<>() {{
        add(EntityType.BOAT);
        add(EntityType.DROPPED_ITEM);
        addAll(Arrays.stream(EntityType.values()).filter(e -> e.name().contains("MINECART")).toList());
    }};
    @EventHandler
    public void onEntitySwitchWorld(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (blacklist.contains(entity.getType())) {
            GlobalUtils.log(Level.INFO, "Prevented a %s from going through a portal at %s", event.getEntityType(), GlobalUtils.formatLocation(event.getEntity().getLocation()));
            entity.getScheduler().run(main, (tsk) -> entity.remove(), () ->{});
        }
    }
}
