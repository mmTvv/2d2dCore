package org.ggupp.antiillegal.check.checks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.ggupp.antiillegal.check.Check;
import org.ggupp.util.GlobalUtils;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCheck implements Check {
    private final CharsetEncoder encoder = StandardCharsets.ISO_8859_1.newEncoder();
    @Override
    public boolean check(ItemStack item) {
        BookMeta meta;
        try {
            meta = (BookMeta) item.getItemMeta();
        } catch (Exception e) {
            return false;
        }
        String[] pages = getPages(meta).orElse(null);
        return pages != null && encoder.canEncode(String.join(" ", pages));
    }

    @Override
    public boolean shouldCheck(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta() instanceof BookMeta;
    }

    @Override
    public void fix(ItemStack item) {
        BookMeta meta = (BookMeta) item.getItemMeta();
        List<Component> cleanPages = new ArrayList<>();
        String[] currPages = getPages(meta).orElseThrow(() -> new IllegalArgumentException("Book without pages passed to BookCheck#fix"));
        for (String page : currPages) {
            StringBuilder builder =  new StringBuilder();
            for (char c : page.toCharArray()) {
                if (encoder.canEncode(c)) builder.append(c);
            }
            TextComponent cleanComponent = Component.text(builder.toString());
            cleanPages.add(cleanComponent);
        }
        meta.pages(cleanPages);
        item.setItemMeta(meta);
    }

    private Optional<String[]> getPages(BookMeta meta) {
        return meta.hasPages() ? Optional.of(meta.pages().stream().map(GlobalUtils::getStringContent).toArray(String[]::new)) : Optional.empty();
    }
}
