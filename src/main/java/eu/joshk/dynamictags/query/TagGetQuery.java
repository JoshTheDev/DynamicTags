package eu.joshk.dynamictags.query;

import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.util.Callback;
import eu.joshk.dynamictags.util.DatabaseQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Josh on 30/07/2016.
 */
public class TagGetQuery extends DatabaseQuery {

    private UUID uuid;

    private String result;

    public TagGetQuery(UUID uuid, Callback<TagGetQuery> callback) {
        super(callback);
        this.uuid = uuid;
        this.result = null;
    }

    @Override
    protected void runQuery() {
        try {
            PreparedStatement query = getConnection().prepareStatement("SELECT `tag` FROM `"
                    + DynamicTags.getInstance().getDatabaseManager().getSQLConnection().getTable() + "` WHERE `uuid`=?;");
            query.setString(1, uuid.toString());
            ResultSet resultSet = query.executeQuery();
            if(resultSet.next()) {
                this.result = resultSet.getString("tag");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the query result.
     * Null if player doesn't exist.
     *
     * @return the query result. Null if something went wrong / non-existant player.
     */
    public String getResult() {
        return result;
    }

}
