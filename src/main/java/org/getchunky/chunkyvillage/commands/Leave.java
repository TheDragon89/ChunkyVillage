package org.getchunky.chunkyvillage.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Leave implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player)sender;
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player.getName());
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);
        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You do not belong to a town.");
            return;
        }
        if(chunkyTown.isMayor(chunkyPlayer)) {
            Language.sendBad(chunkyPlayer, "Please set a new mayor before leaving.");
            return;
        }

        chunkyTown.kickResident(chunkyPlayer);
        Language.sendGood(chunkyPlayer,"You have left the town.");
        if(chunkyTown.getResidents().size() ==0 ) chunkyTown.delete();
    }
}
