package com.kaltiz.cc.command.commands;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.command.BasicCommand;
import com.kaltiz.cc.util.ChatTools;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public class FieldsCommand extends BasicCommand
{
    public FieldsCommand(CharacterCards plugin)
    {
        super(plugin, "cc fields");
        description = "Shows a list of all available fields";
        usage = "/cc fields [page]";
        minArgs = 0;
        maxArgs = 1;
        aliases = new String[] {};
        permission = "cc.command.fields";
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args)
    {
        // Get the Commands to Display
        List<Field> fields = new LinkedList<Field>();

        for (Field field : this.plugin.getCharacterManager().getFields().values())
        {
            if (!this.plugin.getPerms().has(sender, "cc.field." + field.getName()))
                continue;

            fields.add(field);
        }

        // Work out Pages
        int currPage = 0;
        if ( args.length != 0)
        {
            try { currPage = Integer.parseInt(args[0]) - 1; }
            catch (NumberFormatException ignored) {}
        }

        int totalPages = (int) Math.ceil(fields.size() / 5.0);
        // Sanity Check
        if (currPage < 0) currPage = 0;
        if (currPage > totalPages) currPage = totalPages - 1;

        sender.sendMessage(ChatTools.formatTitle("Character Cards - Fields [" + (currPage + 1) + "/" + totalPages + "]"));

        int start = currPage * 5;

        for (int i = start; i < start + 5 && i < fields.size(); i++ )
        {
            Field field = fields.get(i);
            sender.sendMessage("  " + ChatColor.GOLD + field.getName() + ": " + ChatColor.WHITE + field.getDesc());
        }

        sender.sendMessage("/cc fields <page> to view specific page");

        return true;
    }
}
