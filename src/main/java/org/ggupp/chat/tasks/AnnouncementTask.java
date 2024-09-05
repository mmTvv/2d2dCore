package org.ggupp.chat.tasks;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ggupp.Localization;
import org.ggupp.util.GlobalUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AnnouncementTask implements Runnable {

    private ThreadLocalRandom random = ThreadLocalRandom.current();
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Localization loc = Localization.getLocalization(p.locale().getLanguage());
            List<TextComponent> announcements = loc.getStringList("announcements")
                    .stream()
                    .map(s -> s.replace("%prefix%", loc.getPrefix()))
                    .map(GlobalUtils::translateChars).toList();
            TextComponent announcement = announcements.get(random.nextInt(announcements.size()));
            p.sendMessage(announcement);
        }
    }
}
