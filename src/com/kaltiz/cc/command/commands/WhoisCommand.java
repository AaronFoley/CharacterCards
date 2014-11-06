package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.util.ChatTools;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class WhoisCommand extends BasicCommand
{
    public WhoisCommand(CharacterCards plugin)
    {
        super(plugin, "cc whois");
        description = "Search for a Player";
        usage = "/cc whois <search>";
        minArgs = 1;
        maxArgs = 100;
        aliases = new String[] {"cc whois"};
        permission = "cc.command.whois";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        String data = "";
        for(int i = 1; i < args.length - 1; i++)
            data += args[i] + " ";

        data += args[args.length -1];

        Set<RpChar> PossibleMatches = plugin.getCharacterManager().search(data);

        sender.sendMessage(ChatTools.formatTitle("Search Results"));
        if (PossibleMatches.isEmpty())
        {
            sender.sendMessage("No Matches found for " + data);
            return true;
        }

        for (RpChar match : PossibleMatches){
            sender.sendMessage(match.getPlayer().getName() + " - " + match.getField(plugin.getCharacterManager().searchField));
        }

        return true;
    }
}
