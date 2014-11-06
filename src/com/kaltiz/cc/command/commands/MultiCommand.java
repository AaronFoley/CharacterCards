package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.command.Command;
import com.kaltiz.cc.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class MultiCommand extends BasicCommand
{
    public MultiCommand(CharacterCards plugin)
    {
        super(plugin, "cc");
        description = "";
        usage = "/cc";
        minArgs = 0;
        maxArgs = 100;
        aliases = new String[] {};
        permission = "cc.command";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        Command othercmd = null;

        Map<String, Field> fields = plugin.getCharacterManager().getFields();
        Map<String, Command> commands = plugin.getCommandHandler().getCommands();

        if (args.length == 0)
        {
            othercmd = commands.get("cc card");
        }
        // Check for <field>
        else if (fields.containsKey(args[0].toLowerCase()))
        {
            // cc <field> <value>
            if (args.length > 1)
            {
                othercmd = commands.get("cc set");
            }
            // cc <field>
            else
            {
                Field field = fields.get(args[0].toLowerCase());
                sender.sendMessage("  " + ChatColor.GOLD + field.getName() + ": " + ChatColor.WHITE + field.getDesc());

                return true;
            }
        }
        // Check for <player>
        else if (UUIDUtils.getOfflinePlayerByName(args[0]) != null)
        {
            othercmd = commands.get("cc card");
        }

        // Else show help
        if (othercmd == null)
            othercmd = commands.get("cc help");

        // Check permissions
        if (!plugin.getPerms().has(sender, othercmd.getPermission()))
        {
            sender.sendMessage("Insufficient Permissions.");
            return true;
        }

        othercmd.execute(sender, cmd, args);
        return true;
    }

    @Override
    public boolean isShownOnHelpMenu()
    {
        return false;
    }
}
