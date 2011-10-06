package org.getchunky.chunkyvillage.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
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
        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You are not part of a town.");
            return;
        }

        if(chunkyPlayer.getCurrentChunk().isOwned() && !chunkyPlayer.getCurrentChunk().isOwnedBy(chunkyTown) && !chunkyPlayer.getCurrentChunk().isOwnedBy(chunkyPlayer)) {
            Language.sendBad(chunkyPlayer,"You cannot teleport from other town's land.");
            return;
        }

        ((Player)sender).teleport(chunkyTown.getHome().getCoord().toLocation());

    }
}
