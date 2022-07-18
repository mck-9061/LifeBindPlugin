package me.mck.LifeBind;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class KarmaManager {
    private FileConfiguration config;
    private File configFile;


    public KarmaManager(FileConfiguration config, File configFile) {
        this.config = config;
        this.configFile = configFile;
    }

    public void addPlayer(Player player) throws IOException {
        if (!config.contains(player.getName())) {
            config.set(player.getName(), 0);
            config.save(configFile);
        }
    }

    public void addKarma(Player player, int karma) throws IOException {
        int current = config.getInt(player.getName());
        config.set(player.getName(), current + karma);
        config.save(configFile);
    }

    public void resetKarma(Player player) throws IOException {
        config.set(player.getName(), 0);
        config.save(configFile);
    }

    public int getKarma(Player player) {
        return config.getInt(player.getName());
    }

    public boolean checkKarma(Player player) {
        int karma = getKarma(player);
        Random r = new Random();

        if (r.nextInt(100) < karma) {
            return true;
        } else return false;
    }
}
