package com.kaltiz.cc.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class UUIDUtils
{
    public static OfflinePlayer getOfflinePlayerByName(String name)
    {
        // We are only interested in getting players who have played
        for ( OfflinePlayer player : Bukkit.getOfflinePlayers())
        {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }

        return null;
    }
}
