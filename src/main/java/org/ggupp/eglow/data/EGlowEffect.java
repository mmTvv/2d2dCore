package org.ggupp.eglow.data;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.ggupp.eglow.EGlowSection;

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

    public EGlowEffect(EGlowSection main, String name, int delay, ChatColor... colors){
        setMain(main);
        setName(name);
        setDelay(delay);
        Collections.addAll(effectColors, colors);
    }

    public void addPlayer(EGlowPlayer player){
        getPlayers().put(player, 0);
        actionEffect();
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

                            for (EGlowPlayer player1 : DataManager.getEGlowPlayers()) {
                                if (player.getPlayer().isListed(player1.getPlayer()) && player.getPlayer().getLocation().distance(player1.getPlayer().getLocation()) < 40) {
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
