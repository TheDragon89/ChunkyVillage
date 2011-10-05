package org.getchunky.chunkyvillage.objects;

import com.nijikokun.register.payment.Method;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChunkyTown extends ChunkyObject {

    public ChunkyPlayer getMayor() {
        return (ChunkyPlayer)this.getOwner();
    }

    public ChunkyTown setMayor(ChunkyPlayer mayor) {
        if(this.getOwner() != null) this.remove("mayor");
        this.setOwner(mayor,true,false);
        try {
            mayor.put("mayor",this.getId());
        } catch (JSONException e) {}
        mayor.save();
        return this;
    }

    public ChunkyTown setHome(ChunkyChunk chunk) {
        try {
            put("home",chunk.getFullId());
        } catch (JSONException e) {}
        return this;
    }

    public ChunkyChunk getHome() {
        try {
            return (ChunkyChunk)ChunkyManager.getObject(this.get("home").toString());
        } catch (JSONException e) {}
        return null;
    }

    public Method.MethodAccount getAccount() {
        return Chunky.getMethod().getAccount("town-" + this.getId());
    }

    public HashSet<ChunkyObject> getResidents() {
        List<ChunkyPlayer> chunkyPlayerList = new ArrayList<ChunkyPlayer>();
        chunkyPlayerList.add(getMayor());
        return this.getOwnables().get(ChunkyPlayer.class.getName());
    }

    public boolean pay(ChunkyPlayer buyer, double amount) {
        Method.MethodAccount source = Chunky.getMethod().getAccount(buyer.getName());
        if(!source.hasEnough(amount)) {
            Language.sendBad(buyer,"You cannot afford " + Chunky.getMethod().format(amount));
            return false;
        }
        source.subtract(amount);
        this.getAccount().add(amount);
        return true;
    }

    public boolean isResident(ChunkyPlayer player) {
        return getResidents().contains(player);
    }

    public ChunkyTown addResident(ChunkyPlayer chunkyPlayer) {
        chunkyPlayer.setOwner(this,true,false);
        return this;
    }

    public ChunkyTown setChunkForSale(ChunkyChunk chunk, double cost) {
        try {
            chunk.put("cost", cost);
            chunk.save();
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return this;
    }

    public ChunkyTown setChunkNotForSale(ChunkyChunk chunk) {
        chunk.remove("cost");
        chunk.save();
        return this;
    }

    public boolean isForSale(ChunkyChunk chunk) {
        return chunk.has("cost");
    }

    public double getCost(ChunkyChunk chunk) {
        if(isForSale(chunk)) try {
            return chunk.getDouble("cost");
        } catch (JSONException e) {}
        return 0;
    }

    public boolean buyChunk(ChunkyChunk chunk, ChunkyPlayer buyer) {
        Language.sendGood(buyer,"Buying chunk...");
        if(chunk.getOwner().equals(this)) {
            if(!pay(buyer,getCost(chunk))) return false;
            chunk.setOwner(buyer,false,true);
            setChunkNotForSale(chunk);
            Language.sendGood(buyer,"You have purchased this chunk!");
            return true;
        }
        return false;
    }

}

