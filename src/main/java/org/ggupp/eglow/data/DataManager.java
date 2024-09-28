package org.ggupp.eglow.data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.ggupp.eglow.Constants;
import org.ggupp.eglow.EGlowSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
    private static EGlowSection main;
    private final static ConcurrentHashMap<String, EGlowEffect> effects = new ConcurrentHashMap<>();
    private final static Map<String, EGlowPlayer> dataPlayers = new ConcurrentHashMap<>();

    public static void init(EGlowSection _main){
        main = _main;
        loadEffect();
    }

    public static EGlowPlayer addPlayer(Player player, String uuid){
        if (!dataPlayers.containsKey(uuid))
            dataPlayers.put(uuid, new EGlowPlayer(player, main));
        return dataPlayers.get(uuid);
    }

    public static void disable(){
        effects.clear();
    }

    public static Collection<EGlowPlayer> getEGlowPlayers(){
        return dataPlayers.values();
    }

    public static List<EGlowEffect> getEGlowEffects() {
        List<EGlowEffect> _effects = new ArrayList<>();

        effects.forEach((key, value) -> _effects.add(value));
        return _effects;
    }

    private static void loadEffect(){
        if(main.config().getBoolean("Eglow.loadVanillaColorEffect")) {
            for (ChatColor color : Constants.colors) {
                String configName = color.name().toLowerCase().replace("ı", "i").replace("dark_purple", "purple").replace("light_purple", "pink").replace("reset", "none").replace(" ", "");
                String name = configName.replace("_", "");
                addEGlowEffect(name, 50, color);
            }
            addEGlowEffect("none", 50, ChatColor.RESET);
        }

        if(main.config().getBoolean("Eglow.loadRainbowEffect")) {
            addEGlowEffect("rainbowslow", main.config().getInt("Eglow.delay.slow"), Constants.colors);
            addEGlowEffect("rainbowfast", main.config().getInt("Eglow.delay.fast"), Constants.colors);
        }
    }

    public static EGlowPlayer getEGlowPlayer(Player player) {
        return dataPlayers.getOrDefault(player.getUniqueId().toString(), null);
    }

    public static void removeEGlowPlayer(Player player) {
        dataPlayers.remove(player.getUniqueId().toString());
    }

    public static EGlowEffect getEGlowEffect(String name) {
        if (effects.containsKey(name.toLowerCase()))
            return effects.get(name.toLowerCase());

        return null;
    }

    private static void addEGlowEffect(String name, int delay, ChatColor... colors){
        if(!effects.containsKey(name)){
            effects.put(name, new EGlowEffect(main, name, delay, colors));
        }
    }
}
