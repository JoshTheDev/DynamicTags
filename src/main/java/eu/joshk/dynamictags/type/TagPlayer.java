package eu.joshk.dynamictags.type;

import java.util.UUID;

/**
 * Created by Josh on 29/07/2016.
 */
public class TagPlayer {

    private final UUID uuid;
    private String tag;

    public TagPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
