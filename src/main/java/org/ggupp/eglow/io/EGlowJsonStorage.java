package org.ggupp.eglow.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowPlayer;
import org.ggupp.util.GlobalUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

@RequiredArgsConstructor
public class EGlowJsonStorage implements EGlowStorege<EGlowPlayer> {
    private final File dataDir;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void save(EGlowPlayer eGlowPlayer) {
        File file = new File(dataDir, eGlowPlayer.getPlayer().getUniqueId().toString().concat(".json"));

        try {
            JsonObject obj = new JsonObject();

            obj.addProperty("activeGlow", eGlowPlayer.getGlowEffect().getName());

            @Cleanup FileWriter fw = new FileWriter(file, false);
            gson.toJson(obj, fw);
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "Failed to save EGlow for&r&a %s&r&c. Please see the stacktrace below for more info", eGlowPlayer.getPlayer().getUniqueId());
            t.printStackTrace();
        }
    }

    @Override
    public void load(EGlowPlayer eGlowPlayer) {
        try {
            File file = new File(dataDir, eGlowPlayer.getPlayer().getUniqueId().toString().concat(".json"));

            if (file.exists()) {
                @Cleanup FileReader reader = new FileReader(file);
                JsonObject obj = gson.fromJson(reader, JsonObject.class);

                eGlowPlayer.setEffect(DataManager.getEGlowEffect(obj.get("activeGlow").getAsString()));
            }
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "&cFailed to parse&r&a %s&r&c. This is most likely due to malformed json", eGlowPlayer.getPlayer().getUniqueId());
            t.printStackTrace();
        }
    }

    @Override
    public void delete(EGlowPlayer eGlowPlayer) {
        File file = new File(dataDir, eGlowPlayer.getPlayer().getUniqueId().toString().concat(".json"));
        file.delete();
        GlobalUtils.log(Level.INFO, "&3Deleted eglow file for&r&a %s&r", eGlowPlayer.getPlayer().getName());
    }
}
