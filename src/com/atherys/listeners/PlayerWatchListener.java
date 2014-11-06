package com.atherys.listeners;

import com.kaltiz.cc.CharacterCards;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.atherys.AtherysTime;

public class PlayerWatchListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (p.getItemInHand().getType() == Material.WATCH) {
            p.sendMessage(AtherysTime.CurrentTime());
        }
    }
}
