package eu.joshk.dynamictags.command;

import com.google.common.base.Strings;
import eu.joshk.dynamictags.DynamicTags;
import eu.joshk.dynamictags.query.TagGetQuery;
import eu.joshk.dynamictags.query.TagUpdateQuery;
import eu.joshk.dynamictags.type.TagPlayer;
import eu.joshk.dynamictags.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Josh on 30/07/2016.
 */
public class TagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!commandSender.hasPermission("dynamictags.manage")) {
            commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }
        if(args.length < 2) {
            commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "Correct Usage:");
            commandSender.sendMessage(DynamicTags.getStarter() + "/" + label + " get <player>");
            commandSender.sendMessage(DynamicTags.getStarter() + "/" + label + " set <player> <tag>");
            commandSender.sendMessage(DynamicTags.getStarter() + "/" + label + " remove <player>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "get": {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if(offlinePlayer == null) {
                    commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "That player doesn't seem to have played before?");
                    return true;
                }
                commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.GREEN + "Searching...");
                DynamicTags.getInstance().getDatabaseManager().schedule(new TagGetQuery(offlinePlayer.getUniqueId(), (TagGetQuery callback) -> {
                    if(Strings.isNullOrEmpty(callback.getResult())) {
                        commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "That player doesn't seem to have a defined tag!");
                        commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "If they tried to chat, they would get the default tag.");
                        return;
                    }
                    commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.GREEN + offlinePlayer.getName() + "'s tag: " +
                            ChatColor.DARK_GRAY + ChatColor.translateAlternateColorCodes('&', callback.getResult()));
                }));
                return true;
            }
            case "set": {
                if(args.length < 3) {
                    commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "Correct Usage:");
                    commandSender.sendMessage(DynamicTags.getStarter() + "/" + command + " set <player> <tag>");
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if(offlinePlayer == null) {
                    commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "That player doesn't seem to have played before?");
                    return true;
                }
                String tagSet = StringUtils.fromArgsArray(args, 2);
                if(offlinePlayer.isOnline()) {
                    TagPlayer tagPlayer = DynamicTags.getInstance().getTagManager().getPlayer(offlinePlayer.getUniqueId());
                    if(tagPlayer != null) {
                        tagPlayer.setTag(tagSet);
                    }
                }
                DynamicTags.getInstance().getDatabaseManager().schedule(new TagUpdateQuery(offlinePlayer.getUniqueId(), tagSet));
                commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.GREEN + "Tag updated!");
                return true;
            }
            case "remove": {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if(offlinePlayer == null) {
                    commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "That player doesn't seem to have played before?");
                    return true;
                }
                DynamicTags.getInstance().getDatabaseManager().schedule(new TagUpdateQuery(offlinePlayer.getUniqueId(), null));
                commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.GREEN + "Tag removed!");
                return true;
            }
            default: {
                commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.RED + "Unknown sub-command!");
                commandSender.sendMessage(DynamicTags.getStarter() + ChatColor.YELLOW + "Known sub-commands: " +
                        ChatColor.GRAY + "get, set, remove" + ChatColor.YELLOW + ".");
            }
        }
        return true;
    }

}
