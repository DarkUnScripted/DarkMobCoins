package net.darkunscripted.DarkMobCoins.Commands;

import net.darkunscripted.DarkMobCoins.Main;
import net.darkunscripted.DarkMobCoins.Managers.CurrencyManager;
import net.darkunscripted.DarkMobCoins.Utils.Utils;
import net.minecraft.server.v1_12_R1.CommandExecute;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Currency;
import java.util.UUID;

public class TokenCommand extends CommandExecute implements CommandExecutor, Listener {

    private Main plugin = Main.getPlugin(Main.class);
    private String TokenName = plugin.getConfig().getString("CurrencyName", "Token");

    public TokenCommand() {
        plugin.getCommand(TokenName).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("darkmobcoin.use")){
            if(args.length == 0){
                sender.sendMessage(Utils.chat("&e/" + TokenName + " <add:remove:set> <player> <amount>"));
                return true;
            } else if(args.length == 1){
                if(args[0].equalsIgnoreCase("add")){
                    sender.sendMessage(Utils.chat("&e/" + TokenName + " add <player> <amount>"));
                } else if(args[0].equalsIgnoreCase("remove")){
                    sender.sendMessage(Utils.chat("&e/" + TokenName + " remove <player> <amount>"));
                } else if(args[0].equalsIgnoreCase("set")){
                    sender.sendMessage(Utils.chat("&e/" + TokenName + " set <player> <amount>"));
                } else if(args[0].equalsIgnoreCase("get")){
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        CurrencyManager manager = new CurrencyManager(plugin);
                        int currency = manager.getPlayerCurrency(p);
                        p.sendMessage(Utils.chat("&aYou have " + currency + " " + TokenName));
                    }else {
                        sender.sendMessage(Utils.chat("&e/" + TokenName + " get <player>"));
                    }
                }
            } else if(args.length == 2){
                if(args[0].equalsIgnoreCase("add")){
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        CurrencyManager manager = new CurrencyManager(plugin);
                        int amount = Integer.parseInt(args[1]);
                        manager.addCurrencyToPlayer(p, amount);
                        p.sendMessage(Utils.chat("&aYou now have " + manager.getPlayerCurrency(p) + " " + TokenName +"."));
                    }else {
                        sender.sendMessage(Utils.chat("&e/" + TokenName + " add <player> <amount>"));
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        CurrencyManager manager = new CurrencyManager(plugin);
                        int amount = Integer.parseInt(args[1]);
                        if(amount > manager.getPlayerCurrency(p)){
                            p.sendMessage(Utils.chat("&cYou dont have enough " + TokenName + "!"));
                        }else{
                            manager.removeCurrencyFromPlayer(p, amount);
                            p.sendMessage(Utils.chat("&aYou now have " + manager.getPlayerCurrency(p) + " " + TokenName + "."));
                        }
                    }else {
                        sender.sendMessage(Utils.chat("&e/" + TokenName + " remove <player> <amount>"));
                    }
                }else if(args[0].equalsIgnoreCase("set")){
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        CurrencyManager manager = new CurrencyManager(plugin);
                        int amount = Integer.parseInt(args[1]);
                        manager.setPlayerCurrency(p, amount);
                    }else {
                        sender.sendMessage(Utils.chat("&e/" + TokenName + " set <player> <amount>"));
                    }
                }else if(args[0].equalsIgnoreCase("get")){
                    Player player = Bukkit.getPlayer(args[1]);
                    OfflinePlayer p = Bukkit.getOfflinePlayer(player.getUniqueId());
                    if(p != null || p.hasPlayedBefore()){
                        CurrencyManager manager = new CurrencyManager(plugin);
                        sender.sendMessage(Utils.chat("&a" + p.getName() + " now has " + manager.getPlayerCurrency(p) + " " + TokenName + "."));
                    }else {
                        sender.sendMessage(Utils.chat("&ePlayer &c" + args[1] + " &e could not be found"));
                    }
                }else{
                    sender.sendMessage(Utils.chat("&e/" + TokenName + " <add:remove:set> <player> <amount>"));
                }
            } else if(args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);
                OfflinePlayer p = Bukkit.getOfflinePlayer(player.getUniqueId());
                int amount = Integer.parseInt(args[2]);
                CurrencyManager manager = new CurrencyManager(plugin);
                if (args[0].equalsIgnoreCase("add")) {
                    if (p != null || p.hasPlayedBefore()) {
                        manager.addCurrencyToPlayer(p, amount);
                        sender.sendMessage(Utils.chat("&aYou have successfully added &6$" + args[2] + " &ato the player &e" + p.getName()));
                    } else {
                        sender.sendMessage(Utils.chat("&ePlayer &c" + args[1] + " &e could not be found"));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (p != null || p.hasPlayedBefore()) {
                        if (manager.getPlayerCurrency(p) >= amount) {
                            manager.removeCurrencyFromPlayer(p, amount);
                            sender.sendMessage(Utils.chat("&aYou have successfully removed &c$" + args[2] + " &afrom the player &e" + p.getName()));
                        } else {
                            sender.sendMessage(Utils.chat("&cPlayer does not have enough " + TokenName + "!"));
                        }
                    } else {
                        sender.sendMessage(Utils.chat("&ePlayer &c" + args[1] + " &e could not be found"));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (p != null || p.hasPlayedBefore()) {
                        manager.setPlayerCurrency(p, amount);
                        sender.sendMessage(Utils.chat("&aYou have successfully set the player " + p.getName() + "'s " + TokenName + " to &5$" + args[2]));
                    } else {
                        sender.sendMessage(Utils.chat("&ePlayer &c" + args[1] + " &e could not be found"));
                    }
                } else {
                    sender.sendMessage(Utils.chat("&e/" + TokenName + " <add:remove:set> <player> <amount>"));
                }
            } else if(args.length > 3){
                sender.sendMessage(Utils.chat("&e/" + TokenName + " <add:remove:set> <player> <amount>"));
            }
        } else {
            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
        }

        return false;
    }
}