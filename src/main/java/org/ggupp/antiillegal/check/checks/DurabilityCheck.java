package org.ggupp.antiillegal.check.checks;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.ggupp.antiillegal.check.Check;

public class DurabilityCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable && damageable.getDamage() < 0) return true;
        return meta.isUnbreakable();
    }

    @Override
    public boolean shouldCheck(ItemStack material) {
        return true;
    }

    @Override
    public void fix(ItemStack item) {
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta.isUnbreakable()) meta.setUnbreakable(false);
        if (meta instanceof Damageable damageable) {
            if (damageable.getDamage() < 0) {
                damageable.setDamage(1);
            } else if (damageable.getDamage() > item.getType().getMaxDurability()) damageable.setDamage(item.getType().getMaxDurability());
            item.setItemMeta(meta);
        }
    }
}
