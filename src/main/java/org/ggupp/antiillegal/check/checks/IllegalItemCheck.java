package org.ggupp.antiillegal.check.checks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.ggupp.Main;
import org.ggupp.antiillegal.check.Check;
import org.ggupp.util.GlobalUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;


public class IllegalItemCheck implements Check {
    private final HashSet<Material> illegals;

    public IllegalItemCheck() {
        illegals = parseConfig();
    }

    @Override
    public boolean check(ItemStack item) {
        return illegals.contains(item.getType()); // O(1) thanks HashSet
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return true;
    }

    @Override
    public void fix(ItemStack item) {
        item.setAmount(0);
    }

    private HashSet<Material> parseConfig() {
        List<String> materialNames = Arrays.stream(Material.values()).map(Material::name).toList();
        List<String> strList = Main.getInstance().getConfig().getStringList("AntiIllegal.IllegalItems");
        List<Material> output = new ArrayList<>();
        for (String raw : strList) {
            try {
                raw = raw.toUpperCase();
                if (raw.contains("*")) {
                    raw = raw.replace("*", "");
                    for (String materialName : materialNames) {
                        if (materialName.contains(raw)) output.add(Material.getMaterial(materialName));
                    }
                    continue;
                }
                Material material = Material.getMaterial(raw);
                if (material == null) throw new EnumConstantNotPresentException(Material.class, raw);
                output.add(material);
            } catch (EnumConstantNotPresentException | IllegalArgumentException e) {
                GlobalUtils.log(Level.WARNING, "&3Unknown material&r&a %s&r&3 in blocks section of the config", raw);
            }
        }
        return new HashSet<>(output);
    }
}
