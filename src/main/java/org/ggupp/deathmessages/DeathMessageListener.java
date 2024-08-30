package org.ggupp.deathmessages;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static org.ggupp.util.GlobalUtils.sendDeathMessage;


public class DeathMessageListener implements Listener {

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        e.deathMessage(null);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Entity killer = victim.getKiller();
        ItemStack weapon = null;

        if (victim.getLastDamageCause() == null) {
            return;
        }

        if (hasTotems(victim)) {
            return;
        }

        DeathCause deathCause = DeathCause.from(victim.getLastDamageCause().getCause());

        if(deathCause == DeathCause.UNKNOWN || deathCause == DeathCause.ENTITY_ATTACK){
            return;
        }

        if(deathCause == DeathCause.ENTITY_EXPLOSION) {

            if (Bukkit.getOnlinePlayers().contains(killer)) {
                Player killerPlayer = (Player) killer;
                weapon = getKillWeapon(killerPlayer);

                deathCause = DeathCause.from(DeathCause.PLAYER);

                String causePath = deathCause.getPath();
                String killerName = (killer == null) ? "GGuPP" : killer.getName();
                String weaponName = (weapon.getType() == Material.AIR) ? "their hand" : getWeaponName(weapon);

                if(killerName.equals(victim.getName())){
                    causePath = DeathCause.from(DeathCause.END_CRYSTAL).getPath();
                }
                sendDeathMessage(causePath, victim.getName(), killerName, weaponName);

            }
        } else if(killer == null){

            String causePath = deathCause.getPath();
            sendDeathMessage(causePath, victim.getName(), "", "");
        }

    }



    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        if (hasTotems(victim)) {
            return;
        }

        if (victim.getHealth() - event.getFinalDamage() > 0) {
            return;
        }

        Entity killer = event.getDamager();
        ItemStack weapon = null;

        if (killer instanceof Projectile) {
            Projectile proj = (Projectile) killer;

            if (proj.getShooter() != null && proj.getShooter() instanceof Entity) {
                killer = (Entity) proj.getShooter();
            }
        }

        DeathCause deathCause = DeathCause.from(killer.getType());

        if (deathCause == DeathCause.WITHER) {
            deathCause = DeathCause.WITHER_BOSS;
        }


        if((deathCause == DeathCause.PLAYER || deathCause == DeathCause.PROJECTILE || deathCause == DeathCause.ENTITY_EXPLOSION) && killer !=null){

            if(Bukkit.getOnlinePlayers().contains(killer)){
                Player killerPlayer = (Player) killer;
                weapon = getKillWeapon(killerPlayer);

                deathCause = DeathCause.from(DeathCause.PLAYER);
            } else {
                deathCause = DeathCause.from(DeathCause.PROJECTILE);
            }
        }

        String killerName = (killer == null) ? "GGuPP" : killer.getName();
        String weaponName;
        if (weapon == null || weapon.getType() == Material.AIR) {
            weaponName = "their hand";
        } else {
            weaponName = getWeaponName(weapon);
        }

        String causePath = deathCause.getPath();

        if (deathCause == DeathCause.END_CRYSTAL) {
            return;
        }
        if(deathCause == DeathCause.UNKNOWN){
            return;
        }

        sendDeathMessage(causePath, victim.getName(), killerName, weaponName);

    }

    private static ItemStack getKillWeapon(Player killer) {
        return killer.getInventory().getItemInMainHand();
    }

    private boolean hasTotems(Player player) {
        PlayerInventory inventory = player.getInventory();
        boolean hasTotems = false;

        if(inventory.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING){
            hasTotems = true;
        } else if(inventory.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING){
            hasTotems = true;
        }

        return hasTotems;
    }

    private String getWeaponName(ItemStack weapon) {
        if (weapon == null) {
            return "";
        }

        ItemMeta meta = weapon.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            return String.valueOf(weapon.getItemMeta().getDisplayName());
        } else {
            return weapon.getType().toString().replace("_", " ").toLowerCase();
        }
    }
}
