package com.kaltiz.cc.character.field;

import com.kaltiz.cc.CharacterCards;

import java.util.List;

public class IntField extends Field<Integer>
{

    private int min;
    private int max;

    public IntField(CharacterCards plugin, String name, String desc, int defaultValue, List<Integer> opts, int min, int max)
    {
        super(plugin, name, desc, defaultValue, opts);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validateInput(String data)
    {
        // Check it can be safely converted to an int
        int val;
        try{
            val = Integer.parseInt(data);
        }
        catch(NumberFormatException nfe){
            return false;
        }

        // Check if it is a valid option
        if (!this.options.isEmpty() && !this.options.contains(val))
            return false;

        // Check for Valid Length
        if (val < min || val > max)
            return false;

        // Otherwise all Good
        return true;
    }

    @Override
    public String getDesc() {
        String descStr = name + ": " + desc;
        descStr += "Min: " + min + " Max: " + max;

        // If the field is limited to options
        if (!options.isEmpty())
        {
            descStr += "Options: <";
            for (int opt : options)
                descStr += opt + "|";

            // Remove the last |
            descStr = descStr.substring(0,descStr.length()-1);
            descStr += ">";
        }

        return descStr;
    }
}
