package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.util.ChatTools;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CardCommand extends BasicCommand
{
    public CardCommand(CharacterCards plugin)
    {
        super(plugin, "cc card");
        description = "View a Player's Character Card";
        usage = "/cc card <player>";
        minArgs = 0;
        maxArgs = 1;
        aliases = new String[] {};
        permission = "cc.command.card";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        RpChar character;

        String name = null;

        if (args.length > 0)
            name = args[0];
        else
            name = sender.getName();

        character = plugin.getCharacterManager().getCharacter(name);

        if (character == null)
        {
            sender.sendMessage(name + " does not have a character.");
            return true;
        }

        sender.sendMessage(ChatTools.formatTitle(character.getPlayer().getName()));

        Map<String, String> fields = character.getFields();

        for (Field field : plugin.getCharacterManager().getFields().values())
        {
            if (fields.containsKey(field.getName()))
            {
                sender.sendMessage("  " + ChatColor.GOLD + field.getName() + " : " + ChatColor.WHITE + fields.get(field.getName()));
            }
        }

        return true;
    }
}
