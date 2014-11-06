package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class AdminSetCommand extends BasicCommand
{
    public AdminSetCommand(CharacterCards plugin)
    {
        super(plugin, "ccadmin set");
        description = "Set a field on a player's character card";
        usage = "/ccadmin set <player> <field> <value>";
        minArgs = 3;
        maxArgs = 100;
        aliases = new String[] {};
        permission = "cc.admin.command.set";
        allowServer = true;
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        RpChar character = plugin.getCharacterManager().getCharacter(args[0]);
        // Check the Player has a Character
        if (character == null)
        {
            OfflinePlayer player = UUIDUtils.getOfflinePlayerByName(args[0]);

            if (player == null)
            {
                sender.sendMessage("Could not find player `" + args[0] + "` has not visited the server");
                return true;
            }

            character = plugin.getStorage().createNewCharacter(player);
        }

        Field field = this.plugin.getCharacterManager().getField(args[1]);
        // Check Field Exists
        if (field == null)
        {
            sender.sendMessage("The Field `" + args[1] + "` does not exist");
            return true;
        }

        // Get the Data
        String data = "";
        for(int i = 2; i < args.length; i++)
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

        character.setField(field.getName(), data);
        sender.sendMessage("Character Card Updated!");

        return true;
    }
}
