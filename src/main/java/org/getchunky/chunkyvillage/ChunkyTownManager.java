package org.getchunky.chunkyvillage;

import com.nijikokun.register.payment.Method;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.json.JSONException;

public class ChunkyTownManager {

    public static ChunkyTown getTown(String name) {
        return (ChunkyTown)ChunkyManager.getObject(ChunkyTown.class.getName(),name);
    }

    public static ChunkyTown getTown(ChunkyPlayer chunkyPlayer) {
        if(chunkyPlayer.getOwner() != null && chunkyPlayer.getOwner() instanceof ChunkyTown) return (ChunkyTown)chunkyPlayer.getOwner();
        ChunkyTown chunkyTown = isMayor(chunkyPlayer);
        if(chunkyTown != null) return chunkyTown;
        return null;
    }

    public static ChunkyTown isMayor (ChunkyPlayer chunkyPlayer) {
        try {
            String id = chunkyPlayer.get("mayor").toString();
            return getTown(id);
        } catch (JSONException e) {return null;}
    }

    public static boolean pay(ChunkyPlayer seller, ChunkyPlayer buyer, double amount) {
        Method.MethodAccount source = Chunky.getMethod().getAccount(buyer.getName());
        Method.MethodAccount target = Chunky.getMethod().getAccount(seller.getName());
        if(!source.hasEnough(amount)) {
            Language.sendBad(buyer, "You cannot afford " + Chunky.getMethod().format(amount));
            return false;
        }
        source.subtract(amount);
        target.add(amount);
        return true;
    }

}
