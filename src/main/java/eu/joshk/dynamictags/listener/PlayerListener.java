package eu.joshk.dynamictags.listener;

import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.type.TagPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Josh on 29/07/2016.
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {
        if(e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        DynamicTags.getInstance().getTagManager().handleJoin(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        DynamicTags.getInstance().getTagManager().handleQuit(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.isCancelled()) {
            return;
        }
        TagPlayer tagPlayer = DynamicTags.getInstance().getTagManager().getPlayer(e.getPlayer().getUniqueId());
        if(tagPlayer != null) {
            e.setFormat(DynamicTags.getInstance().getTagManager().format(tagPlayer));
        }
    }

}
