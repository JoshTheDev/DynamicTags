package eu.joshk.dynamictags.query;

import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.util.DatabaseQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Josh on 30/07/2016.
 */
public class TagUpdateQuery extends DatabaseQuery {

    private final UUID uuid;
    private final String tag;

    public TagUpdateQuery(UUID uuidToSet, String tagToSet) {
        this.uuid = uuidToSet;
        this.tag = tagToSet;
    }

    @Override
    protected void runQuery() {
        try {
            if (tag == null) {
                PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM `" +
                        DynamicTags.getInstance().getDatabaseManager().getSQLConnection().getTable() + "` WHERE `uuid`=?;");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.executeUpdate();
            } else {
                PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO " +
                        DynamicTags.getInstance().getDatabaseManager().getSQLConnection().getTable() + " (`uuid`, `tag`) " +
                        "VALUES (?, ?) ON DUPLICATE KEY UPDATE `tag`=?;");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, tag);
                preparedStatement.setString(3, tag);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DynamicTags.getInstance().getLogger().severe("An issue occurred when updating a tag!");
            DynamicTags.getInstance().getLogger().severe("As a result, the update may not have been saved in the database.");
        }
    }

}
