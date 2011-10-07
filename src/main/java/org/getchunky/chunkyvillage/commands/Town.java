package org.getchunky.chunkyvillage.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Town implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);

        if(chunkyTown==null) {
            Language.sendBad(chunkyPlayer, "You are not part of a town");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "|-------------------" + ChatColor.GREEN + "["+ChatColor.GOLD + chunkyTown.getName()+ChatColor.GREEN + "]" + ChatColor.GRAY + "-------------------|");
        String res = "";
        int i=0;
        for(ChunkyObject chunkyObject : chunkyTown.getResidents()) {
            res += ChatColor.WHITE + chunkyObject .getName() + ChatColor.GRAY + ", ";
            i++;
            if(i>40) break;
        }
        if(res.length()>2) res = res.substring(0,res.length()-2);

        String ass = "";
        int a=0;
        for(String player : chunkyTown.getAssistants()) {
            ass += ChatColor.BLUE + player + ChatColor.GRAY + ", ";
            i++;
            if(i>40) break;
        }
        if(ass.length()>2) ass = ass.substring(0,ass.length()-2);
        sender.sendMessage(
                ChatColor.GRAY + "| " + ChatColor.GREEN + "Size: " + ChatColor.YELLOW + chunkyTown.claimedChunkCount() + "/" +chunkyTown.maxChunks() +
                ChatColor.GRAY + " | "+ ChatColor.GREEN + "Bank: " + ChatColor.YELLOW + chunkyTown.getAccount().balance() +
                ChatColor.GRAY + " | "+ ChatColor.GREEN + "Mayor: " + ChatColor.YELLOW + chunkyTown.getMayor().getName());

        sender.sendMessage(ChatColor.GRAY + "| " + ChatColor.GREEN + "Assistant: " + ChatColor.YELLOW + ass + ((i>40) ? " and more" : ""));
        sender.sendMessage(ChatColor.GRAY + "| " + ChatColor.GREEN + "Population: " + ChatColor.YELLOW + res + ((i>40) ? " and more" : ""));


    }
}
