package eu.joshk.dynamictags.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Josh on 29/07/2016.
 */
public class TagPlayer {

    private final UUID uuid;
    private String tag;

    public TagPlayer(UUID uuid, String tag) {
        this.uuid = uuid;
        if(tag != null) {
            setTag(tag);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getTag() {
        return tag;
    }

    public String getPlayerName() {
        Player player = Bukkit.getPlayer(getUUID());
        return (player == null ? "UNKNOWN" : player.getDisplayName());
    }

    public void setTag(String tag) {
        this.tag = ChatColor.translateAlternateColorCodes('&', tag);
    }

}
