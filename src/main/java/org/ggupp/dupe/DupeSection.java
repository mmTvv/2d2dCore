package org.ggupp.dupe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.dupe.framedupe.FrameDupe;
import org.ggupp.dupe.zombiedupe.ZombieDupe;
import org.ggupp.util.GlobalUtils;



@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class DupeSection implements Section {
    private final Main plugin;

    private ConfigurationSection config;

    @Override
    public void enable() {
        config = plugin.getSectionConfig(this);
        plugin.register(new FrameDupe(plugin));
        plugin.register(new ZombieDupe(plugin));
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
        return "Dupe";
    }

    private String getItemName(ItemStack itemStack) {
        return (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) ? GlobalUtils.getStringContent(itemStack.getItemMeta().displayName()) : itemStack.getType().name();
    }
}
