package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.command.Command;
import com.kaltiz.cc.util.ChatTools;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public class HelpCommand extends BasicCommand
{
    public HelpCommand(CharacterCards plugin)
    {
        super(plugin, "cc help");
        description = "Shows the Help Menu";
        usage = "/cc help [page]";
        minArgs = 0;
        maxArgs = 1;
        permission = "cc.command.help";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        // Get the Commands to Display
        List<Command> commands = new LinkedList<Command>();

        for (Command command : this.plugin.getCommandHandler().getCommands().values())
        {
            if (!command.isShownOnHelpMenu() || !this.plugin.getPerms().has(sender, command.getPermission()))
                continue;

            commands.add(command);
        }

        // Work out Pages
        int currPage = 0;
        if ( args.length != 0)
        {
            try { currPage = Integer.parseInt(args[0]) - 1; }
            catch (NumberFormatException ignored) {}
        }

        int totalPages = (int) Math.ceil(commands.size() / 5.0);
        // Sanity Check
        if (currPage < 0) currPage = 0;
        if (currPage > totalPages) currPage = totalPages - 1;

        sender.sendMessage(ChatTools.formatTitle("Character Cards [" + (currPage + 1) + "/" + totalPages + "]"));

        int start = currPage * 5;

        for (int i = start; i < start + 5 && i < commands.size(); i++ )
        {
            Command command = commands.get(i);
            sender.sendMessage("  " + ChatColor.GOLD + command.getUsage() + ": " + ChatColor.WHITE + command.getDescription());
        }

        sender.sendMessage("/cc help <page> to view specific page");
        sender.sendMessage("For specific command help, type /command ?");

        return true;
    }

    @Override
    public boolean isShownOnHelpMenu()
    {
        return false;
    }
}
