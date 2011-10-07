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
import org.getchunky.chunkyvillage.util.Config;
import org.getchunky.chunkyvillage.util.Tools;

public class Vote implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
                   Language.IN_GAME_ONLY.bad(sender);
                   return;}

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);

        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You are not part of a town.");
            return;}

        if(strings.length < 1) {
            chunkyTown.printVotes(chunkyPlayer);
            return;
        }

        ChunkyPlayer candidate = ChunkyManager.getChunkyPlayer(strings[0]);


        if(!chunkyTown.isResident(candidate)) {
            Language.sendBad(chunkyPlayer, candidate.getName() + " is not part of your town.");
            return;}

        int i = chunkyTown.addVote(chunkyPlayer,candidate);
        Tools.goodMessageTown(chunkyTown,chunkyPlayer.getName() + " has voted for " + candidate.getName() + ", " + i + " total votes.");
        if(chunkyTown.getResidents().size() * (Config.getElectionPercentage()/100) <= i) {
            if(!candidate.getName().equals(chunkyTown.getOwner().getName()))chunkyTown.setMayor(candidate);
            chunkyTown.clearVotes();
            chunkyTown.save();
            Tools.goodMessageTown(chunkyTown,candidate.getName() + " has been elected mayor!");
        }
    }

}
