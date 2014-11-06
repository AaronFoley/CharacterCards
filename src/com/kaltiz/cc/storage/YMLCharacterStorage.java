package com.kaltiz.cc.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.character.field.Field;

public class YMLCharacterStorage extends CharacterStorage
{
	private final File playerFolder;

	public YMLCharacterStorage(CharacterCards plugin)
    {
        super(plugin);
        // Setup the Characters Folder
		this.playerFolder = new File(plugin.getDataFolder(), "characters");
        this.playerFolder.mkdirs();
    }

    // Loads all the Characters
	@Override
    public void loadCharacters()
    {
        File[] players = playerFolder.listFiles();

        if (players == null) return;

        for (File playerFile : players)
        {
            if (playerFile.getName().endsWith(".yml"))
                plugin.getCharacterManager().addCharacter(loadCharacter(playerFile));
        }
    }

    // Loads the Player from the file
	private RpChar loadCharacter(File playerFile)
    {
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(playerFile.getName().substring(0, playerFile.getName().length() - 4)));
        RpChar character = new RpChar(player,plugin);

		Configuration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
			
        for (Field field : plugin.getCharacterManager().getFields().values())
        {
            if (!playerConfig.isSet(field.getName()))
                continue;

            character.setField(field.getName(), playerConfig.getString(field.getName()));
        }

        return character;
	}
	
	@Override
	public void saveCharacter(RpChar character)
    {
		String name = character.getPlayer().getUniqueId().toString();
		
		File playerFile = new File(playerFolder, name + ".yml");
		FileConfiguration playerConfig = new YamlConfiguration();

        for (Map.Entry<String,String> field : character.getFields().entrySet())
        {
            playerConfig.set(field.getKey(), field.getValue());
        }
		
		try {
			playerConfig.save(playerFile);
		} catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Problem Saving " + character.getPlayer().getName() + "'s character");
			e.printStackTrace();
		}
	}

    @Override
	public void removeCharacter(RpChar character)
    {
		String name = character.getPlayer().getUniqueId().toString();
		
		File playerFile = new File(playerFolder, name + ".yml");
		playerFile.delete();
	}

    @Override
    public void shutdown()
    {
        // Save all the characters
        Map<UUID, RpChar> players = plugin.getCharacterManager().getCharacters();

        for (RpChar character : players.values())
        {
            this.saveCharacter(character);
        }
    }
}
