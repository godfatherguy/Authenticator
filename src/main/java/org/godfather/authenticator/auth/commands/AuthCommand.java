package org.godfather.authenticator.auth.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.godfather.authenticator.Authenticator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthCommand implements CommandExecutor, TabCompleter {

    private final Authenticator plugin;

    public AuthCommand(Authenticator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        switch(args.length) {
            case 1:
                switch(args[0]) {
                    case "reload":
                        if(!sender.hasPermission("auth.reload") && !sender.isOp()) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        plugin.getPlayerData().reloadPlayerConfig();
                        plugin.getSpawnData().reloadSpawnConfig();
                        plugin.getLangData().reloadLangConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(0));
                        break;
                    case "setspawn":
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "This command cannot be performed by console.");
                            return false;
                        }
                        if(!sender.hasPermission("auth.setspawn") && !sender.isOp()) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        plugin.getSpawnData().setSpawn(((Player) sender).getLocation());
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(1));
                        break;
                    case "spawn":
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.RED + "This command cannot be performed by console.");
                            return false;
                        }
                        if(!sender.hasPermission("auth.spawn") && !sender.isOp()) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        if(plugin.getSpawnData().getSpawn() == null) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(2));
                            return false;
                        }
                        ((Player) sender).teleport(plugin.getSpawnData().getSpawn());
                        break;
                    case "delspawn":
                        if(plugin.getSpawnData().getSpawn() == null) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(2));
                            return false;
                        }
                        if(!sender.hasPermission("auth.delspawn") && !sender.isOp()) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        plugin.getSpawnData().removeSpawn();
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(3));
                        break;
                    default:
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(4));
                        return false;
                }
                break;
            case 2:
                String target = args[1];
                if(!args[0].equalsIgnoreCase("unregister")) {
                    sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(10));
                    return false;
                }
                if(!sender.hasPermission("auth.unregister.others")) {
                    sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                    return false;
                }
                if(!plugin.getPlayerData().isSaved(target)) {
                    sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(5).replaceAll("%NAME%", args[1]));
                    return false;
                }
                plugin.getPlayerData().getFileConfiguration().set(target, null);
                plugin.getPlayerData().getFileConfiguration().set(target + ".ip", null);
                plugin.getPlayerData().getFileConfiguration().set(target + ".password", null);
                plugin.getPlayerData().savePlayerConfig();
                sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(6).replaceAll("%NAME%", args[1]));
                if(Bukkit.getPlayerExact(target) == null) return false;
                Objects.requireNonNull(Bukkit.getPlayerExact(target)).kickPlayer(plugin.getConfigManager().getLangFile().getUnRegisterMessages().get(1));
                break;
            case 3:
                String target1 = args[1];
                String password = args[2];
                int minLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMinLength();
                int maxLength = plugin.getConfigManager().getConfigFile().getRegistration().getPasswordMaxLength();
                if (password.length() < minLength || password.length() > maxLength) {
                    sender.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(10));
                    return false;
                }
                if (plugin.getConfigManager().getConfigFile().getRegistration().getUnsafePasswords().contains(password)) {
                    sender.sendMessage(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(11));
                    return false;
                }
                switch(args[0]) {
                    case "register":
                        if(!sender.hasPermission("auth.register.others")) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        if(plugin.getPlayerData().isSaved(target1)) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(7).replaceAll("%NAME%", args[1]));
                            return false;
                        }
                        plugin.getPlayerData().setPassword(target1, password);
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(8).replaceAll("%NAME%", args[1]));
                        if(Bukkit.getPlayerExact(target1) == null) return false;
                        Objects.requireNonNull(Bukkit.getPlayerExact(target1)).kickPlayer(plugin.getConfigManager().getLangFile().getRegistrationMessages().get(13));
                        break;
                    case "changepassword":
                        if(!sender.hasPermission("auth.changepassword.others")) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getNoPerm());
                            return false;
                        }
                        if(!plugin.getPlayerData().isSaved(target1)) {
                            sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(5).replaceAll("%NAME%", args[1]));
                            return false;
                        }
                        plugin.getPlayerData().setPassword(target1, password);
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(9).replaceAll("%NAME%", args[1]));
                        if(Bukkit.getPlayerExact(target1) == null) return false;
                        Objects.requireNonNull(Bukkit.getPlayerExact(target1)).kickPlayer(plugin.getConfigManager().getLangFile().getChangepassMessages().get(1));
                        break;
                    default:
                        sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(10));
                        return false;
                }
                break;
            default:
                sender.sendMessage(plugin.getConfigManager().getLangFile().getCommandsMessages().get(10));
                return false;
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("auth.reload") || sender.isOp())
                list.add("reload");
            if(sender.hasPermission("auth.spawn") || sender.isOp())
                list.add("spawn");
            if(sender.hasPermission("auth.setspawn") || sender.isOp())
                list.add("setspawn");
            if(sender.hasPermission("auth.delspawn") || sender.isOp())
                list.add("delspawn");
            if(sender.hasPermission("auth.register.others") || sender.isOp())
                list.add("register");
            if(sender.hasPermission("auth.unregister.others") || sender.isOp())
                list.add("unregister");
            if(sender.hasPermission("auth.changepassword.others") || sender.isOp())
                list.add("changepassword");
        } else if(args.length == 2) {
            if(args[0].equals("register") && (sender.hasPermission("auth.register.others") || sender.isOp())) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }
            } else if(args[0].equals("unregister") && (sender.hasPermission("auth.unregister.others") || sender.isOp())
                || args[0].equals("changepassword") && (sender.hasPermission("auth.changepassword.others") || sender.isOp())) {
                list.addAll(plugin.getPlayerData().getFileConfiguration().getKeys(false));
            } else return list;
        } else return list;
        List<String> finalList = new ArrayList<>();
        String currentArg = args[args.length - 1].toUpperCase();
        for (String string : list) {
            String s1 = string.toUpperCase();
            if (s1.startsWith(currentArg) || s1.contains(currentArg))
                finalList.add(string);
        }
        return finalList;
    }
}
