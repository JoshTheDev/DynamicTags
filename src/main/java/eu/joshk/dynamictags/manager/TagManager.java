package eu.joshk.dynamictags.manager;

import eu.joshk.dynamictags.type.TagPlayer;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Josh on 29/07/2016.
 */
public class TagManager {

    private final HashMap<UUID, TagPlayer> playerCache;

    public TagManager() {
        this.playerCache = new HashMap<>();
    }

    /**
     * Check whether a player is cached locally.
     *
     * @param uuid player uuid to check.
     * @return true/false, depending on cache status.
     */
    public boolean isCached(UUID uuid) {
        return playerCache.containsKey(uuid);
    }

}
