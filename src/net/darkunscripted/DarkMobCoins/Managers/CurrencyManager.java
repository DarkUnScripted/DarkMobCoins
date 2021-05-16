package net.darkunscripted.DarkMobCoins.Managers;

import net.darkunscripted.DarkMobCoins.Main;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class CurrencyManager {
    public static HashMap<UUID, Integer> currency = new HashMap<UUID, Integer>();

    public Main plugin;

    public CurrencyManager(Main plugin){
        this.plugin = plugin;
    }

    public void addCurrencyToPlayer(OfflinePlayer p, int amount){
        if(currency.get(p.getUniqueId()) != null){
            currency.put(p.getUniqueId(), currency.get(p.getUniqueId()) + amount);
        }else{
            currency.put(p.getUniqueId(), amount);
        }
    }

    public void removeCurrencyFromPlayer(OfflinePlayer p, int amount){
        if(currency.get(p.getUniqueId()) != null){
            currency.put(p.getUniqueId(), currency.get(p.getUniqueId()) - amount);
        }
    }

    public void setPlayerCurrency(OfflinePlayer p, int amount){
        currency.put(p.getUniqueId(), amount);
    }

    public int getPlayerCurrency(OfflinePlayer p){
        if(currency.get(p.getUniqueId()) != null){
            return currency.get(p.getUniqueId());
        }else{
            return 0;
        }
    }
}
