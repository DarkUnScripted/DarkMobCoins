package net.darkunscripted.DarkMobCoins.Events;

import net.darkunscripted.DarkMobCoins.Main;
import net.darkunscripted.DarkMobCoins.Managers.CurrencyManager;
import net.darkunscripted.DarkMobCoins.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PluginListener implements Listener {

    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        try {
            OfflinePlayer p = event.getPlayer();
            if (!(p.hasPlayedBefore())) {
                Statement st2 = Main.getConnection().createStatement();
                String query2 = "INSERT INTO Tokens (UUID, Name, Amount) VALUES ('" + event.getPlayer().getUniqueId() + "', '" + event.getPlayer().getName() + "', 0)";
                st2.execute(query2);
                st2.close();
            } else {
                Statement st1 = Main.getConnection().createStatement();
                String query1 = "SELECT Amount FROM Tokens WHERE UUID = '" + event.getPlayer().getUniqueId() + "'";
                ResultSet rs1 = st1.executeQuery(query1);
                CurrencyManager manager = new CurrencyManager(plugin);
                int Amount = 0;
                while (rs1.next()) {
                    Amount = rs1.getInt("Amount");
                }
                manager.setPlayerCurrency(event.getPlayer(), Amount);
                event.getPlayer().sendMessage(Utils.chat("&aYou have " + manager.getPlayerCurrency(event.getPlayer()) + " Tokens"));
                st1.close();
                rs1.close();
            }
        }catch(SQLException sqlex){
            System.err.println("[SQLERROR] " + sqlex);
            sqlex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        try{
            Statement st = Main.getConnection().createStatement();
            CurrencyManager manager = new CurrencyManager(plugin);
            String query = "UPDATE Tokens SET Amount = " + manager.getPlayerCurrency(e.getPlayer()) + " WHERE UUID = '" + e.getPlayer().getUniqueId() + "'";
            st.execute(query);
            st.close();
        }catch(SQLException sqlex) {
            System.err.println("[SQLERROR] " + sqlex);
            sqlex.printStackTrace();
        }
    }

    @EventHandler
    public void mobDeath(EntityDeathEvent event){
        LivingEntity e = event.getEntity();
        if(!(e instanceof Player)){
            Random random = new Random();
            int Amount = 0;
            while (true){
                Amount = random.nextInt(plugin.getConfig().getInt("MaxCoinValue", 10)+1);
                if(Amount != 0) break;
            }
            List lore = new ArrayList();
            lore.add(Utils.chat("&bThis coin is worth:&a " + Amount));
            Material type;
            ItemStack item = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
            e.getLocation().getWorld().dropItem(e.getLocation(), item);
        }
    }

    @EventHandler
    public void InteractEvent(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.hasItem()) {
                if (e.getItem().hasItemMeta()){
                    if (e.getItem().getItemMeta().getLore().get(0).contains("This coin is worth")) {
                        String[] splitter = e.getItem().getItemMeta().getLore().get(0).split(" ");
                        String AmountString =  ChatColor.stripColor(splitter[splitter.length - 1]);
                        Integer Amount = Integer.parseInt(AmountString);
                        Amount = Amount * e.getItem().getAmount();
                        e.getPlayer().getInventory().remove(e.getPlayer().getInventory().getItemInMainHand());
                        Player p = e.getPlayer();
                        CurrencyManager manager = new CurrencyManager(plugin);
                        manager.addCurrencyToPlayer(p, Amount);
                    }
                }
            }
        }
    }
}
