package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.command.BasicCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnsetFieldCommand extends BasicCommand
{
    public UnsetFieldCommand(CharacterCards plugin)
    {
        super(plugin, "cc unset");
        description = "Unset a field";
        usage = "/cc unset <field>";
        minArgs = 1;
        maxArgs = 1;
        aliases = new String[] {"cc unset"};
        permission = "cc.command.unset";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (!this.plugin.getCharacterManager().getFields().containsKey(args[0]))
        {
            sender.sendMessage("The Field `" + args[0] + "` does not exist");
            return true;
        }

        RpChar playerChar = plugin.getCharacterManager().getCharacter(sender.getName());
        if (playerChar == null) return true;

        playerChar.removeField(args[0]);
        sender.sendMessage("Field Removed!");

        return true;
    }
}
