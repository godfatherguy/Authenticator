package org.godfather.authenticator.auth.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.godfather.authenticator.Authenticator;

public class UnregisterCommand implements CommandExecutor {

    private final Authenticator plugin;

    public UnregisterCommand(Authenticator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be performed by console.");
            return false;
        }
        Player player = (Player) sender;
        if (plugin.getAuthManager().isAuth(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You have to be authenticated in order to unregister.");
            return false;
        }

        if(args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Use: /unregister <yourPassword> <confirmPassword>");
            return false;
        }

        if(!plugin.getPlayerData().isSaved(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You are not registered in the database.");
            return false;
        }
        String password = args[0];

        if (!password.equals(args[1])) {
            player.sendMessage(ChatColor.DARK_RED + "The passwords don't match.");
            return false;
        }

        if(!password.equals(plugin.getPlayerData().getPassword(player))) {
            player.sendMessage(ChatColor.DARK_RED + "Wrong password!");
            return false;
        }

        plugin.getPlayerData().getFileConfiguration().set(player.getName(), null);
        player.kickPlayer(ChatColor.DARK_GREEN + "You have been unregistered from the database.");
        return true;
    }
}
