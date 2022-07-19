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


        for (List<OfflinePlayer> list : Main.bindings) {
            if (!list.contains(Bukkit.getOfflinePlayer(healed.getUniqueId()))) {
                System.out.println("List does not contain healed player!");
                continue;
            }
            System.out.println("List contains healed player!");


            for (OfflinePlayer player : list) {
                if (player.getName().equals(healed.getName())) {
                    System.out.println("Skipping identical player...");
                    continue;
                }



                if (player.isOnline()) {
                    Main.ignoredDamage.add(player.getPlayer());

                    double newHealth = player.getPlayer().getHealth() + event.getAmount();
                    if (newHealth > 20) newHealth = 20;



                    player.getPlayer().setHealth(newHealth);
                    Main.ignoredDamage.remove(player.getPlayer());
                    System.out.print("Healed!");
                } else {
                    FileConfiguration config = Main.instance.getCustomConfig();
                    double currentValue = 0;

                    if (config.contains(player.getName())) {
                        currentValue = config.getDouble(player.getName());
                    }

                    currentValue -= event.getAmount();
                    config.set(player.getName(), currentValue);
                    config.save(Main.instance.getCustomConfigFile());
                    System.out.println("Queued health!");

                }
            }
        }
    }
}
