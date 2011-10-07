package org.getchunky.chunkyvillage.commands.set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.util.Tools;

public class SetMayor implements ChunkyCommandExecutor{


    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        ChunkyTown chunkyTown = ChunkyTownManager.isMayor(chunkyPlayer);

        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You do not have the authority to do this");
            return;}

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer, "Please specify the new mayor.");
            return;
        }
        ChunkyPlayer newMayor = ChunkyManager.getChunkyPlayer(strings[0]);

        if(newMayor == chunkyPlayer) {
            Language.sendBad(chunkyPlayer, "You are already the mayor.");
            return;
        }

        if(!chunkyTown.isResident(newMayor)) {
            Language.sendBad(chunkyPlayer, "This player is not part of your town");
            return;
        }

        if(chunkyTown.isAssistant(newMayor)) chunkyTown.removeAssistant(newMayor);

        chunkyTown.setMayor(newMayor);

        Tools.goodMessageTown(chunkyTown,newMayor.getName() + " is now the new mayor.");

    }
}
