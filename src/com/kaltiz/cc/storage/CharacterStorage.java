package com.kaltiz.cc.storage;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;

import org.bukkit.OfflinePlayer;

public abstract class CharacterStorage
{
    protected final CharacterCards plugin;

	public CharacterStorage(CharacterCards plugin)
    {
        this.plugin = plugin;
	}

	public abstract void loadCharacters();
	
	public abstract void saveCharacter(RpChar character);
	
	public RpChar createNewCharacter(OfflinePlayer player)
    {
		RpChar character = new RpChar(player,plugin);
		this.saveCharacter(character);
        plugin.getCharacterManager().addCharacter(character);
		return character;
	}

	public abstract void removeCharacter(RpChar character);
	
	public abstract void shutdown();
}
