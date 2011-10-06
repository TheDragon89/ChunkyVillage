package org.getchunky.chunkyvillage.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.util.Tools;

public class Tax implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {

        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"You must specify the percentage.");
            return;
        }

        double percentage = Tools.parseDouble(strings[0]);

        if(percentage < 1 || percentage > 100) {
            Language.sendBad(chunkyPlayer,"Pleace specify a number between 1 and 100");
            return;
        }

        percentage = percentage/100;

        ChunkyTown chunkyTown = ChunkyTownManager.isMayor(chunkyPlayer);

        if(chunkyTown == null) {
            Language.sendBad(chunkyPlayer,"You do not have the authority to do this");
            return;}

        double gained = chunkyTown.taxPlayers(percentage);

        Language.sendGood(chunkyPlayer,"Managed to collect " + Chunky.getMethod().format(gained));
    }

}
