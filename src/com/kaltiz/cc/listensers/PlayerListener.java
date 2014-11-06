package com.kaltiz.cc.listensers;

import com.kaltiz.cc.CharacterCards;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.kaltiz.cc.character.RpChar;
import com.kaltiz.cc.util.ChatTools;

import java.util.Map;

public class PlayerListener implements Listener
{
	private CharacterCards plugin;

	public PlayerListener(CharacterCards plugin)
    {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event)
    {
		Player player = event.getPlayer();
		RpChar character = plugin.getCharacterManager().getCharacter(player);
		if (character == null)
			return;
		plugin.getStorage().saveCharacter(character);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerKicked(PlayerKickEvent event)
    {
		Player player = event.getPlayer();
		RpChar character = plugin.getCharacterManager().getCharacter(player);
		if (character == null)
			return;
        plugin.getStorage().saveCharacter(character);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerEntityInteract(PlayerInteractEntityEvent event)
    {
		if (!(event.getRightClicked() instanceof Player) || !(event.getPlayer().isSneaking())) {
			return;
		}
		
		if (!plugin.getPerms().has(event.getPlayer(), "cc.command.card")){
			return;
		}

		Player rightclicked = (Player) event.getRightClicked();

        RpChar character = plugin.getCharacterManager().getCharacter(rightclicked);

        if (character == null)
        {
            event.getPlayer().sendMessage(rightclicked.getName() + " does not have a character.");
            return;
        }

        event.getPlayer().sendMessage(ChatTools.formatTitle(character.getPlayer().getName()));

        for (Map.Entry<String, String> entry : character.getFields().entrySet())
        {
            event.getPlayer().sendMessage(entry.getKey() + " : " + entry.getValue());
        }
	}
}
