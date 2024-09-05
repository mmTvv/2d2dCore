package org.ggupp.antiillegal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.antiillegal.check.Check;
import org.ggupp.antiillegal.check.checks.*;
import org.ggupp.antiillegal.listeners.*;
import org.ggupp.util.GlobalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class AntiIllegalMain implements Section {
    private final Main plugin;
    private final List<Check> checks = new ArrayList<>(Arrays.asList(
            new OverStackCheck(),
            new DurabilityCheck(),
            new AttributeCheck(),
            new LoreCheck(),
            new EnchantCheck(),
            new PotionCheck(),
            new BookCheck(),
            new IllegalItemCheck()
    ));

    private ConfigurationSection config;

    @Override
    public void enable() {

        config = plugin.getSectionConfig(this);
        checks.add(new NameCheck(config));
//        checks.add(new ItemSizeCheck());

        plugin.register(new PlayerListeners(this), new MiscListeners(this), new InventoryListeners(this), new AttackListener(), new StackedTotemsListener());
        if(plugin.getConfig().getBoolean("AntiIllegal.EnableIllegalBlocksCleaner", true)) plugin.register(new IllegalBlocksCleaner());
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
        return "AntiIllegal";
    }

    public void checkFixItem(ItemStack item, Cancellable cancellable) {
        if (item == null || item.getType() == Material.AIR) return;
        for (Check check : checks) {
            if (check.shouldCheck(item) && check.check(item)) {
                if (cancellable != null && !cancellable.isCancelled()) cancellable.setCancelled(true);
                //GlobalUtils.log(Level.INFO, "Item %s failed the %s check and has been fixed.", getItemName(item), check.getClass().getSimpleName());
                check.fix(item);
                item.setItemMeta(item.getItemMeta());
            }
        }
    }
    private String getItemName(ItemStack itemStack) {
        return (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) ? GlobalUtils.getStringContent(itemStack.getItemMeta().displayName()) : itemStack.getType().name();
    }
}
