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
            player.sendMessage(plugin.getConfigManager().getLangFile().getChangepassMessages().get(0));
            return false;
        }

        if(!player.hasPermission("auth.changepassword") && !player.isOp()) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
            return false;
        }

        if(args.length != 2) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getChangepassMessages().get(2));
            return false;
        }

        if(!plugin.getPlayerData().isSaved(player)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(6));
            return false;
        }
        String oldPassword = args[0];
        String newPassword = args[1];

        if(!oldPassword.equals(plugin.getPlayerData().getPassword(player))) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(6));
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
        player.sendMessage(plugin.getConfigManager().getLangFile().getChangepassMessages().get(1));
        return true;
    }
}
