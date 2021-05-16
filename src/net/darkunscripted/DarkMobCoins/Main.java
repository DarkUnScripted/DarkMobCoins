package net.darkunscripted.DarkMobCoins;

import net.darkunscripted.DarkMobCoins.Commands.TokenCommand;
import net.darkunscripted.DarkMobCoins.Events.PluginListener;
import net.darkunscripted.DarkMobCoins.Managers.CurrencyManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JavaPlugin {

    private static Connection connection;
    private String host, database, username, password;
    private int port;

    @Override
    public void onEnable(){
        CurrencyManager currencyManager = new CurrencyManager(this);
        registerManagers();
        registerCommands();
        registerListeners();
        this.saveDefaultConfig();
        this.getConfig();

        host = "46.249.59.197";
        port = 3306;
        database = "s121_MobCoins";
        username = "u121_WxBL88D6rb";
        password = "5H+e@v6h5K5js2K^IO^FyS9h";
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable(){
        CurrencyManager currencyManager = new CurrencyManager(this);
        this.saveDefaultConfig();
        try {
            connection.close();
        } catch (SQLException sqlex) {
            System.err.println("[SQLERROR] " + sqlex);
        }
    }

    public void registerManagers(){
        new CurrencyManager(this);
    }

    public void registerCommands(){
        new TokenCommand();
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PluginListener(), this);
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (Main.getPlugin(Main.class)){
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            String DatabaseHost = Main.getPlugin(Main.class).getConfig().getString("DatabaseHost", "46.249.59.197");
            Integer DatabasePort = Main.getPlugin(Main.class).getConfig().getInt("DatabasePort", 3306);
            String DatabaseNaam = Main.getPlugin(Main.class).getConfig().getString("DatabaseNaam", "s121_MobCoins");
            String DatabaseUser = Main.getPlugin(Main.class).getConfig().getString("DatabaseUser", "u121_WxBL88D6rb");
            String DatabasePassword = Main.getPlugin(Main.class).getConfig().getString("DatabasePassword", "5H+e@v6h5K5js2K^IO^FyS9h");
            connection = DriverManager.getConnection("jdbc:mysql://" + DatabaseHost + ":" + DatabasePort + "/" + DatabaseNaam, DatabaseUser, DatabasePassword);
        }
    }

    public static Connection getConnection(){
        return connection;
    }
}
