package org.ggupp.antiillegal.util;

import org.ggupp.antiillegal.AntiIllegalMain;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Utils {
    public static void checkStand(ArmorStand stand, AntiIllegalMain main) {
        EntityEquipment eq = stand.getEquipment();
        main.checkFixItem(eq.getItemInMainHand(), null);
        main.checkFixItem(eq.getItemInOffHand(), null);
        for (ItemStack item : eq.getArmorContents()) main.checkFixItem(item, null);
    }
}
