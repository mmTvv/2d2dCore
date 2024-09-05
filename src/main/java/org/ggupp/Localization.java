package org.ggupp;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ggupp.util.GlobalUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class Localization {
    private static HashMap<String, Localization> localizationMap;
    private final Configuration config;

    protected static void loadLocalizations(File dataFolder) {
        if (localizationMap != null) localizationMap.clear();
        localizationMap = new HashMap<>();
        File localeDir = new File(dataFolder, "lang");
        if (!localeDir.exists()) localeDir.mkdirs();
        GlobalUtils.unpackResource("lang/ar.yml", new File(localeDir, "ar.yml"));
        GlobalUtils.unpackResource("lang/en.yml", new File(localeDir, "en.yml"));
        GlobalUtils.unpackResource("lang/es.yml", new File(localeDir, "es.yml"));
        GlobalUtils.unpackResource("lang/fr.yml", new File(localeDir, "fr.yml"));
        GlobalUtils.unpackResource("lang/hi.yml", new File(localeDir, "hi.yml"));
        GlobalUtils.unpackResource("lang/it.yml", new File(localeDir, "it.yml"));
        GlobalUtils.unpackResource("lang/jp.yml", new File(localeDir, "jp.yml"));
        GlobalUtils.unpackResource("lang/pt.yml", new File(localeDir, "pt.yml"));
        GlobalUtils.unpackResource("lang/ru.yml", new File(localeDir, "ru.yml"));
        GlobalUtils.unpackResource("lang/zh.yml", new File(localeDir, "zh.yml"));
        for (File ymlFile : localeDir.listFiles(f -> f.getName().endsWith(".yml"))) {
            Configuration config = YamlConfiguration.loadConfiguration(ymlFile);
            localizationMap.put(ymlFile.getName().replace(".yml", ""), new Localization(config));
        }
    }

    public static Localization getLocalization(String locale) {
        if (localizationMap.containsKey(locale)) return localizationMap.get(locale);
        String first = locale.split("_")[0];
        if (localizationMap.containsKey(first)) return localizationMap.get(first);
        return localizationMap.get("en");
    }

    public String getPrefix() {
        return config.getString("prefix", "&l[&5&l2d&d&l2d&r&l]");
    }

    public String get(String key) {
        return config.getString(key, String.format("Unknown key %s", key))
                .replace("%prefix%", getPrefix());
    }
    public List<String> getStringList(String key) {
        return config.getStringList(key).stream()
                .map(s -> s.replace("%prefix%", getPrefix()))
                .toList();
    }
}
