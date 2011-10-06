package org.getchunky.chunkyvillage.commands.toggle;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class ToggleAssistant implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"Please specify player.");
            return;
        }

        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);
        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You are not part of a town.");
            return;
        }

        if(!chunkyTown.isMayor(chunkyPlayer)) {
            Language.sendBad(chunkyPlayer,"You do not have the authority to do this.");
            return;
        }

        ChunkyPlayer assistant = ChunkyManager.getChunkyPlayer(strings[0]);

        if(!chunkyTown.isResident(assistant)) {
            Language.sendBad(chunkyPlayer,"This player does not belong to your town.");
            return;
        }

        if(chunkyTown.isAssistant(chunkyPlayer)) {
            chunkyTown.removeAssistant(chunkyPlayer);
            Language.sendGood(assistant, ChatColor.RED + "You have been demoted from assistant.");
            Language.sendMessage(chunkyPlayer, ChatColor.RED + assistant.getName() + " has been demoted from assistant.");
        }
        else {
            chunkyTown.addAssistant(chunkyPlayer);
            Language.sendGood(assistant,"You have been promoted to assistant.");
            Language.sendGood(chunkyPlayer,assistant.getName() + " has been promoted to assistant.");}


    }
}
