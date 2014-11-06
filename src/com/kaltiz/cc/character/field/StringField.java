package com.kaltiz.cc.character.field;

import com.kaltiz.cc.CharacterCards;

import java.util.List;

public class StringField extends Field<String>
{
    private int length;

    public StringField(CharacterCards plugin, String name, String desc, String defaultValue, List<String> opts, int length)
    {
        super(plugin, name, desc, defaultValue, opts);
        this.length = length;
    }

    @Override
    public boolean validateInput(String data)
    {
        // Check if it is a valid option
        if (!this.options.isEmpty() && !this.options.contains(data))
            return false;

        // Check for Valid Length
        if (data.length() > this.length)
            return false;

        // Else it's all good
        return true;
    }

    @Override
    public String getDesc()
    {
        String descStr;
        // If the field is limited to options
        if (!options.isEmpty())
        {
            descStr = "Options: <";
            for (String opt : options)
                descStr += opt + "|";

            // Remove the last |
            descStr = descStr.substring(0,descStr.length()-1);
            descStr += ">";
        }
        else
        {
            descStr = "MaxLength: " + length;
        }

        return descStr;
    }
}
