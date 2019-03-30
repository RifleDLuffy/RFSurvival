package com.rifledluffy.survival;

import com.rifledluffy.survival.command.CommandManager;
import com.rifledluffy.survival.config.ConfigManager;
import com.rifledluffy.survival.domestication.AnimalManager;
import com.rifledluffy.survival.falling.FallingManager;
import com.rifledluffy.survival.nms.NMSManager;
import com.rifledluffy.survival.nms.handlers.NMSHandler;
import com.rifledluffy.survival.utility.HologramManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RFSurvival extends JavaPlugin {

    private static RFSurvival instance;

    private CommandManager commandManager;
    private ConfigManager configManager;

    private FallingManager fallingManager;
    private NMSManager nmsManager;
    private AnimalManager animalManager;
    private HologramManager hologramManager;

    public static RFSurvival inst() {
        return instance;
    }

    @Override
    public void onEnable() {
        setInstance(this);

        nmsManager = new NMSManager();
        nmsManager.setup();

        commandManager = new CommandManager();
        commandManager.setup();

        configManager = new ConfigManager();
        configManager.setup();

        animalManager = new AnimalManager();
        if (animalManager.isEnabled()) {
            animalManager.reload();
            animalManager.generateAnimals();
            animalManager.register(this);
        }

        fallingManager = new FallingManager();

        hologramManager = new HologramManager();
        if (hologramManager.useHolograms()) hologramManager.setup();

        getLogger().info("Rifle's Survival has been enabled!");
    }

    public void reload() {
        animalManager.reload();
        fallingManager.unregister();

        fallingManager = new FallingManager();
        hologramManager.reload();
    }

    private void setInstance(RFSurvival instance) {
        RFSurvival.instance = instance;
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving Configuration Files!");
        getLogger().info("Rifle's Survival has been disabled!");
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FallingManager getFallingManager() {
        return fallingManager;
    }

    public NMSHandler getNavigation() {
        return nmsManager.getNavigation();
    }

    public AnimalManager getAnimalManager() {
        return animalManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
