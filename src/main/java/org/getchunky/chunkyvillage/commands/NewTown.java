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

public class NewTown implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        if(!(sender.hasPermission("chunky.town.create"))) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;
        }
        Player player = (Player)sender;
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player.getName());
        if(ChunkyTownManager.getTown(chunkyPlayer)!=null) {
            Language.sendBad(chunkyPlayer, "You are already part of a town.");
            return;
        }
        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"Please specify the town name.");
            return;
        }
        ChunkyChunk chunkyChunk = chunkyPlayer.getCurrentChunk();
        if(chunkyChunk.isOwned()) {
            Language.sendBad(chunkyPlayer,"This chunk is already owned.");
            return;
        }
        String id = strings[0];
        ChunkyTown chunkyTown = new ChunkyTown();
        chunkyTown.setId(id);
        chunkyTown.setMayor(chunkyPlayer).setHome(chunkyChunk).setName(id);
        chunkyChunk.setOwner(chunkyTown, true,true);
        chunkyChunk.save();
        Language.sendGood(chunkyPlayer,"You've just created a town called %1",chunkyTown.getName());
        return;
    }
}
