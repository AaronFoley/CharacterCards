package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.command.Command;
import com.kaltiz.cc.util.UUIDUtils;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class AdminMultiCommand extends BasicCommand
{
    public AdminMultiCommand(CharacterCards plugin)
    {
        super(plugin, "ccadmin");
        description = "";
        usage = "/ccadmin";
        minArgs = 1;
        maxArgs = 100;
        aliases = new String[] {};
        permission = "cc.admin";
        allowServer = true;
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        Command othercmd = null;

        Map<String, Field> fields = plugin.getCharacterManager().getFields();
        Map<String, Command> commands = plugin.getCommandHandler().getCommands();

        // Check for <player>
        if (UUIDUtils.getOfflinePlayerByName(args[0]) != null)
        {
            // ccadmin <player> <field> <value>
            if (args.length > 2 && fields.containsKey(args[1].toLowerCase()))
            {
                othercmd = commands.get("ccadmin set");
                args = new String[] {args[0], args[1], args[2]};
            }
            // ccadmin <player> unset <field>
            else if (args.length == 3 && args[1].equalsIgnoreCase("unset") && fields.containsKey(args[2].toLowerCase()))
            {
                othercmd = commands.get("ccadmin unset");
                args = new String[] {args[0], args[2]};
            }
            // ccadmin <player> remove
            else if (args.length == 2 && args[1].equalsIgnoreCase("remove"))
            {
                othercmd = commands.get("ccadmin remove");
                args = new String[] {args[0]};
            }
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
