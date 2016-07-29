package eu.joshk.dynamictags;

import eu.joshk.dynamictags.manager.TagManager;
import eu.joshk.dynamictags.util.SQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Josh on 29/07/2016.
 */
public class DynamicTags extends JavaPlugin {

    private static DynamicTags instance;

    private SQLConnection sqlConnection;
    private TagManager tagManager;

    @Override
    public void onEnable() {
        instance = this;

        if(!this.getDataFolder().exists() || !new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
            getLogger().info("It seems like you don't have a config file! We've copied one over for you, please modify it" +
                    " to reflect your database information then restart the server!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        setupConnection();
        this.tagManager = new TagManager();
    }

    @Override
    public void onDisable() {
        getSQLConnection().disconnect();

        instance = null;
    }

    public static DynamicTags getInstance() {
        return instance;
    }

    /**
     * Get the Tag Manager.
     * This manager is responsible for most of the plugin functionality.
     *
     * @return tag manager. Shouldn't be null unless the plugin is disabled or something.
     */
    public TagManager getTagManager() {
        return tagManager;
    }

    /**
     * Get the current SQL connection.
     *
     * @return hopefully the SQL connection instance. If not loaded / something went wrong, null.
     */
    public SQLConnection getSQLConnection() {
        return sqlConnection;
    }

    /**
     * Setup the database connection.
     */
    private void setupConnection() {
        this.sqlConnection = new SQLConnection(getConfig().getString("database.ip"), getConfig().getString("database.database"),
                getConfig().getString("database.username"), getConfig().getString("database.password"), getConfig().getInt("database.port"));
        this.sqlConnection.connect();
    }

}
