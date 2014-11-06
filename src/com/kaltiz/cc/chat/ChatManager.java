package com.kaltiz.cc.chat;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;

import java.util.List;

public abstract class ChatManager
{
    protected final CharacterCards plugin;
    public final List<String> displayNameFields;

    public ChatManager(CharacterCards plugin)
    {
        this.plugin = plugin;

        this.displayNameFields = plugin.getConfig().getStringList("displayNameFields");
    }

    public abstract void setUpChannels();

    public String getDisplayName(RpChar character)
    {
        String name = "";

        for ( String field : displayNameFields)
        {
            name += character.getField(field.toLowerCase()) + " ";
        }
        return name.trim();
    }
}
