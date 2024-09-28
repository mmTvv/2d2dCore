package org.ggupp.eglow.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ggupp.Localization;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowEffect;
import org.ggupp.eglow.data.EGlowPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

public class EGlowMenu implements InventoryHolder {
    private final Inventory inventory;
    private final Player sender;
    private int currentPage;
    private final int itemsPage = 46;

    public EGlowMenu(Player sender) {
        this.sender = sender;
        this.inventory = Bukkit.createInventory(this, 54, Localization.getLocalization(this.sender.locale().getLanguage()).get("eglow_name_menu"));
        this.currentPage = 0;
        loadItems();
    }

    public void open() {
        sender.openInventory(inventory);
    }

    private void loadItems() {
        inventory.clear();

        List<EGlowEffect> effects = DataManager.getEGlowEffects();

        int start = currentPage * itemsPage;
        int end = Math.min(start + itemsPage, effects.size());

        int i1 = 0;
        for (int i = start; i < end; i++) {
            EGlowEffect effect = effects.get(i);
            if (!effect.getEffectColors().get(0).equals(ChatColor.RESET)) {
                ItemStack effectItem = createEffectItem(effect);
                inventory.setItem(i1, effectItem);
                i1++;
            }
        }

        ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta paneMeta = glassPane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" ");
            glassPane.setItemMeta(paneMeta);
        }

        inventory.setItem(45, glassPane);
        inventory.setItem(46, glassPane);
        inventory.setItem(52, glassPane);
        inventory.setItem(53, glassPane);

        ItemStack redWool = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta woolMeta = glassPane.getItemMeta();
        if (paneMeta != null) {
            woolMeta.setDisplayName(Localization.getLocalization(sender.locale().getLanguage()).get("eglow_menu_disable_effect"));
            redWool.setItemMeta(woolMeta);
        }

        inventory.setItem(49, redWool);

        ItemStack nextPageArrow = new ItemStack(Material.ARROW, 1);
        ItemMeta nextMeta = nextPageArrow.getItemMeta();
        if (nextMeta != null) {
            nextMeta.setDisplayName(Localization.getLocalization(sender.locale().getLanguage()).get("eglow_menu_next_page"));
            nextPageArrow.setItemMeta(nextMeta);
        }
        inventory.setItem(51, nextPageArrow);

        ItemStack previousPageArrow = new ItemStack(Material.ARROW, 1);
        ItemMeta prevMeta = previousPageArrow.getItemMeta();
        if (prevMeta != null) {
            prevMeta.setDisplayName(Localization.getLocalization(sender.locale().getLanguage()).get("eglow_menu_pre_page"));
            previousPageArrow.setItemMeta(prevMeta);
        }
        inventory.setItem(47, previousPageArrow);
    }

    private ItemStack createEffectItem(EGlowEffect effect) {
        ItemStack item = new ItemStack(effect.getMaterialColor(), 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(effect.getDisplayName(sender).replace("&", "§"));
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(inventory)) return;

        event.setCancelled(true);

        if (event.getSlot() == 51 && (currentPage + 1) * itemsPage < DataManager.getEGlowEffects().size()) {
            currentPage++;
            loadItems();
        } else if (event.getSlot() == 47 && currentPage > 0) {
            currentPage--;
            loadItems();
        } else if(event.getSlot() == 49){
            EGlowPlayer eGlowPlayer = DataManager.getEGlowPlayer(sender);

            eGlowPlayer.disableGlow();
            sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_disable");
        } else {
            int clickedSlot = event.getSlot();
            if (clickedSlot >= 0 && clickedSlot < itemsPage - 1) {
                List<EGlowEffect> effects = DataManager.getEGlowEffects();
                EGlowEffect effect = effects.get(currentPage * itemsPage + clickedSlot);
                if(!sender.hasPermission(effect.getPermissionNode())){
                    sendPrefixedLocalizedMessage(sender, "eglow_not_permission_effect", effect.getDisplayName(sender));
                    sender.closeInventory();
                    return;
                }

                EGlowPlayer eGlowPlayer = DataManager.getEGlowPlayer(sender);

                if (eGlowPlayer.getGlowEffect() != null && eGlowPlayer.getGlowEffect().equals(effect)) {
                    sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_same");
                    return;
                }

                eGlowPlayer.setEffect(effect);
                sendPrefixedLocalizedMessage(sender, "eglow_set", effect.getDisplayName(sender));
            }
        }
    }
}
