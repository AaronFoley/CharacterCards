package com.kaltiz.cc.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.kaltiz.cc.util.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.kaltiz.cc.CharacterCards;
import com.kaltiz.cc.character.RpChar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SQLCharacterStorage extends CharacterStorage
{

	private CharacterCards plugin;

    protected DatabaseType driver;
    protected String url = "";
    protected String username = "";
    protected String password = "";

	protected Connection conn = null;

	public SQLCharacterStorage(CharacterCards plugin) throws SQLException
    {
		super(plugin);
		this.plugin = plugin;

        this.driver = DatabaseType.match(plugin.getConfig().getString("storage.database.driver"));

        this.url = "jdbc:" + plugin.getConfig().getString("storage.database.url");

        this.username = plugin.getConfig().getString("storage.database.username");
        this.password = plugin.getConfig().getString("storage.database.password");

        if (!loadDriver())
            throw new SQLException("Couldn't load driver");

        this.conn = getConnection();

        if (conn==null)
            throw new SQLException("Couldn't connect to the database");

        // Create the Characters Table
        String qry = "CREATE TABLE IF NOT EXISTS `characters` (`uuid` VARCHAR(36) NOT NULL PRIMARY KEY, `rpchar` TEXT NOT NULL);";
        Statement stmt = this.conn.createStatement();
        stmt.execute(qry);
        stmt.close();
    }

    private boolean loadDriver()
    {
        try {
            this.getClass().getClassLoader().loadClass(this.driver.driver).newInstance();
            return true;
        } catch (IllegalAccessException e) {
            // Constructor is private, OK for DriverManager contract
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() throws SQLException
    {
        if (conn == null || conn.isClosed())
        {
            conn = (username.isEmpty() && password.isEmpty()) ? DriverManager.getConnection(url) : DriverManager.getConnection(url, username, password);
        }
        // The connection could be null here (!)
        return conn;
    }

	@Override
	public void loadCharacters()
    {
        System.out.println("Loading Characters");
        // Select all the Characters and create them all
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM `characters`");

            while (rs.next())
            {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                if (player == null) continue;

                FileConfiguration fields = new YamlConfiguration();
                fields.loadFromString(rs.getString("rpchar"));

                Map<String, Object> data = fields.getValues(false);

                HashMap<String, String> fieldValues = new HashMap<String, String> ();

                for (Map.Entry<String, Object> fieldval : data.entrySet())
                {
                    fieldValues.put(fieldval.getKey(), (String) fieldval.getValue());
                }

                RpChar character = new RpChar(player, plugin, fieldValues);

                plugin.getCharacterManager().addCharacter(character);
            }
        }
        catch (Exception e)
        {
            plugin.getLogger().severe("Error loading characters " + e.getMessage());
            plugin.getLogger().severe("Disabling Plugin!");
            // Disable the Plugin.
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        // JDBC is Ugly
        finally
        {
            if (stmt != null)
                try { stmt.close(); }
                catch (SQLException e)  { e.printStackTrace(); }
            if (rs != null)
                try { rs.close(); }
                catch (SQLException e)  { e.printStackTrace(); }
        }
	}

    @Override
	public void saveCharacter(RpChar character)
    {
        PreparedStatement stmt = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement("REPLACE INTO `characters` VALUES (?,?)");

            FileConfiguration fields = new YamlConfiguration();

            for (Map.Entry<String,String> field : character.getFields().entrySet())
            {
                fields.set(field.getKey(), field.getValue());
            }

            stmt.setString(1, character.getPlayer().getUniqueId().toString());
            stmt.setString(2, fields.saveToString());

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            plugin.getLogger().severe("There was an error saving " + character.getPlayer().getName() + e.getMessage());
        }
        // JDBC is ugly
        finally
        {
            if (stmt != null)
            try { stmt.close(); }
            catch (SQLException e)  { e.printStackTrace(); }
        }
	}

	@Override
	public void removeCharacter(RpChar character)
    {
        PreparedStatement stmt = null;
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement("DELETE FROM `characters` WHERE `UUID` = ?");

            stmt.setString(1,character.getPlayer().getUniqueId().toString());

            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("There was an error removing " + character.getPlayer().getName() + e.getMessage());
        }
        finally
        {
            if (stmt != null)
                try { stmt.close(); }
                catch (SQLException e)  { e.printStackTrace(); }
        }
	}

    @Override
    public void shutdown()
    {
        Map<UUID,RpChar> toSave = plugin.getCharacterManager().getCharacters();

        for (RpChar character : toSave.values())
            this.saveCharacter(character);

        // Close the Connection.
        try
        {
            this.conn.close();
        }
        catch (SQLException e)
        {
            plugin.getLogger().severe("There was an error closing Database connection.");
        }
    }

}
