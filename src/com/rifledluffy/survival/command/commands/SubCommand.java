package com.rifledluffy.survival.command.commands;

import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.command.CommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected final String name;

    public SubCommand(String name, boolean add) {
        this.name = name;
        this.plugin = RFSurvival.inst();

        if (add) {
            cmdManager = plugin.getCommandManager();
            cmdManager.addCommand(this);
        }
    }

    /**
     * Blank Constructor.<br>
     * Adds it itself to the command manager
     * @param name the name of the command EG: /c [reload]
     */
    public SubCommand(String name) {
        this(name, true);
    }

    protected RFSurvival plugin;
    protected CommandManager cmdManager;

    /**
     * The prefix that all commands share
     */
    protected final static String prefix = "§8[§6RFSurvival§8]";

    /**
     * The message that occurs when console tried to execute a player only command
     */
    protected final static String playerOnly = ":§cOnly players can use this command.";

    /**
     * The message that occurs when players tried to execute a console only command
     */
    protected final static String consoleOnly = ":§cOnly console can use this command.";

    /**
     * Intial method that triggers different methods based on the instance of the sender
     * @param sender The CommandSender that triggered the command
     * @param args The message split by spaces
     */
    public void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) onCommand((Player)sender, args);
        else if (sender instanceof ConsoleCommandSender) onCommand((ConsoleCommandSender)sender, args);
        else return;
    }

    /**
     * Runs the SubCommand through console and their message split by spaces
     * @param sender The console running the SubCommand
     * @param args The message split by spaces
     */
    public abstract void onCommand(ConsoleCommandSender sender, String[] args);

    /**
     * Runs the SubCommand with a player and their message split by spaces
     * @param player The player running the SubCommand
     * @param args The message split by spaces
     */
    public abstract void onCommand(Player player, String[] args);

    /**
     * @return The name of the SubCommand
     */
    public String name() {
        return this.name;
    }

    /**
     * The alaises the command can occupy
     * @return An array of alaises
     */
    public String[] aliases() {
        return new String[0];
    }

}
