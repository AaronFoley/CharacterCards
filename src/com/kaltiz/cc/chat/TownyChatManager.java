package com.kaltiz.cc.chat;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.chat.channel.TownyRPChannel;

import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class TownyChatManager extends ChatManager
{
    private Chat townychat = (Chat) Bukkit.getServer().getPluginManager().getPlugin("TownyChat");

    public TownyChatManager(CharacterCards plugin)
    {
        super(plugin);
    }

    @Override
    public void setUpChannels()
    {
        ConfigurationSection channels = plugin.getConfig().getConfigurationSection("chatChannels");

        for( String chName : channels.getKeys(false))
        {
            ConfigurationSection channelSection = channels.getConfigurationSection(chName);

            List<String> commands = channelSection.getStringList("commands");
            if (commands.isEmpty()) commands.add(chName);
            String permission = channelSection.getString("permission");
            int min = channelSection.getInt("min", 25);
            int max = channelSection.getInt("max", 25);
            String channelTag = ChatColor.translateAlternateColorCodes('&', channelSection.getString("channelTag",""));
            String format = channelSection.getString("format", "<channelTag> " + ChatColor.YELLOW + "<player>" + ChatColor.WHITE + ": <message>");
            format = ChatColor.translateAlternateColorCodes('&',format);

            Channel channel = new TownyRPChannel(plugin, chName, min, max, channelTag, format, commands, permission);

            this.townychat.getChannelsHandler().addChannel(channel);
        }
    }
}
