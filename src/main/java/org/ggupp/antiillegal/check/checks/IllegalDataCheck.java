package org.ggupp.antiillegal.check.checks;

import org.ggupp.antiillegal.check.Check;import org.bukkit.inventory.ItemStack;


public class IllegalDataCheck implements Check {
    @Override
    public boolean check(ItemStack item) {
        return false;
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return false;
    }

    @Override
    public void fix(ItemStack item) {

    }
}
