package org.ggupp.antiillegal.check;

import org.bukkit.inventory.ItemStack;


public interface Check {


    boolean check(ItemStack item);

    boolean shouldCheck(ItemStack item);

    void fix(ItemStack item);
}
