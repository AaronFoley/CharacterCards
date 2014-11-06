package com.kaltiz.cc.command;

import com.kaltiz.cc.CharacterCards;

public abstract class BasicCommand implements Command
{
    private final String name;
    protected final CharacterCards plugin;
    protected String description = "";
    protected String usage = "";
    protected String permission = "";
    protected String[] aliases = new String[0];
    protected int minArgs = 0;
    protected int maxArgs = 0;
    protected boolean allowServer = false;
    protected boolean allowPlayer = true;

    public BasicCommand(CharacterCards plugin, String name)
    {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getMaxArguments()
    {
        return maxArgs;
    }

    @Override
    public int getMinArguments()
    {
        return minArgs;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String[] getAliases()
    {
        return aliases;
    }

    @Override
    public String getPermission()
    {
        return permission;
    }

    @Override
    public boolean isServerAllowed() { return allowServer; }

    @Override
    public boolean isPlayerAllowed() { return allowPlayer; }

    @Override
    public String getUsage()
    {
        return usage;
    }

    @Override
    public boolean isShownOnHelpMenu()
    {
        return true;
    }
}
