package org.getchunky.chunkyvillage;

import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunkyvillage.commands.ForSale;
import org.getchunky.chunkyvillage.commands.NewTown;
import org.getchunky.chunkyvillage.commands.Town;
import org.getchunky.chunkyvillage.listeners.ChunkyEvents;

import java.util.Arrays;

public class ChunkyVillage extends JavaPlugin {
    private ChunkyEvents chunkyEvents;
    public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }

    public void onEnable() {
        Chunky.getModuleManager().registerModule(this);

        chunkyEvents = new ChunkyEvents();
        Chunky.getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_CHUNK_CLAIM, chunkyEvents, ChunkyEvent.Priority.Normal,this);
        Chunky.getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_CHUNK_CHANGE, chunkyEvents, ChunkyEvent.Priority.Normal,this);

        registerChunkyCommands();

        System.out.println(this + " is now enabled!");
    }

    private void registerChunkyCommands(){
        ChunkyCommand root = Chunky.getModuleManager().getCommandByName("chunky");
        try {
            ChunkyCommand town = new ChunkyCommand("town", Arrays.asList("t"),"Commands for ChunkyVillage",Arrays.asList("/chunky town <name> or /c t <name>"),new Town(),root);
            ChunkyCommand newTown = new ChunkyCommand("new", Arrays.asList("n"),"Create a new town",Arrays.asList("/c town new or /c t n"),new NewTown(),town);
            ChunkyCommand forSale = new ChunkyCommand("forsale", Arrays.asList("fs"),"Sets land for sale",Arrays.asList("/c town forsale or /c t fs"),new ForSale(),town);
            Chunky.getModuleManager().registerCommand(town);
            Chunky.getModuleManager().registerCommand(newTown);
            Chunky.getModuleManager().registerCommand(forSale);
        } catch (ChunkyUnregisteredException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
