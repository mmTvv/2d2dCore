package org.ggupp.antiillegal.check.checks;

import org.ggupp.antiillegal.check.Check;import org.bukkit.inventory.ItemStack;

public class OverStackCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        return item.getAmount() > item.getType().getMaxStackSize();
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return true;
    }

    @Override
    public void fix(ItemStack item) {
        item.setAmount(item.getType().getMaxStackSize());
    }
}
