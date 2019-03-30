package com.rifledluffy.survival.command;

import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.command.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements CommandExecutor {

    private RFSurvival plugin = RFSurvival.inst();

    private ArrayList<SubCommand> commands = new ArrayList<>();

    public CommandManager(){}

    private final String main = "rfsurvival";

    public void setup() {
        plugin.getCommand(main).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase(main)) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Missing Arguments. Type /rfcbattle help for info");
                return true;
            }

            SubCommand target = this.get(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand");
                return true;
            }

            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(args));
            arrayList.remove(0);

            try {
                target.onCommand(sender, args);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error has occurred.");
                e.printStackTrace();
            }
        }

        return true;
    }

    private SubCommand get(String name) {

        for (SubCommand sc : this.commands) {
            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }

            }
        }
        return null;
    }

    public void addCommand(SubCommand command) {
        this.commands.add(command);
    }

}
