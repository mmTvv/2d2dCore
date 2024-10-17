package org.ggupp.eglow.data;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ggupp.Localization;
import org.ggupp.eglow.EGlowSection;
import org.ggupp.eglow.utils.ColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class EGlowEffect {
    private EGlowSection main;
    private String name;
    private int delay;
    private ScheduledTask task;
    private ArrayList<ChatColor> effectColors = new ArrayList<>();
    private ConcurrentHashMap<EGlowPlayer, Integer> players = new ConcurrentHashMap<>();
    private final Material materialColor;

    public EGlowEffect(EGlowSection main, String name, int delay, ChatColor... colors){
        this(main, name, delay, ColorUtil.getMaterial(colors[0]), colors);
    }

    public EGlowEffect(EGlowSection main, String name, int delay, Material dyeColor, ChatColor... colors){
        setMain(main);
        setName(name);
        setDelay(delay);
        Collections.addAll(effectColors, colors);
        this.materialColor = dyeColor;
    }

    public void addPlayer(EGlowPlayer player){
        getPlayers().put(player, 0);
        actionEffect();
    }

    public String getDisplayName(Player sender){
        return Localization.getLocalization(sender.getLocale()).get("eglow_color." + this.getName());
    }

    public void removePlayer(EGlowPlayer player){
        getPlayers().remove(player);
    }

    public String getPermissionNode(){
        return  "2d2dcore.eglow.effect." + name;
    }

    private void actionEffect() {
        //System.out.println("e " + getName());
        if(getTask() == null) {
            //System.out.println("!= null " + getName());
            setTask(
                    Bukkit.getGlobalRegionScheduler().runAtFixedRate(main.plugin(), (t) -> {
                        //System.out.println("3 " + getName());

                        if (getPlayers() == null)
                            players = new ConcurrentHashMap<>();
                        if (getPlayers().isEmpty()) {
                            //System.out.println("cencel " + getName());
                            setTask(null);
                            t.cancel();
                            return;
                        }

                        getPlayers().forEach((player, progress) -> {
                            //System.out.println("4 " + getName());
                            ChatColor color = effectColors.get(progress);
                            if(player.getPlayer() == null){
                                getPlayers().remove(player);
                                return;
                            }

                            for (EGlowPlayer player1 : DataManager.getEGlowPlayers()) {
                                if (player.getPlayer().isListed(player1.getPlayer()) && player.getPlayer().getWorld().equals(player1.getPlayer().getWorld())
                                        && player.getPlayer().getLocation().distance(player1.getPlayer().getLocation()) < 40) {
                                    try {
                                        main.glowingEntities().setGlowing(player.getPlayer(), player1.getPlayer(), color);
                                    } catch (ReflectiveOperationException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }

                            if (progress < getEffectColors().size() - 1) {
                                getPlayers().replace(player, progress + 1);
                            } else {
                                getPlayers().replace(player, 0);
                            }
                            //System.out.println("progress " + progress);
                        });
                    }, 5, getDelay())
            );
        }
    }
}
