package org.ggupp.customexperience.util;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixManager {

    private static final List<String> PREFIX_HIERARCHY = Arrays.asList(
            "*",
            "2d2dcore.prefix.dev",
            "2d2dcore.prefix.bot",
            "2d2dcore.prefix.donator3",
            "2d2dcore.prefix.donator2",
            "2d2dcore.prefix.donator1"
    );

    private static final Map<String, String> PREFIXES = new HashMap<>();
    static {
        PREFIXES.put("*", "&7&l[&r&5OWNER&7&l]&r");
        PREFIXES.put("2d2dcore.prefix.dev", "&7&l[&r&d DEV&7&l]&r");
        PREFIXES.put("2d2dcore.prefix.bot", "&7&l[&r&bBOT&7&l]&r");
        PREFIXES.put("2d2dcore.prefix.donator3", "&7&l[&r&1VIP++&7&l]&r");
        PREFIXES.put("2d2dcore.prefix.donator2", "&7&l[&r&3VIP+&7&l]&r");
        PREFIXES.put("2d2dcore.prefix.donator1", "&7&l[&r&9VIP&7&l]&r");
    }

    public String getPrefix(Player player) {
        String highestPrefix = "";

        for (String permission : PREFIX_HIERARCHY) {
            if (player.hasPermission(permission)) {
                highestPrefix = PREFIXES.get(permission) + " ";
                break;
            }
        }
        return highestPrefix;
    }
}