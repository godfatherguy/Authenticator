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
            player.sendMessage(plugin.getConfigManager().getLangFile().getUnRegisterMessages().get(0));
            return false;
        }

        if(!player.hasPermission("auth.unregister") && !player.isOp()) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
            return false;
        }

        if(args.length != 2) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getUnRegisterMessages().get(2));
            return false;
        }

        if(!plugin.getPlayerData().isSaved(player)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(6));
            return false;
        }
        String password = args[0];

        if (!password.equals(args[1])) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(13));
            return false;
        }

        if(!password.equals(plugin.getPlayerData().getPassword(player))) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(6));
            return false;
        }

        plugin.getPlayerData().getFileConfiguration().set(player.getName(), null);
        player.kickPlayer(plugin.getConfigManager().getLangFile().getUnRegisterMessages().get(1));
        return true;
    }
}
