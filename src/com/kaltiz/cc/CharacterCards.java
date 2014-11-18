package com.kaltiz.cc;

import com.atherys.listeners.PlayerWatchListener;
import com.kaltiz.cc.character.CharacterManager;
import com.kaltiz.cc.chat.ChatManager;
import com.kaltiz.cc.chat.SMPChatManager;
import com.kaltiz.cc.chat.TownyChatManager;
import com.kaltiz.cc.command.CommandHandler;
import com.kaltiz.cc.command.commands.*;
import com.kaltiz.cc.listensers.PlayerListener;
import com.kaltiz.cc.storage.CharacterStorage;
import com.kaltiz.cc.storage.SQLCharacterStorage;
import com.kaltiz.cc.storage.YMLCharacterStorage;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public class CharacterCards extends JavaPlugin
{
    private CharacterManager charManager = null;
    private CharacterStorage storage = null;
    private CommandHandler commandHandler = null;
    private ChatManager chat = null;
    private Permission perms = null;
    private Logger log;

    @Override
    public void onEnable()
    {
        log = getLogger();

        // Save the Default Configs
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Setup Permissions with Vault
        if ((getServer().getPluginManager().getPlugin("Vault") == null) || (!setupPermissions()))
        {
            log.warning("CharacterCards requires Vault!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Setup Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Setup Commands
        commandHandler = new CommandHandler(this);
        registerCommands();

        // Setup the Character Manager
        charManager = new CharacterManager(this);

        // Setup the Storage
        setupStorage();
        this.storage.loadCharacters();

        // Setup a ChatManager
        setupChat();
    }

    @Override
    public void onDisable()
    {
        storage.shutdown();
        log.info("CharacterCards is Disabled!");
    }

    public CharacterManager getCharacterManager()
    {
		return charManager;
	}

    public Permission getPerms()
    {
        return perms;
    }

    public CharacterStorage getStorage()
    {
        return storage;
    }

    public CommandHandler getCommandHandler()
    {
        return commandHandler;
    }

    public ChatManager getChatManager() { return chat;}

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        return this.commandHandler.onCommand(sender,cmd.getName(),args);
    }

    private void registerCommands()
    {
        // Normal Commands
        this.commandHandler.addCommand(new HelpCommand(this));
        this.commandHandler.addCommand(new CardCommand(this));
        this.commandHandler.addCommand(new FieldsCommand(this));
        this.commandHandler.addCommand(new SetCommand(this));
        this.commandHandler.addCommand(new UnsetFieldCommand(this));
        this.commandHandler.addCommand(new WhoisCommand(this));
        this.commandHandler.addCommand(new MultiCommand(this));
        this.commandHandler.addCommand(new VersionCommand(this));
        this.commandHandler.addCommand(new RemoveCommand(this));

        // Admin Commands
        this.commandHandler.addCommand(new AdminMultiCommand(this));
        this.commandHandler.addCommand(new AdminRemoveCommand(this));
        this.commandHandler.addCommand(new AdminSetCommand(this));
        this.commandHandler.addCommand(new AdminUnsetCommand(this));
    }

    private void setupStorage()
    {
        if((getConfig().getString("storage.type").equalsIgnoreCase("database")))
        {
            try {
                this.storage = new SQLCharacterStorage(this);
            }
            catch (SQLException ex)
            {
                log.severe("Could not create SQLStorage, falling back to File Storage");
                log.severe("Reason: " + ex.getMessage());
                // Fall back to YML
                this.storage = new YMLCharacterStorage(this);
            }
        }
        // Else YNL storage
        else
        {
            this.storage = new YMLCharacterStorage(this);
        }
    }

    private void setupChat()
    {
        String type = getConfig().getString("chatManager");

        if (type.equalsIgnoreCase("townychat"))
            this.chat = new TownyChatManager(this);
        else if (type.equalsIgnoreCase("mc"))
            this.chat = new SMPChatManager(this);
        else
            return;

        chat.setUpChannels();
    }
}
