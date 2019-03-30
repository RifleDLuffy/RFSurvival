package com.rifledluffy.survival.config;

import com.rifledluffy.api.config.handle.ConfigFile;
import com.rifledluffy.survival.RFSurvival;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private RFSurvival plugin = RFSurvival.inst();
    private ConfigFile configFile;

    public void setup() {

        configFile = new ConfigFile("config", plugin.getDataFolder(), true, RFSurvival.inst());
        configFile.reload();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
    }

    public FileConfiguration getConfig() {
        return configFile.getConfig();
    }

    public void reloadConfig() {
        configFile.reload();
    }
}
