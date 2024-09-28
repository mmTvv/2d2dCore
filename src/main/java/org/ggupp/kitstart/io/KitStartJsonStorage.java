package org.ggupp.kitstart.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.ggupp.kitstart.KitStartData;
import org.ggupp.util.GlobalUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

@RequiredArgsConstructor
public class KitStartJsonStorage {
    private final File dataDir;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void save(KitStartData data) {
        File file = new File(dataDir, "pos.json");

        try {
            JsonObject obj = new JsonObject();

            if(data == null || data.getPlatformPos1() == null){
                return;
            }
            obj.addProperty("world", data.getPlatformPos1().getWorld().getName());

            JsonObject plat1Obg = new JsonObject();
            plat1Obg.addProperty("x", data.getPlatformPos1().x());
            plat1Obg.addProperty("y", data.getPlatformPos1().y());
            plat1Obg.addProperty("z", data.getPlatformPos1().z());

            JsonObject plat2Obg = new JsonObject();
            plat2Obg.addProperty("x", data.getPlatformPos2().x());
            plat2Obg.addProperty("y", data.getPlatformPos2().y());
            plat2Obg.addProperty("z", data.getPlatformPos2().z());

            JsonObject teleportObg = new JsonObject();
            teleportObg.addProperty("x", data.getTeleportPos().x());
            teleportObg.addProperty("y", data.getTeleportPos().y());
            teleportObg.addProperty("z", data.getTeleportPos().z());

            obj.add("teleportObg", teleportObg);
            obj.add("plat1Obg", plat1Obg);
            obj.add("plat2Obg", plat2Obg);

            @Cleanup FileWriter fw = new FileWriter(file, false);
            gson.toJson(obj, fw);
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "Failed to save Kit Start platform pos for&r&a. Please see the stacktrace below for more info");
            t.printStackTrace();
        }
    }

    public KitStartData load() {
        try {
            File file = new File(dataDir, "pos.json");

            Location defailtLocation = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
            if(!file.exists()){
                return new KitStartData(defailtLocation, defailtLocation, defailtLocation);
            }

            @Cleanup FileReader reader = new FileReader(file);
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            if(obj == null){
                return new KitStartData(defailtLocation, defailtLocation, defailtLocation);
            }
            JsonObject plat1Obg = obj.get("plat1Obg").getAsJsonObject();
            JsonObject plat2Obg = obj.get("plat2Obg").getAsJsonObject();
            JsonObject teleportPosObg = obj.get("teleportObg").getAsJsonObject();
            String worldStr = obj.get("world").getAsString();
            World world = worldStr == null ? Bukkit.getWorlds().get(0) : Bukkit.getWorld(worldStr);

            Location platLocation1 = plat1Obg == null ? defailtLocation :
                    new Location(world, plat1Obg.get("x").getAsDouble(), plat1Obg.get("y").getAsDouble(), plat1Obg.get("z").getAsDouble());
            Location platLocation2 = plat2Obg == null ? defailtLocation :
                    new Location(world, plat2Obg.get("x").getAsDouble(), plat2Obg.get("y").getAsDouble(), plat2Obg.get("z").getAsDouble());
            Location teleportLocation = teleportPosObg == null ? defailtLocation :
                    new Location(world, teleportPosObg.get("x").getAsDouble(), teleportPosObg.get("y").getAsDouble(), teleportPosObg.get("z").getAsDouble());

            return new KitStartData(platLocation1, platLocation2, teleportLocation);
        } catch (Throwable t) {
            GlobalUtils.log(Level.SEVERE, "&cFailed to parse&r&a &r&c. This is most likely due to malformed json");
            t.printStackTrace();
            return null;
        }
    }
}
