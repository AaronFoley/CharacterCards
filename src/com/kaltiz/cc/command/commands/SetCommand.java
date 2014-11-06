package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends BasicCommand
{
    public SetCommand(CharacterCards plugin)
    {
        super(plugin, "cc set");
        description = "Set a field on your character card";
        usage = "/cc set <field> <value>";
        minArgs = 2;
        maxArgs = 100;
        aliases = new String[] {"cc set"};
        permission = "cc.command.set";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        Field field = this.plugin.getCharacterManager().getField(args[0]);
        // Check Field Exists
        if (field == null)
        {
            sender.sendMessage("The Field `" + args[0] + "` does not exist");
            return true;
        }

        // Check they have access to set this filed
        if (!plugin.getPerms().has(sender, "cc.field." + field.getName()))
            return true;

        // Get the Data
        String data = "";
        for(int i = 1; i < args.length; i++)
            data += args[i] + " ";

        data = data.trim();

        // If they have access to colours, colourise
        if (plugin.getPerms().has(sender, "cc.color"))
            data = ChatColor.translateAlternateColorCodes('&',data);

        // if not valid input
        if (!field.validateInput(data))
        {
            sender.sendMessage("  " + field.getName() + " : " + field.getDesc());
            return true;
        }

        RpChar playerChar = plugin.getCharacterManager().getCharacter(sender.getName());
        // If they Don't have a Character create one
        if (playerChar == null)
        {
            playerChar = plugin.getStorage().createNewCharacter(((Player) sender).getPlayer());
        }

        playerChar.setField(args[0], data);
        sender.sendMessage("Character Card Updated!");

        return true;
    }
}
