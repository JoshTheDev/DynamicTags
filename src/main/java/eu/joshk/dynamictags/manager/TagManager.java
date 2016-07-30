package eu.joshk.dynamictags.manager;

import com.google.common.base.Strings;
import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.query.TagGetQuery;
import eu.joshk.dynamictags.type.TagPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Josh on 29/07/2016.
 */
public class TagManager {

    private final Map<UUID, TagPlayer> playerCache;
    private boolean spaceAfterTag;
    private String tagFormat, defaultTag;

    public TagManager() {
        this.playerCache = new ConcurrentHashMap<>();
        this.spaceAfterTag = DynamicTags.getInstance().getConfig().getBoolean("spaceAfterTag");
        this.tagFormat = ChatColor.translateAlternateColorCodes('&',
                DynamicTags.getInstance().getConfig().getString("tagFormat"));
        this.defaultTag = ChatColor.translateAlternateColorCodes('&',
                DynamicTags.getInstance().getConfig().getString("defaultTag"));

        Bukkit.getOnlinePlayers().forEach((Player player) -> handleJoin(player.getUniqueId()));
    }

    /**
     * Check whether a player is cached locally.
     *
     * @param uuid player uuid to check.
     * @return true/false, depending on cache status.
     */
    public boolean isCached(UUID uuid) {
        return this.playerCache.containsKey(uuid);
    }

    /**
     * Handle a player join (loading tags etc).
     *
     * @param uuid uuid of player joining.
     */
    public void handleJoin(UUID uuid) {
        DynamicTags.getInstance().getDatabaseManager().schedule(new TagGetQuery(uuid, (TagGetQuery tagGetQuery) -> {
            try {
                this.playerCache.put(uuid, new TagPlayer(uuid, tagGetQuery.getResult()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * Handle player quit (remove local references etc).
     *
     * @param uuid uuid of player quitting.
     */
    public void handleQuit(UUID uuid) {
        if(isCached(uuid)) {
            this.playerCache.remove(uuid);
        }
    }

    /**
     * Get a cached TagPlayer.
     *
     * @param uuid uuid of player trying to lookup.
     * @return TagPlayer if present, null if not.
     */
    public TagPlayer getPlayer(UUID uuid) {
        if(isCached(uuid)) {
            return this.playerCache.get(uuid);
        }
        return null;
    }

    /**
     * Get a format for a specific player.
     *
     * @param tagPlayer player to get format for.
     * @return hopefully a correct chat format.
     */
    public String format(TagPlayer tagPlayer) {
        String result = tagFormat;

        // Player name handling.
        result = result.replace("{NAME}", tagPlayer.getPlayerName());
        // Tag handling.
        if(spaceAfterTag) {
            result = result.replace("{TAG}", (Strings.isNullOrEmpty(tagPlayer.getTag()) ?
                    (defaultTag.isEmpty() ? defaultTag : defaultTag + " ") : tagPlayer.getTag() + " "));
        } else {
            result = result.replace("{TAG}", (Strings.isNullOrEmpty(tagPlayer.getTag()) ? defaultTag : tagPlayer.getTag()));
        }
        // Message handling.
        result = result.replace("{MESSAGE}", "%2$s");
        // Trim any misc. blanks.
        result = result.trim();
        // Suggestions for any more placeholders? Make a pull request to let me know!

        return result;
    }

}
