package com.kaltiz.cc.character.field;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.kaltiz.cc.CharacterCards;

public abstract class Field<T>
{
    protected CharacterCards plugin;

    protected final String name;
    protected final String desc;
    protected T defaultValue;
    protected boolean unique = false;
    protected List<T> options = null;

    public Field(CharacterCards plugin, String name, String desc, T defaultValue, List<T> opts)
    {
        this.plugin = plugin;
        this.name = name;
        this.desc = desc;
        this.defaultValue = defaultValue;
        this.options = opts;
    }

    public abstract boolean validateInput(String data);

    public String getName() { return name; }

    public String getDefaultValue() { return defaultValue.toString(); }

    public abstract String getDesc();

}
