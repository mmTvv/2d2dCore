package org.ggupp.patch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.patch.epc.ChestLimiter;
import org.ggupp.patch.epc.EntityCheckTask;
import org.ggupp.patch.epc.EntitySpawnListener;
import org.ggupp.patch.listeners.*;
import org.ggupp.patch.workers.ElytraWorker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class PatchSection implements Section {
    private final Main plugin;
    private Map<Player, Location> positions;
    @Getter private HashMap<EntityType, Integer> entityPerChunk;
    private ConfigurationSection config;

    @Override
    public void enable() {
        positions = new HashMap<>();
        config = plugin.getSectionConfig(this);
        entityPerChunk = parseEntityConf();
        plugin.getExecutorService().scheduleAtFixedRate(new ElytraWorker(this), 0, 1, TimeUnit.SECONDS);
        plugin.getExecutorService().scheduleAtFixedRate(new EntityCheckTask(this), 0, config.getInt("EntityPerChunk.CheckInterval"), TimeUnit.MINUTES);
        plugin.register(new Redstone(this));
        plugin.register(new EntitySpawnListener(this));
        plugin.register(new FallFlyListener(plugin));
        plugin.register(new EntitySwitchWorldListener(plugin));
        plugin.register(new NbtBanPatch(plugin));
        plugin.register(new AntiLagChestListener(plugin));
        plugin.register(new SuffocationListener());
        plugin.register(new ChestLimiter(plugin));
    }

    @Override
    public void disable() {
    }

    @Override
    public String getName() {
        return "Patch";
    }

    @Override
    public void reloadConfig() {
        config = plugin.getSectionConfig(this);
        if (entityPerChunk != null) {
            entityPerChunk.clear();
            entityPerChunk = parseEntityConf();
        }
    }

    private HashMap<EntityType, Integer> parseEntityConf() {
        List<String> raw = config.getStringList("EntityPerChunk.EntitiesPerChunk");
        HashMap<EntityType, Integer> buf = new HashMap<>();
        for (String str : raw) {
            String[] split = str.split("::");
            try {
                EntityType type = EntityType.valueOf(split[0].toUpperCase());
                int i = Integer.parseInt(split[1]);
                buf.put(type, i);
            } catch (EnumConstantNotPresentException | NumberFormatException e) {
                if (e instanceof NumberFormatException) {
                    log(Level.INFO, "&a%s&r&c is not a number", split[1]);
                    continue;
                }
                log(Level.INFO, "&cUnknown EntityType&r&a %s", split[0]);
            }
        }
        int defMax = config.getInt("EntityPerChunk.DefaultMax");
        if (defMax != -1) {
            for (EntityType type : EntityType.values()) buf.putIfAbsent(type, defMax);
        }
        return buf;
    }
}
