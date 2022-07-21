package me.mck.LifeBind;

import me.mck.LifeBind.commands.CheckKarma;
import me.mck.LifeBind.commands.CreateBind;
import me.mck.LifeBind.events.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;
    public static List<List<String>> bindings;
    public static List<Player> ignoredDamage;
    public static List<Player> justKarmad;

    public KarmaManager manager;

    private File damageQueue;
    private FileConfiguration damageQueueConfig;

    private File karma;
    private FileConfiguration karmaConfig;


    @Override
    public void onEnable() {
        ignoredDamage = new ArrayList<>();
        justKarmad = new ArrayList<>();
        instance = this;

        saveDefaultConfig();
        createCustomConfig();


        manager = new KarmaManager(karmaConfig, karma);

        FileConfiguration config = getConfig();
        bindings = (List<List<String>>) config.getList("bindings");

        for (List<String> list : bindings) {
            for (String player:list) {
                System.out.println(player);
            }
        }

        getCommand("addbind").setExecutor(new CreateBind());
        getCommand("checkkarma").setExecutor(new CheckKarma());


        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new HealListener(), this);

        System.out.println(bindings.size());
    }







    public FileConfiguration getCustomConfig() {
        return this.damageQueueConfig;
    }

    public File getCustomConfigFile() {
        return this.damageQueue;
    }

    private void createCustomConfig() {
        damageQueue = new File(getDataFolder(), "damagequeue.yml");
        if (!damageQueue.exists()) {
            damageQueue.getParentFile().mkdirs();
            saveResource("damagequeue.yml", false);
        }

        damageQueueConfig = new YamlConfiguration();
        try {
            damageQueueConfig.load(damageQueue);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }




        karma = new File(getDataFolder(), "karma.yml");
        if (!karma.exists()) {
            karma.getParentFile().mkdirs();
            saveResource("karma.yml", false);
        }

        karmaConfig = new YamlConfiguration();
        try {
            karmaConfig.load(karma);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
