package com.kaltiz.cc.character;

import java.util.*;

import com.kaltiz.cc.CharacterCards;

import com.kaltiz.cc.character.field.Field;
import com.kaltiz.cc.character.field.IntField;
import com.kaltiz.cc.character.field.StringField;
import com.kaltiz.cc.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class CharacterManager
{
	private final CharacterCards plugin;
	private final Map<UUID,RpChar> characters;
    private final Map<String,RpChar> charNames;
	private Map<String,Field> fields;

    // Field used to search for a Character
	public final String searchField;

	public CharacterManager(CharacterCards plugin)
    {
		this.plugin = plugin;
		this.characters = new HashMap<UUID,RpChar>();
        this.charNames = new HashMap<String,RpChar>();
        fields = new LinkedHashMap<String,Field>();
		loadFields();
		searchField = plugin.getConfig().getString("searchField");
	}

    // Deletes a Character
	public void removeCharacter(RpChar character)
    {
		if (!characters.containsKey(character.getPlayer().getUniqueId()))
			return;

		characters.remove(character.getPlayer().getUniqueId());
        charNames.remove(character.getPlayer().getName().toLowerCase());
		plugin.getStorage().removeCharacter(character);
	}

    public void addCharacter(RpChar character)
    {
        this.characters.put(character.getPlayer().getUniqueId(),character);
        this.charNames.put(character.getPlayer().getName().toLowerCase(), character);
    }

    // Searches for a Character based upon the default search field
	public Set<RpChar> search(String str)
    {
		return search(str,this.searchField);
	}

    // Searches for a Character based in the search field
	public Set<RpChar> search(String str, String search)
    {
		Set<RpChar> chars = new HashSet<RpChar>();
		
		for (RpChar character : characters.values()){
			String cmp = character.getField(this.searchField.toLowerCase());
			if (cmp.toLowerCase().contains(str.toLowerCase()))
				chars.add(character);
		}
		return chars;
	}

    // Gets a Character based on a Player's Name
	public RpChar getCharacter(String name)
    {
        if (this.charNames.containsKey(name.toLowerCase()))
        {
            return this.charNames.get(name.toLowerCase());
        }
        else
        {
            return null;
        }
	}

    // Gets a Character based on OfflinePlayer
	public RpChar getCharacter(OfflinePlayer player)
    {
		if (characters.containsKey(player.getUniqueId()))
        {
			return characters.get(player.getUniqueId());
		}
        // Else create a new Character
		return plugin.getStorage().createNewCharacter(player);
	}

    // Gets
	public Field getField(String index)
    {
		if (fields.containsKey(index.toLowerCase()))
        {
			return fields.get(index.toLowerCase());
		}
		return null;
	}

    public Map<String,Field> getFields()
    {
        return Collections.unmodifiableMap(this.fields);
    }
	
	private void loadFields()
    {
		ConfigurationSection fieldSections = plugin.getConfig().getConfigurationSection("fields");
		
		for( String fieldName : fieldSections.getKeys(false))
        {
            ConfigurationSection fieldSection = fieldSections.getConfigurationSection(fieldName);

            String type = fieldSection.getString("type","String");
            String desc = fieldSection.getString("desc","");

            Field field;

            if (type.equalsIgnoreCase("string"))
            {
                String defVal = fieldSection.getString("default","");
                List<String> options = fieldSection.getStringList("options");
                int length = fieldSection.getInt("length",32);
                field = new StringField(plugin,fieldName,desc,defVal,options,length);
            }
            else if (type.equalsIgnoreCase("int"))
            {
                int defVal = fieldSection.getInt("default",0);
                List<Integer> options = fieldSection.getIntegerList("options");
                int min = fieldSection.getInt("min",Integer.MIN_VALUE);
                int max = fieldSection.getInt("max",Integer.MAX_VALUE);
                field = new IntField(plugin,fieldName,desc,defVal,options,min,max);
            }
            else
            {
                plugin.getLogger().warning("Unsupported Field Type: " + type + "!");
                continue;
            }

            // Put it into the fields stuff
            fields.put(fieldName.toLowerCase(),field);
        }
	}

    public Map<UUID, RpChar> getCharacters()
    {
        return Collections.unmodifiableMap(this.characters);
    }
}