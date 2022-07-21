package me.mck.LifeBind.commands;

import me.mck.LifeBind.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateBind implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender.hasPermission("*")) {
            List<String> boundPlayers = new ArrayList<>();

            for (String player : args) {
                try {
                    boundPlayers.add(player);

                } catch (Exception e) {
                    commandSender.sendMessage("Player not recognised!");
                    return false;
                }

            }


            List<List<String>> bindings = (List<List<String>>) Main.instance.getConfig().getList("bindings");

            bindings.add(boundPlayers);

            Main.instance.getConfig().set("bindings", bindings);
            Main.instance.saveConfig();
            Main.bindings.add(boundPlayers);

            commandSender.sendMessage("Success!");
            return true;
        }



        else {
            commandSender.sendMessage("You don't have permission to do that baldy");
            return false;
        }
    }
}
