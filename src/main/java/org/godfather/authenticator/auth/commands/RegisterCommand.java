package org.godfather.authenticator.auth.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.godfather.authenticator.Authenticator;

public class RegisterCommand implements CommandExecutor {

    private final Authenticator plugin;

    public RegisterCommand(Authenticator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be performed by console.");
            return false;
        }
        Player player = (Player) sender;
        if (!plugin.getAuthManager().isRegister(player)) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(7));
            return false;
        }

        if(!player.hasPermission("auth.register") && !player.isOp()) {
            player.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
            return false;
        }

        if (player.getName().length() < plugin.getConfigManager().getConfigFile().getRegistration().getNicknameMinLength()) {
            player.kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(8));
            return false;
        } else if (player.getName().length() > plugin.getConfigManager().getConfigFile().getRegistration().getNicknameMaxLength()) {
            player.kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(9));
            return false;
        }

        if(plugin.getConfigManager().getConfigFile().getRestrictions().getMaxRegIP() != 0) {
            int ipCount = plugin.getPlayerData().getIPs(player);
            if(ipCount > plugin.getConfigManager().getConfigFile().getRestrictions().getMaxRegIP()) {
                player.kickPlayer(plugin.getConfigManager().getLangFile().getRestrictionMessages().get(2));
                return false;
            }
        }

        int minLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMinLength();
        int maxLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMaxLength();

        if (plugin.getConfigManager().getConfigFile().getRegistration().passwordDoubleCheck()) {
            if (args.length == 2) {
                String password = args[0];
                if (password.length() < minLength || password.length() > maxLength) {
                    player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(10));
                    return false;
                }
                if (plugin.getConfigManager().getConfigFile().getRegistration().getUnsafePasswords().contains(password)) {
                    player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(11));
                    return false;
                }
                if (!password.equals(args[1])) {
                    player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(12));
                    return false;
                }
                plugin.getPlayerData().savePlayer(player);
                plugin.getPlayerData().setPassword(player, password);
                player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(13));
                plugin.getAuthManager().removeRegister(player);
                player.sendTitle(" ", " ", 0, 1, 0);

                if (plugin.getConfigManager().getConfigFile().getRegistration().kickAfterReg()) {
                    player.kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(14));
                    return false;
                }
                if (plugin.getConfigManager().getConfigFile().getRegistration().logAfterReg()) {
                    plugin.getAuthManager().addLogin(player);
                    return false;
                }
                player.setInvulnerable(false);
                player.setInvisible(false);
                player.removePotionEffect(PotionEffectType.BLINDNESS);

                if (plugin.getConfigManager().getConfigFile().getAuth().hideInventory())
                    plugin.getAuthManager().getAuthPlayer(player).showInventory();
            } else player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(15));
            return false;
        }
        if (args.length == 1) {
            String password = args[0];
            if (password.length() < minLength || password.length() > maxLength) {
                player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(10));
                return false;
            }
            if (plugin.getConfigManager().getConfigFile().getRegistration().getUnsafePasswords().contains(password)) {
                player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(11));
                return false;
            }
            plugin.getPlayerData().savePlayer(player);
            plugin.getPlayerData().setPassword(player, password);
            player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(13));
            plugin.getAuthManager().removeRegister(player);

            if (plugin.getConfigManager().getConfigFile().getRegistration().kickAfterReg()) {
                player.kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(14));
                return false;
            }
            if (plugin.getConfigManager().getConfigFile().getRegistration().logAfterReg()) {
                plugin.getAuthManager().addLogin(player);
                return false;
            }
            player.setInvulnerable(false);
            player.setInvisible(false);
            player.removePotionEffect(PotionEffectType.BLINDNESS);

            if (plugin.getConfigManager().getConfigFile().getAuth().hideInventory())
                plugin.getAuthManager().getAuthPlayer(player).showInventory();
        } else player.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(16));
        return true;
    }
}
