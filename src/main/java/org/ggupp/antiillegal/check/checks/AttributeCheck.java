package org.ggupp.antiillegal.check.checks;

import org.ggupp.antiillegal.check.Check;import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().hasAttributeModifiers() || !item.getItemMeta().getItemFlags().isEmpty();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return true;
    }

    @Override
    public void fix(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getAttributeModifiers() != null) meta.getAttributeModifiers().forEach((a, m) -> meta.removeAttributeModifier(a));
        meta.removeItemFlags(meta.getItemFlags().toArray(ItemFlag[]::new));
        item.setItemMeta(meta);
    }
}
