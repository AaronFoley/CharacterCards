package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.command.BasicCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends BasicCommand
{
    public RemoveCommand(CharacterCards plugin)
    {
        super(plugin, "cc remove");
        description = "Removes a Player's Character";
        usage = "/cc remove";
        minArgs = 0;
        maxArgs = 0;
        aliases = new String[] {"cc clear", "cc delete"};
        permission = "cc.command.unset";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        RpChar toRemove = plugin.getCharacterManager().getCharacter((Player) sender);

        if (toRemove == null)
        {
            sender.sendMessage(sender.getName() + " does not have a character.");
            return true;
        }

        plugin.getCharacterManager().removeCharacter(toRemove);

        sender.sendMessage(sender.getName() + "'s character has been removed.");

        return true;
    }
}
