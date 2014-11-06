package com.kaltiz.cc.character;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.util.JsonUtil;
import org.bukkit.OfflinePlayer;

public class RpChar
{
	private final CharacterCards plugin;
	private final OfflinePlayer player;
	private HashMap<String, String> fieldValues;
	
	public RpChar(OfflinePlayer player, CharacterCards plugin)
    {
        this.plugin = plugin;
		this.player = player;
		this.fieldValues = new HashMap<String,String>();
    }

    public RpChar(OfflinePlayer player, CharacterCards plugin, HashMap<String, String> fields)
    {
        this.plugin = plugin;
        this.player = player;
        this.fieldValues = fields;
    }
	
	public OfflinePlayer getPlayer()
    {
		return this.player;
	}
	
	public void removeField(String indx)
    {
        Field field = plugin.getCharacterManager().getField(indx);

		if (field != null && this.fieldValues.containsKey(field.getName()))
        {
			this.fieldValues.remove(field.getName());
		}

        // If this is the last field, delete th character
        if (this.fieldValues.size() == 0)
            this.plugin.getCharacterManager().removeCharacter(this);
	}
	
	public boolean setField(String indx, String data)
    {
		Field field = plugin.getCharacterManager().getField(indx);
		
		if (field == null || !field.validateInput(data)) return false;
		
		if (this.fieldValues.containsKey(field.getName()))
        	this.fieldValues.remove(field.getName());


		this.fieldValues.put(field.getName(), data);

		return true;
	}

	public String getField(String indx)
    {
		Field field = plugin.getCharacterManager().getField(indx);
		
		if (field == null) return null;
		
		if (this.fieldValues.containsKey(field.getName()))
			return this.fieldValues.get(field.getName());

		return field.getDefaultValue();
	}

    public Map<String,String> getFields()
    {
        return Collections.unmodifiableMap(this.fieldValues);
    }
}
