package me.mck.LifeBind.events;

import me.mck.LifeBind.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.IOException;
import java.util.List;

public class DamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) throws IOException {
        if (!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();

        if (Main.ignoredDamage.contains(damaged)) return;

        System.out.println("Event thrown");

        Main.instance.manager.addKarma(damaged, (int) Math.ceil(event.getDamage()));


        if (Main.instance.manager.checkKarma(damaged)) {
            damaged.sendMessage("Your karma has caught up with you! Dealing double damage to you!");
            Main.ignoredDamage.add(damaged);
            Main.justKarmad.add(damaged);
            damaged.damage(event.getDamage());
            Main.ignoredDamage.remove(damaged);


            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                public void run() {
                    Main.justKarmad.remove(damaged);
                    System.out.println("Karma lock removed");
                }
            }, 100L);


            return;
        }


        for (List<OfflinePlayer> list : Main.bindings) {
            if (!list.contains(Bukkit.getOfflinePlayer(damaged.getUniqueId()))) {
                System.out.println("List does not contain damaged player!");
                continue;
            }
            System.out.println("List contains damaged player!");


            for (OfflinePlayer player : list) {
                if (player.getName().equals(damaged.getName())) {
                    System.out.println("Skipping identical player...");
                    continue;
                }



                if (player.isOnline()) {
                    Main.ignoredDamage.add(player.getPlayer());
                    player.getPlayer().damage(event.getDamage());
                    Main.ignoredDamage.remove(player.getPlayer());
                    System.out.print("Damaged!");
                } else {
                    FileConfiguration config = Main.instance.getCustomConfig();
                    double currentValue = 0;

                    if (config.contains(player.getName())) {
                        currentValue = config.getDouble(player.getName());
                    }

                    currentValue += event.getDamage();
                    config.set(player.getName(), currentValue);
                    config.save(Main.instance.getCustomConfigFile());
                    System.out.println("Queued damage!");

                }
            }
        }
    }
}
