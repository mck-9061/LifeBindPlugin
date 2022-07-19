package me.mck.LifeBind.events;

import me.mck.LifeBind.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        FileConfiguration config = Main.instance.getCustomConfig();
        Player player = event.getPlayer();

        Main.instance.manager.addPlayer(player);

        if (config.contains(player.getName())) {
            double damage = config.getDouble(player.getName());
            player.setNoDamageTicks(0);
            Main.ignoredDamage.add(player);


            if (damage > 0) {
                player.damage(damage);
                player.sendMessage("While you were offline, your damage-binded partner(s) took " + damage/2 + " hearts of damage!");
            } else if (damage < 0) {
                player.setHealth(player.getHealth() - damage);
                player.sendMessage("While you were offline, your damage-binded partner(s) healed " + damage/2 + " hearts!");
            }


            Main.ignoredDamage.remove(player);
            config.set(player.getName(), 0);
            config.save(Main.instance.getCustomConfigFile());
        }
    }
}
