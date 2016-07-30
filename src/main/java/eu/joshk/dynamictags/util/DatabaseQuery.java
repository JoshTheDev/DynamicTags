package eu.joshk.dynamictags.util;

import eu.joshk.dynamictags.DynamicTags;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Thanks to Wouter for the idea.
 */
public abstract class DatabaseQuery implements Runnable {

    private final Callback callback;
    private Connection connection;

    public DatabaseQuery() {
        this(null);
    }

    public DatabaseQuery(Callback callback) {
        this.callback = callback;
    }

    protected abstract void runQuery();

    /**
     * Get the assigned connection from the connection pool.
     *
     * @return Connection instance. Shouldn't be null (unless the query hasn't started running).
     */
    protected final Connection getConnection() {
        return connection;
    }

    @Override
    public final void run() {
        // First, get a new connection from the connection pool.
        this.connection = DynamicTags.getInstance().getDatabaseManager().getNewConnection();

        // Next, run the query.
        runQuery();

        // After the query is finished, close & null the connection.
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("An issue occurred when closing a database connection!");
            DynamicTags.getInstance().getLogger().severe("This shouldn't be fatal, however please be cautious and restart" +
                    " the server if you notice any irregularities.");
        }
        this.connection = null;

        // Finally, if there is a callback present, call it.
        if(this.callback != null) {
            callback.call(this);
        }
    }

}
