package org.ggupp.eglow.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ColorUtil {
    public static Material getMaterial(ChatColor chatColor) {
        return switch (chatColor) {
            case RED, DARK_RED -> Material.RED_STAINED_GLASS;
            case GREEN, DARK_GREEN -> Material.GREEN_STAINED_GLASS;
            case BLUE, DARK_BLUE -> Material.BLUE_STAINED_GLASS;
            case YELLOW, GOLD -> Material.YELLOW_STAINED_GLASS;
            case AQUA, DARK_AQUA -> Material.CYAN_STAINED_GLASS;
            case DARK_GRAY, GRAY -> Material.GRAY_STAINED_GLASS;
            case DARK_PURPLE -> Material.PURPLE_STAINED_GLASS;
            case LIGHT_PURPLE -> Material.PINK_STAINED_GLASS;
            case WHITE -> Material.WHITE_STAINED_GLASS;
            case BLACK -> Material.BLACK_STAINED_GLASS;
            default -> null;
        };
    }
}
