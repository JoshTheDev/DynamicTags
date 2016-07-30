package eu.joshk.dynamictags.query;

import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.util.DatabaseQuery;
import org.bukkit.Bukkit;

import java.sql.SQLException;

/**
 * Created by Josh on 30/07/2016.
 */
public class DatabaseSetupQuery extends DatabaseQuery {

    @Override
    protected void runQuery() {
        try {
            getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " +
                    "`" + DynamicTags.getInstance().getDatabaseManager().getSQLConnection().getDatabase() + "`." +
                    "`" + DynamicTags.getInstance().getDatabaseManager().getSQLConnection().getTable() + "` " +
                    "(" +
                    "`uuid` VARCHAR(36) NOT NULL, " +
                    "`tag` VARCHAR(200) NOT NULL," +
                    "PRIMARY KEY (`uuid`)" +
                    ") ENGINE = InnoDB;").execute();
        } catch (SQLException e) {
            DynamicTags.getInstance().getLogger().severe("Unable to create table!");
            DynamicTags.getInstance().getLogger().severe("Shutting down plugin, please check your MySQL details then try again.");
            Bukkit.getPluginManager().disablePlugin(DynamicTags.getInstance());
        }
    }

}
