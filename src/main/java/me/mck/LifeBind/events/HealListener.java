package me.mck.LifeBind.events;

import me.mck.LifeBind.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.io.IOException;
import java.util.List;

public class HealListener implements Listener {
    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) throws IOException {
        if (!(event.getEntity() instanceof Player)) return;
        Player healed = (Player) event.getEntity();

        if (Main.ignoredDamage.contains(healed)) return;

        System.out.println("Event thrown");

        Main.instance.manager.addKarma(healed, -1*(int) Math.ceil(event.getAmount()));


        for (List<String> list : Main.bindings) {
            if (!list.contains(healed.getName())) {
                System.out.println("List does not contain healed player!");
                continue;
            }
            System.out.println("List contains healed player!");


            for (String playerName : list) {
                if (playerName.equals(healed.getName())) {
                    System.out.println("Skipping identical player...");
                    continue;
                }

                OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);


                if (player.isOnline()) {
                    if (player.getPlayer().isDead()) {
                        System.out.println("Player is dead, aborting!");
                        continue;
                    }


                    Main.ignoredDamage.add(player.getPlayer());

                    double newHealth = player.getPlayer().getHealth() + event.getAmount();
                    if (newHealth > 20) newHealth = 20;



                    player.getPlayer().setHealth(newHealth);
                    Main.ignoredDamage.remove(player.getPlayer());
                    System.out.print("Healed!");
                } else {
                    FileConfiguration config = Main.instance.getCustomConfig();
                    double currentValue = 0;

                    if (config.contains(playerName)) {
                        currentValue = config.getDouble(playerName);
                    }

                    currentValue -= event.getAmount();
                    config.set(playerName, currentValue);
                    config.save(Main.instance.getCustomConfigFile());
                    System.out.println("Queued health!");

                }
            }
        }
    }
}
