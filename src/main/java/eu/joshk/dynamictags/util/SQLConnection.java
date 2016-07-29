package eu.joshk.dynamictags.util;

import eu.joshk.dynamictags.DynamicTags;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Josh on 29/07/2016.
 */
public class SQLConnection {

    public static final String DRIVER_FORMAT = "jdbc:mysql://%s:%s/%s";

    private final String ip, database, username, password;
    private final int port;

    private Connection connection;

    public SQLConnection(String ip, String database, String username, String password, int port) {
        this.ip = ip;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    /**
     * Connect to the database using the given credentials.
     */
    public synchronized void connect() {
        try {
            connection = DriverManager.getConnection(String.format(DRIVER_FORMAT, ip, port, database), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("Uh-oh! We failed to establish a database connection!");
            DynamicTags.getInstance().getLogger().severe("Please update the config and restart the server to try again.");
            Bukkit.getPluginManager().disablePlugin(DynamicTags.getInstance());
        }
    }

    /**
     * Disconnect from the database if the connection is open.
     */
    public synchronized void disconnect() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("Failed closing the database connection!");
            DynamicTags.getInstance().getLogger().severe("This may not be fatal, however please be cautious and restart" +
                    " the server if you notice any irregularities.");
        }
    }

    /**
     * Get the current database connection.
     *
     * @return connection instance if connected, null if not.
     */
    public Connection getConnection() {
        return connection;
    }

}
