package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.command.BasicCommand;
import org.bukkit.command.CommandSender;

public class AdminRemoveCommand extends BasicCommand
{
    public AdminRemoveCommand(CharacterCards plugin)
    {
        super(plugin, "ccadmin remove");
        description = "Removes a Player's Character";
        usage = "/ccadmin remove <player>";
        minArgs = 1;
        maxArgs = 1;
        aliases = new String[] {"ccadmin clear", "ccadmin delete"};
        permission = "cc.admin.command.remove";
        allowServer = true;
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        RpChar toRemove = plugin.getCharacterManager().getCharacter(args[0]);

        if (toRemove == null)
        {
            sender.sendMessage(args[0] + " does not have a character.");
            return true;
        }

        plugin.getCharacterManager().removeCharacter(toRemove);

        sender.sendMessage(args[0] + "'s character has been removed.");

        return true;
    }
}
