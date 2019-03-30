package com.rifledluffy.survival.command.commands;

import com.rifledluffy.survival.config.ConfigManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage(prefix + playerOnly);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("rfcbattle.manage")) return;
        ConfigManager configManager = plugin.getConfigManager();

        configManager.setup();
        player.sendMessage(prefix + ":ยงa Plugin Reloaded");
    }
}
