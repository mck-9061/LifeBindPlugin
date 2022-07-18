package me.mck.LifeBind.events;

import me.mck.LifeBind.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.IOException;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws IOException {


        if (Main.justKarmad.contains(event.getEntity())) {
            event.setDeathMessage(event.getEntity().getName() + "'s karma caught up to them");
            event.getEntity().sendMessage("Your death has not reset your karma...");
            return;
        }


        if (Main.ignoredDamage.contains(event.getEntity())) {
            event.setDeathMessage(event.getEntity().getName() + " was killed by their Life Bind");
        }
        event.getEntity().sendMessage("Your karma has been reset...");
        Main.instance.manager.resetKarma(event.getEntity());
    }
}
