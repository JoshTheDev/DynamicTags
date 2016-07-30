package eu.joshk.dynamictags;

import eu.joshk.dynamictags.command.TagCommand;
import eu.joshk.dynamictags.listener.PlayerListener;
import eu.joshk.dynamictags.manager.DatabaseManager;
import eu.joshk.dynamictags.manager.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Josh on 29/07/2016.
 */
public class DynamicTags extends JavaPlugin {

    private static DynamicTags instance;
    private static final String STARTER = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "DynamicTags" +
            ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    private DatabaseManager databaseManager;
    private TagManager tagManager;

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDataFolder().exists() || !new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
            getLogger().info("It seems like you don't have a config file! We've copied one over for you, please modify it" +
                    " to reflect your database information then restart the server!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.getConfig().options().copyDefaults(true);

        this.databaseManager = new DatabaseManager(10);
        this.tagManager = new TagManager();

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        if(getDatabaseManager() != null) {
            // For when the plugin is stopped prematurely.
            getDatabaseManager().finish();
        }

        instance = null;
    }

    public static DynamicTags getInstance() {
        return instance;
    }

    /**
     * Get formatted plugin message starter.
     *
     * @return chat starter.
     */
    public static String getStarter() {
        return STARTER;
    }

    /**
     * Register plugin listeners.
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    /**
     * Register plugin commands.
     */
    private void registerCommands() {
        getCommand("tag").setExecutor(new TagCommand());
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
     * Get the database manager.
     * This manager is responsible for scheduling all database updates / queries.
     *
     * @return database manager instance.
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

}
