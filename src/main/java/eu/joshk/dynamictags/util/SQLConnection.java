package eu.joshk.dynamictags.util;

import eu.joshk.dynamictags.DynamicTags;
import org.apache.commons.dbcp.BasicDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Josh on 29/07/2016.
 */
public class SQLConnection {

    public static final String DRIVER_FORMAT = "jdbc:mysql://%s:%s/%s";

    private final String ip, database, username, table;
    private final int port;

    private BasicDataSource dataSource;

    public SQLConnection(String ip, String database, String username, String password, String table, int port) {
        this.ip = ip;
        this.database = database;
        this.username = username;
        this.table = table;
        this.port = port;

        this.dataSource = new BasicDataSource();
        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setUrl(String.format(DRIVER_FORMAT, ip, port, database));
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);
    }

    /**
     * Get the IP of the database we're connecting to.
     *
     * @return database IP.
     */
    public String getIP() {
        return ip;
    }

    /**
     * Get the name of the database we're connecting to.
     *
     * @return name of database.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Get the username used to authenticate to the database we're connecting to.
     * I have not made the password publicly available for security reasons.
     *
     * @return database username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the port used for connecting to the database.
     *
     * @return port used. Default would be 3306.
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the table to be used with this plugin.
     * We only require one at this point.
     *
     * @return table to use. Default would be 'DynamicTags'.
     */
    public String getTable() {
        return table;
    }

    /**
     * Get a connection from the connection pool.
     * Will return null and the plugin will be disabled if something goes wrong (i.e. authentication issue).
     *
     * @return connection instance or null.
     */
    public synchronized Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("Uh-oh! We failed to establish a database connection!");
            DynamicTags.getInstance().getLogger().severe("Please update the config and restart the server to try again.");
            Bukkit.getPluginManager().disablePlugin(DynamicTags.getInstance());
            return null;
        }
    }

    /**
     * Disconnect from the database, closing all open connections.
     */
    public synchronized void disconnect() {
        try {
            this.dataSource.close();
            this.dataSource = null;
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("Failed closing the database connection!");
            DynamicTags.getInstance().getLogger().severe("This may not be fatal, however please be cautious and restart" +
                    " the server if you notice any irregularities.");
        }
    }

}
