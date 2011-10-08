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
import org.getchunky.chunkyvillage.util.Tools;

public class Deposit implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"You must specify the amount to withdraw.");
            return;
        }

        double amount = Tools.parseDouble(strings[0]);

        if(amount < 1) {
            Language.sendBad(chunkyPlayer,"Pleace specify a proper number.");
            return;
        }

        ChunkyTown chunkyTown = ChunkyTownManager.getTown(chunkyPlayer);

        chunkyTown.deposit(chunkyPlayer,amount);
    }
}
