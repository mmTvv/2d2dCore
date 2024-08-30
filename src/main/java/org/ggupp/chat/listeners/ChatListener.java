package org.ggupp.chat.listeners;

import lombok.RequiredArgsConstructor;
import org.ggupp.chat.ChatInfo;
import org.ggupp.chat.ChatSection;
import org.ggupp.customexperience.util.PrefixManager;
import org.ggupp.util.GlobalUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static org.ggupp.util.GlobalUtils.log;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final ChatSection manager;
    private final HashSet<String> tlds;
    private ScheduledExecutorService service;
    private final PrefixManager prefixManager = new PrefixManager();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (service == null) service = manager.getPlugin().getExecutorService();
        event.setCancelled(true);
        Player sender = event.getPlayer();
        int cooldown = manager.getConfig().getInt("Cooldown");
        ChatInfo ci = manager.getInfo(sender);
        if (ci.isChatLock()) {
            sendPrefixedLocalizedMessage(sender, "chat_cooldown", cooldown);
            return;
        }
        ci.setChatLock(true);
        service.schedule(() -> ci.setChatLock(false), cooldown, TimeUnit.SECONDS);
        String ogMessage = event.getMessage();
        String playerName = LegacyComponentSerializer.legacyAmpersand().serialize(sender.displayName());
        TextComponent message = format(ogMessage, playerName, sender);

        String tag = ChatColor.translateAlternateColorCodes('&', prefixManager.getPrefix(sender));
        sender.setPlayerListName(String.format("%s%s",ChatColor.translateAlternateColorCodes('&', tag), sender.getDisplayName()));
        if (blockedCheck(ogMessage)) {
            sender.sendMessage(message);
            log(Level.INFO, "&3Prevented&r&a %s&r&3 from sending a message that has banned words", sender.getName());
            return;
        }
        if (domainCheck(ogMessage)) {
            sender.sendMessage(message);
            log(Level.INFO, "&3Prevented player&r&a %s&r&3 from sending a link / server ip", sender.getName());
            return;
        }
        Bukkit.getLogger().info(GlobalUtils.getStringContent(message));
        for (Player recipient : Bukkit.getOnlinePlayers()) {
            ChatInfo info = manager.getInfo(recipient);
            if (info == null) continue;
            if (info.isIgnoring(sender.getUniqueId()) || info.isToggledChat()) continue;
            recipient.sendMessage(message);
        }
    }

    private boolean domainCheck(String message) {
        if (!manager.getConfig().getBoolean("PreventLinks")) return false;
        message = message.toLowerCase().replace("dot", ".").replace("d0t", ".");
        if (message.indexOf('.') == -1) return false;
        String[] split = message.trim().split("\\.");
        if (split.length == 2) {
            String possibleTLD = split[1];
            if (possibleTLD.contains("/")) possibleTLD = possibleTLD.substring(0, possibleTLD.indexOf("/"));
            return tlds.contains(possibleTLD);
        } else {
            for (String word : split) {
                if (word.contains("/")) word = word.substring(0, word.indexOf("/"));
                if (tlds.contains(word)) return true;
            }
        }
        return false;
    }

    private boolean blockedCheck(String message) {
        List<String> blocked = manager.getConfig().getStringList("Blocked");
        for (String blockedWord : blocked) {
            if (message.toLowerCase().contains(blockedWord.toLowerCase())) return true;
        }
        return false;
    }
    private TextComponent format(String message, String playerName, Player player) {
        String tag = ChatColor.translateAlternateColorCodes('&', prefixManager.getPrefix(player));
        String formatString = "%s<%s&r> ";
        if (tag.isEmpty()) formatString = "%s<%s&r> ";

        TextComponent name = GlobalUtils.translateChars(String.format(formatString, tag, playerName));
        TextComponent msg = (message.startsWith(">")) ? Component.text(message).color(TextColor.color(0, 255, 0)) : Component.text(message);
        return name.append(msg);
    }
}
