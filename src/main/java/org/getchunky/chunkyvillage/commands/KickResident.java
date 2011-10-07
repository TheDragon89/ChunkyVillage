package org.getchunky.chunkyvillage.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class KickResident implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"Please specify player to add.");
            return;
        }

        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);
        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You are not part of a town.");
            return;
        }

        if(!chunkyTown.isAssistantOrMayor(chunkyPlayer)) {
            Language.sendBad(chunkyPlayer,"You do not have the authority to do this.");
            return;
        }

        Player player = Bukkit.getServer().matchPlayer(strings[0]).get(0);
        if(player == null) {
            Language.sendBad(chunkyPlayer,"Could not find player: " + strings[0]);
            return;
        }

        ChunkyPlayer resident = ChunkyManager.getChunkyPlayer(player);

        if(chunkyTown.isAssistantOrMayor(resident)) {
            Language.sendBad(chunkyPlayer,"You may not kick assistants or the mayor.");
            return;
        }

        if(!chunkyTown.isResident(resident)) {
            Language.sendBad(chunkyPlayer,"This player is not in your town");
            return;
        }

        chunkyTown.kickResident(resident);
        Language.sendBad(chunkyPlayer,resident.getName() + " added to " + chunkyTown.getName());
        Language.sendGood(resident,"You were kicked from " + chunkyTown.getName());



    }
}
