package com.kaltiz.cc.command;

import org.bukkit.command.CommandSender;

public abstract interface Command
{
    public abstract boolean execute(CommandSender sender, String cmd, String[] args);

    public abstract String getDescription();

    public abstract int getMaxArguments();

    public abstract int getMinArguments();

    public abstract String getName();

    public abstract String[] getAliases();

    public abstract String getPermission();

    public abstract boolean isServerAllowed();

    public abstract boolean isPlayerAllowed();

    public abstract String getUsage();

    public abstract boolean isShownOnHelpMenu();
}