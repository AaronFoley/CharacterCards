package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import org.bukkit.command.CommandSender;

public class AdminUnsetCommand extends BasicCommand
{
    public AdminUnsetCommand(CharacterCards plugin)
    {
        super(plugin, "ccadmin unset");
        description = "Unset a field";
        usage = "/ccadmin unset <player>";
        minArgs = 2;
        maxArgs = 2;
        aliases = new String[] {};
        permission = "cc.admin.command.unset";
        allowServer = true;
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        RpChar character = plugin.getCharacterManager().getCharacter(args[0]);
        if (character == null) return true;

        Field field = this.plugin.getCharacterManager().getField(args[1]);
        // Check Field Exists
        if (field == null)
        {
            sender.sendMessage("The Field `" + args[1] + "` does not exist");
            return true;
        }

        character.removeField(field.getName());
        sender.sendMessage("Field Removed!");

        return true;
    }
}
