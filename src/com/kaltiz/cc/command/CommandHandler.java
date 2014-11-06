package com.kaltiz.cc.command;

import com.kaltiz.cc.CharacterCards;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler
{
    protected LinkedHashMap<String,Command> commands;
    protected LinkedHashMap<String, Command> aliases;
    private final CharacterCards plugin;

    public CommandHandler(CharacterCards plugin)
    {
        this.plugin = plugin;
        this.commands = new LinkedHashMap<String, Command>();
        this.aliases = new LinkedHashMap<String, Command>();
    }

    public Map<String, Command> getCommands()
    {
        return Collections.unmodifiableMap(this.commands);
    }

    public Map<String, Command> getAliases()
    {
        return Collections.unmodifiableMap(this.aliases);
    }

    public void addCommand(Command command)
    {
        commands.put(command.getName(), command);
        for (String alias : command.getAliases())
            aliases.put(alias.toLowerCase(), command);
    }

    public void removeCommand(Command command)
    {
        commands.remove(command.getName());
        for (String alias : command.getAliases())
            aliases.remove(alias.toLowerCase());
    }

    public boolean onCommand(CommandSender sender, String cmdStr, String[] args)
    {
        // Now that we have the command we need to find the specific version
        int i = 0;
        for (i = 0; i < args.length; i++)
        {
            if (commands.containsKey(cmdStr + ' ' + args[i]) ||
                aliases.containsKey(cmdStr + ' ' + args[i]))
            {
                cmdStr += ' ' + args[i];
            }
            else
            {
                break;
            }
        }

        // Now get the command
        Command cmd = null;

        if (commands.containsKey(cmdStr))
            cmd = commands.get(cmdStr);
        else
            cmd = aliases.get(cmdStr);

        // Ensure the Command Exists inside Commands
        if (cmd != null)
        {
            // Check the Perms
            if (!plugin.getPerms().has(sender, cmd.getPermission()))
            {
                sender.sendMessage("Insufficient Permissions.");
                return true;
            }

            if (!cmd.isPlayerAllowed() && sender instanceof Player)
            {
                sender.sendMessage("Only the server can execute this command.");
            }
            else if (!cmd.isServerAllowed() && !(sender instanceof Player))
            {
                System.out.println("This command cannot be executed from the console");
                return true;
            }

            String[] realArgs = new String[0];

            // Deal with some args
            if (i < args.length)
            {
                realArgs = Arrays.copyOfRange(args, i, args.length);

                // Check if a Help Command
                if (realArgs[0].equals("?"))
                {
                    displayHelp(cmd, sender);
                    return true;
                }
            }

            // Check the Command Args
            if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments() )
            {
                displayHelp(cmd, sender);
                return true;
            }

            // Execute Command
            cmd.execute(sender,cmdStr,realArgs);
        }
        // Else Show help
        else
        {
            cmd = commands.get("cc help");
            cmd.execute(sender,cmdStr,new String[0]);
        }

        return true;
    }

    public void displayHelp(Command cmd, CommandSender sender)
    {
        sender.sendMessage("Command: " + cmd.getName());
        sender.sendMessage("Desc: " + cmd.getDescription());
        sender.sendMessage("Usage: " + cmd.getUsage());
    }
}
