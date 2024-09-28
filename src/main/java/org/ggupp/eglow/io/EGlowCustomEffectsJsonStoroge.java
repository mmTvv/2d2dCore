package org.ggupp.eglow.io;

import com.google.gson.*;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.ggupp.eglow.EGlowSection;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowEffect;
import org.ggupp.util.GlobalUtils;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
public class EGlowCustomEffectsJsonStoroge {
    private final File dataDir;
    private final EGlowSection main;

    public Map<String, EGlowEffect> load() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, EGlowEffect> customEffects = new HashMap<>();

        try {

            if (dataDir.exists()) {
                @Cleanup FileReader reader = new FileReader(dataDir);
                JsonObject obj = gson.fromJson(reader, JsonObject.class);
                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    JsonObject effectData = entry.getValue().getAsJsonObject();

                    String name = effectData.get("name").getAsString();
                    int delay = effectData.get("delay").getAsInt();

                    JsonArray colorsArray = effectData.getAsJsonArray("colors");
                    ChatColor[] colors = new ChatColor[colorsArray.size()];
                    for (int i = 0; i < colorsArray.size(); i++) {
                        String configName = colorsArray.get(i).getAsString().replace("ı", "i").replace("purple", "dark_purple").replace("pink", "light_purple").replace("none", "reset").replace(" ", "");
                        colors[i] = ChatColor.valueOf(configName.toUpperCase());
                    }

                    EGlowEffect effect;

                    if(effectData.get("material") != null){
                        effect = new EGlowEffect(main, name, delay, Material.getMaterial(effectData.get("material").getAsString()), colors);
                    } else {
                        effect = new EGlowEffect(main, name, delay, colors);
                    }

                    customEffects.put(entry.getKey(), effect);
                    GlobalUtils.log(Level.INFO, String.format("Add custom effect: %s ", name));
                }
            }
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "&&cFailed to parse&r&a EGlow custom effect %s&r&c. This is most likely due to malformed json", dataDir.getParent());
            t.printStackTrace();
        }

        return customEffects;
    }
}
