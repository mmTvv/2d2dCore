package org.ggupp.antiillegal.check.checks;

import org.ggupp.antiillegal.check.Check;import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;


public class LoreCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasLore();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return true;
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(List.of());
        item.setItemMeta(meta);
    }
}
