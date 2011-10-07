package org.getchunky.chunkyvillage.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

import java.util.HashMap;

public class List implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        HashMap<String,ChunkyObject> towns = ChunkyManager.getObjectsOfType(ChunkyTown.class.getName());
        sender.sendMessage(ChatColor.GRAY + "|-------------------" + ChatColor.GREEN + "[Towns]" + ChatColor.GRAY + "-------------------|");
        for (ChunkyObject chunkyObject : towns.values()) {
            ChunkyTown chunkyTown = (ChunkyTown)chunkyObject;
            sender.sendMessage(ChatColor.GRAY + "| " + ChatColor.GREEN + chunkyObject.getName() + ": " + ChatColor.YELLOW + chunkyTown.getResidents().size() + " residents.");
        }

    }
}
