package org.godfather.authenticator.auth.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.godfather.authenticator.Authenticator;

public class LoginCommand implements CommandExecutor {

    private final Authenticator plugin;

    public LoginCommand(Authenticator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be performed by console.");
            return false;
        }
        Player player = (Player) sender;
        if (!plugin.getAuthManager().isAuth(player)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(4));
            return false;
        } else if (!plugin.getAuthManager().isLogin(player)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(5));
            return false;
        }

        if(!player.hasPermission("auth.login") && !player.isOp()) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(8));
            return false;
        }
        String password = args[0];

        if(plugin.getConfigManager().getConfigFile().getRestrictions().getMaxLogIP() != 0) {
            int ipCount = plugin.getPlayerData().getIPs(player);
            if(ipCount > plugin.getConfigManager().getConfigFile().getRestrictions().getMaxLogIP()) {
                player.kickPlayer(plugin.getConfigManager().getLangFile().getRestrictionMessages().get(1));
                return false;
            }
        }

        if (!password.equals(plugin.getPlayerData().getPassword(player))) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(6));
            if (plugin.getConfigManager().getConfigFile().getLogin().kickOnWrong())
                player.kickPlayer(plugin.getConfigManager().getLangFile().getLoginMessages().get(6));
            return false;
        }

        plugin.getPlayerData().refreshIP(player);
        player.sendMessage(plugin.getConfigManager().getLangFile().getLoginMessages().get(7));
        plugin.getAuthManager().removeLogin(player);
        plugin.getPlayerData().setIp(player);

        if (plugin.getConfigManager().getConfigFile().getAuth().showAccounts() && plugin.getPlayerData().getIPs(player) > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : plugin.getPlayerData().getAccounts(player)) {
                if (string.equals(player.getName())) continue;
                stringBuilder.append(string).append(", ");
            }
            for(String string : plugin.getConfigManager().getLangFile().getAuthShowAcc()) {
                player.sendMessage(string.replaceAll("%NAME%", player.getName()).replaceAll("%OTHERS%", stringBuilder.toString()));
            }

            for(Player players : Bukkit.getOnlinePlayers()) {
                for(String string : plugin.getConfigManager().getLangFile().getAuthShowAdmin()) {
                    players.sendMessage(string.replaceAll("%NAME%", player.getName()).replaceAll("%OTHERS%", stringBuilder.toString()));
                }
            }
        }

        player.setInvulnerable(false);
        player.setInvisible(false);
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        if (plugin.getConfigManager().getConfigFile().getAuth().hideInventory())
            plugin.getAuthManager().getAuthPlayer(player).showInventory();

        if (plugin.getConfigManager().getConfigFile().getAuth().forceSurvivalAfter())
            player.setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
