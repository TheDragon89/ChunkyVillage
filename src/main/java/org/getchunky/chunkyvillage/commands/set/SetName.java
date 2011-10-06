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

public class SetName implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {

        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        if(strings.length < 1) {
            Language.sendBad(chunkyPlayer,"Please specify new name.");
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

        chunkyTown.setName(strings[0]);

        Language.sendGood(chunkyPlayer,"Town has been renamed to " + strings[0]);

    }
}
