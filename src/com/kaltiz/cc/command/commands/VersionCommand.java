package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.util.ChatTools;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCommand extends BasicCommand
{
    public VersionCommand(CharacterCards plugin)
    {
        super(plugin, "cc version");
        description = "Displays version information about this plugin";
        usage = "/cc version";
        minArgs = 0;
        maxArgs = 0;
        aliases = new String[] {"cc about"};
        permission = "cc.command.version";
        allowServer = true;
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        sender.sendMessage(ChatTools.formatTitle("Character Cards"));
        sender.sendMessage("  " + ChatColor.GOLD + "Author: " + ChatColor.WHITE + "Kaltiz");
        sender.sendMessage("  " + ChatColor.GOLD + "Version: " + ChatColor.WHITE + "1.2 - Athery's");
        sender.sendMessage("  " + ChatColor.GOLD + "Licence: " + ChatColor.WHITE + "MIT");
        return true;
    }
}
