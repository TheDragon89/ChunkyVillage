package org.getchunky.chunkyvillage.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Tools {

    public static double parseDouble(String s) {
        try{
            return Double.parseDouble(s);
        } catch(Exception ex) {
            return -1;
        }
    }
}
