package org.getchunky.chunkyvillage;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunkyvillage.commands.*;
import org.getchunky.chunkyvillage.commands.set.Set;
import org.getchunky.chunkyvillage.commands.set.SetMayor;
import org.getchunky.chunkyvillage.commands.set.SetName;
import org.getchunky.chunkyvillage.commands.toggle.Toggle;
import org.getchunky.chunkyvillage.commands.toggle.ToggleAssistant;
import org.getchunky.chunkyvillage.listeners.ChunkyEvents;
import org.getchunky.chunkyvillage.util.Config;

import java.util.Arrays;

public class ChunkyVillage extends JavaPlugin {
    private ChunkyEvents chunkyEvents;

    private static Plugin plugin;

    public static Plugin getInstance() {
        return plugin;
    }

    public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }

    public void onEnable() {
        plugin = this;

        Config.load();

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
            ChunkyCommand town = new ChunkyCommand("town", Arrays.asList("t"),"Teleport to your town.",Arrays.asList("/chunky town or /c t <name>"),new Town(),root);
            ChunkyCommand list = new ChunkyCommand("list", Arrays.asList("l"),"Lists towns.",Arrays.asList("/c town list or /c t l"),new List(),town);

            ChunkyCommand newTown = new ChunkyCommand("new", Arrays.asList("n"),"Create a new town",Arrays.asList("/c town new or /c t n"),new NewTown(),town);
            ChunkyCommand forSale = new ChunkyCommand("forsale", Arrays.asList("fs"),"Sets land for sale",Arrays.asList("/c town forsale <cost> or /c t fs <cost>"),new ForSale(),town);
            ChunkyCommand notForSale = new ChunkyCommand("notforsale", Arrays.asList("nfs"),"Removes land from market",Arrays.asList("/c town notforsale or /c t nfs"),new NotForSale(),town);
            ChunkyCommand addResident = new ChunkyCommand("add", Arrays.asList("a"),"Adds resident to town.",Arrays.asList("/c town add <player> or /c t k <player>"),new AddResident(),town);
            ChunkyCommand kickResident = new ChunkyCommand("kick", Arrays.asList("k"),"Removes resident from town.",Arrays.asList("/c town kick <player> or /c t r <player>"),new KickResident(),town);
            ChunkyCommand tax = new ChunkyCommand("tax", Arrays.asList("tax"),"Taxes all residents.",Arrays.asList("/c town tax <1-100> or /c t tax <1-100>"),new Tax(),town);
            ChunkyCommand vote = new ChunkyCommand("vote", Arrays.asList("v"),"Votes for a mayor.",Arrays.asList("/c town vote <player> or /c t v <player>"),new Vote(),town);
            ChunkyCommand spawn = new ChunkyCommand("spawn", Arrays.asList("s"),"Teleport to town.",Arrays.asList("/c town spawn or /c t s"),new Spawn(),town);
            ChunkyCommand leave = new ChunkyCommand("leave", Arrays.asList("lv"),"Leave from the town.",Arrays.asList("/c town leave or /c t lv"),new Leave(),town);
            ChunkyCommand withdraw = new ChunkyCommand("withdraw", Arrays.asList("w"),"Withdraw money.",Arrays.asList("/c town withdraw <amount> or /c t w <amount>"),new Withdraw(),town);

            ChunkyCommand set = new ChunkyCommand("set", Arrays.asList("s"),"Change various options.",Arrays.asList("/c town set ? or /c t s ?"),new Set(),town);
            ChunkyCommand setName = new ChunkyCommand("name", Arrays.asList("n"),"Sets town name.",Arrays.asList("/c town set name <name> or /c t s n <name>"),new SetName(),set);
            ChunkyCommand setMayor = new ChunkyCommand("mayor", Arrays.asList("m"),"Sets new mayor.",Arrays.asList("/c town set mayor <name> or /c t s m <name>"),new SetMayor(),set);

            ChunkyCommand toggle = new ChunkyCommand("toggle", Arrays.asList("t"),"Toggle various options.",Arrays.asList("/c town toggle ? or /c t t ?"),new Toggle(),town);
            ChunkyCommand toggleAssistant = new ChunkyCommand("assistant", Arrays.asList("a"),"Toggles an assistant.",Arrays.asList("/c town toggle assistant <name> or /c t t a <name>"),new ToggleAssistant(),toggle);

            Chunky.getModuleManager().registerCommand(town);
            Chunky.getModuleManager().registerCommand(newTown);
            Chunky.getModuleManager().registerCommand(vote);
            Chunky.getModuleManager().registerCommand(list);
            Chunky.getModuleManager().registerCommand(spawn);
            Chunky.getModuleManager().registerCommand(leave);
            Chunky.getModuleManager().registerCommand(withdraw);

            Chunky.getModuleManager().registerCommand(forSale);
            Chunky.getModuleManager().registerCommand(notForSale);
            Chunky.getModuleManager().registerCommand(tax);

            Chunky.getModuleManager().registerCommand(addResident);
            Chunky.getModuleManager().registerCommand(kickResident);

            Chunky.getModuleManager().registerCommand(set);
            Chunky.getModuleManager().registerCommand(setName);
            Chunky.getModuleManager().registerCommand(setMayor);

            Chunky.getModuleManager().registerCommand(toggle);
            Chunky.getModuleManager().registerCommand(toggleAssistant);

        } catch (ChunkyUnregisteredException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
