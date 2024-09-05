package org.ggupp.tablist.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.ggupp.util.GlobalUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Utils {

    public static CompletableFuture<Component> parsePlaceHolders(String input, Player player, long startTime) {
        CompletableFuture<Component> future = new CompletableFuture<>();
        GlobalUtils.getTpsNearEntity(player).thenAccept(tps -> {
            String strTps = (tps >= 20) ? String.format("%s*20.0", ChatColor.GREEN) : String.format("%s%.2f", Utils.getTPSColor(tps), tps);
            String uptime = Utils.getFormattedInterval(System.currentTimeMillis() - startTime);
            String online = String.valueOf(Bukkit.getOnlinePlayers().size());
            String ping = String.valueOf(player.getPing());
            future.complete(GlobalUtils.translateChars(input.replace("%tps%", strTps).replace("%players%", online).replace("%ping%", ping).replace("%uptime%", uptime)));
        }); //Stopgap until folia adds an api for getting region TPS
        return future;
    }
    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }
    public static ChatColor getTPSColor(double tps) {
        if (tps >= 18.0D) {
            return ChatColor.GREEN;
        } else {
            return tps >= 13.0D ? ChatColor.YELLOW : ChatColor.RED;
        }
    }
    public static void secondPass(HashMap<Player, Integer> hashMap) {
        for (Map.Entry<Player, Integer> violationEntry : hashMap.entrySet()) {
            if (violationEntry.getValue() > 0)
                violationEntry.setValue(violationEntry.getValue() - 1);
        }
    }

}
