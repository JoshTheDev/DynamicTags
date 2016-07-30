package eu.joshk.dynamictags.manager;

import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.query.DatabaseSetupQuery;
import eu.joshk.dynamictags.util.DatabaseQuery;
import eu.joshk.dynamictags.util.SQLConnection;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josh on 30/07/2016.
 */
public class DatabaseManager {

    private final ExecutorService threadPool;
    private SQLConnection sqlConnection;

    public DatabaseManager(int threads) {
        this.threadPool = Executors.newFixedThreadPool(threads);

        setupConnection();
        schedule(new DatabaseSetupQuery());
    }

    /**
     * Schedule a DatabaseQuery for execution when a thread becomes available.
     *
     * @param databaseQuery query to schedule.
     */
    public void schedule(DatabaseQuery databaseQuery) {
        if (this.threadPool.isShutdown() || this.threadPool.isTerminated()) {
            throw new UnsupportedOperationException("You are unable to schedule any more database queries after the " +
                    "thread pool has been terminated / closed.");
        }
        this.threadPool.submit(databaseQuery);
    }

    /**
     * Finish up with all pending threads (call this when the server stops).
     * You won't be able to schedule any more threads after you call this.
     */
    public void finish() {
        try {
            this.threadPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("Issue finishing all database queries!");
            DynamicTags.getInstance().getLogger().severe("Some data may be lost.");
        }
        this.threadPool.shutdown();
        this.sqlConnection.disconnect();
    }

    /**
     * Manually get a new connection from the connection pool.
     *
     * @return connection from the connection pool. Shouldn't be null.
     */
    public Connection getNewConnection() {
        return this.sqlConnection.getConnection();
    }

    /**
     * Setup the database connection.
     */
    private void setupConnection() {
        this.sqlConnection = new SQLConnection(DynamicTags.getInstance().getConfig().getString("database.ip"),
                DynamicTags.getInstance().getConfig().getString("database.database"),
                DynamicTags.getInstance().getConfig().getString("database.username"),
                DynamicTags.getInstance().getConfig().getString("database.password"),
                DynamicTags.getInstance().getConfig().getString("database.table"),
                DynamicTags.getInstance().getConfig().getInt("database.port"));
    }

    /**
     * Get the current SQL connection.
     * This holds things like connection settings (not including password).
     *
     * @return sql connection - null if something went wrong.
     */
    public SQLConnection getSQLConnection() {
        return this.sqlConnection;
    }

}
