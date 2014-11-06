package com.kaltiz.cc.chat.channel;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import com.palmergames.bukkit.TownyChat.channels.Channel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TownyRPChannel extends Channel
{
    protected CharacterCards plugin;
    protected int maxRange;
    protected int minRange;
    protected String channelTag;
    protected String format;

    public TownyRPChannel(CharacterCards plugin, String name, int min, int max, String tag, String format, List<String> commands, String permission)
    {
        super(name);
        this.plugin = plugin;
        this.maxRange = max;
        this.minRange = min;
        this.channelTag = tag;
        this.format = format;
        this.setCommands(commands);
        this.setPermission(permission);
        this.setType(null);
        this.setLeavePermission("cc.admin");
    }

    @Override
    public void chatProcess(AsyncPlayerChatEvent event)
    {

        Player player = event.getPlayer();
        RpChar character = plugin.getCharacterManager().getCharacter(player);
        // Ensure they have a RP char
        if (character == null) {
            player.sendMessage("You must have a RP Character to talk in this chat!");
            event.setCancelled(true);
            return;
        }
        // Ensure they have a display name
        String displayName = plugin.getChatManager().getDisplayName(character);
        if (displayName.equals("") || displayName == null) {
            player.sendMessage("You must have a RP Name to talk in this chat!");
            event.setCancelled(true);
            return;
        }

        String formatStr = this.format;

        formatStr = formatStr.replace("<channelTag>", this.channelTag);
        formatStr = formatStr.replace("<player>", displayName);
        formatStr = formatStr.replace("<message>", "%2$s");

        event.setFormat(formatStr);

        event.getRecipients().clear();
        event.getRecipients().addAll(findRecipients(player));

        // Send it to other players faded
        for (Player ply : this.findFaded(player))
        {
            String Sentmsg = this.fadeMessage(event.getMessage(), this.minRange, player.getLocation().distance(ply.getLocation()));
            ply.sendMessage(formatStr.replace("%2$s", Sentmsg));
        }
    }

    private Set<Player> findRecipients(Player player)
    {
        Set<Player> recipients = new HashSet<Player>();

        for (Entity ent : player.getNearbyEntities(this.minRange, this.minRange, this.minRange))
        {
            if (ent instanceof Player)
            {
                recipients.add((Player) ent);
            }
        }

        return recipients;
    }

    public Set<Player> findFaded(Player player)
    {
        Set<Player> recipients = new HashSet<Player>();

        for (Entity ent : player.getNearbyEntities(this.maxRange, this.maxRange, this.maxRange))
        {
            if (ent instanceof Player) {
                if (ent.getLocation().distance(player.getLocation()) > this.minRange)
                {
                    recipients.add((Player) ent);
                }
            }
        }

        recipients.add(player);

        return recipients;
    }

    public String fadeMessage(String message, int min, double distance)
    {
        int replaceNo = (int) distance - min;

        replaceNo = replaceNo < message.length() ? replaceNo : message.length();

        char[] characters = message.toCharArray();

        Random r = new Random();

        for (int i = 0; i < replaceNo; i++) {
            characters[r.nextInt(message.length())] = '-';
        }

        return new String(characters);
    }

}
