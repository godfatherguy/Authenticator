package org.godfather.authenticator.auth.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.godfather.authenticator.Authenticator;

public class ChangepasswordCommand implements CommandExecutor {

    private final Authenticator plugin;

    public ChangepasswordCommand(Authenticator plugin) {
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
            player.sendMessage(ChatColor.DARK_RED + "Use: /changepassword <oldPassword> <newPassword>");
            return false;
        }

        if(!plugin.getPlayerData().isSaved(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You are not registered in the database.");
            return false;
        }
        String oldPassword = args[0];
        String newPassword = args[1];

        if(!oldPassword.equals(plugin.getPlayerData().getPassword(player))) {
            player.sendMessage(ChatColor.DARK_RED + "Wrong password!");
            return false;
        }
        int minLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMinLength();
        int maxLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMaxLength();

        if (newPassword.length() < minLength || newPassword.length() > maxLength) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(10));
            return false;
        }
        if (plugin.getConfigManager().getConfigFile().getRegistration().getUnsafePasswords().contains(newPassword)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(11));
            return false;
        }

        plugin.getPlayerData().setPassword(player, newPassword);
        player.sendMessage(ChatColor.DARK_GREEN + "Your password has been changed successfully!");
        return true;
    }
}
