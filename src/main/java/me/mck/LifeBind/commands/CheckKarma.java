package me.mck.LifeBind.commands;

import me.mck.LifeBind.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckKarma implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            player.sendMessage("Your current karma is " + Main.instance.manager.getKarma(player) + ".");


            return true;
        } else return false;
    }
}
