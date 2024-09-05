package org.ggupp.dupe;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SalC1Dupe implements Listener {

    private final JavaPlugin plugin;
    public SalC1Dupe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
    Player p = e.getPlayer();
    Entity entity = e.getRightClicked();
    //if(Utils.getConfig().getBoolean("salc1-dupe.enabled")){
        if(p.getInventory().getItemInMainHand().getType() == Material.CHEST){
            if(entity instanceof Donkey || entity instanceof Llama){
                e.setCancelled(true);
                ChestedHorse donkey = (ChestedHorse) entity;
                for(ItemStack i : donkey.getInventory().getContents()){
                    if(i != null){
                        if(i.getType() != Material.SADDLE){
                            donkey.getWorld().dropItem(donkey.getLocation(), i);
                        }
                    }
                }
                p.sendMessage("&l[&5&l2D&d&l2D&r&l] Placing chests on entities is currently disabled to prevent duplication exploits.");
                donkey.setCarryingChest(false);
            }
        }
    //}
}
}