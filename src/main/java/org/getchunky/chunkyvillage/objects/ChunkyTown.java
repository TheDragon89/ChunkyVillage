package org.getchunky.chunkyvillage.objects;

import com.nijikokun.register.payment.Method;
import org.bukkit.ChatColor;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.Config;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChunkyTown extends ChunkyObject {


    public boolean isMayor(ChunkyPlayer chunkyPlayer) {
        return (ChunkyPlayer)this.getOwner() == chunkyPlayer;
    }

    public boolean isAssistant(ChunkyPlayer chunkyPlayer) {
        return getAssistants().contains(chunkyPlayer.getName());
    }

    public boolean isAssistantOrMayor(ChunkyPlayer chunkyPlayer) {
        return (isMayor(chunkyPlayer) || isAssistant(chunkyPlayer));
    }

    public HashSet<String> getAssistants() {
        HashSet<String> result = new HashSet<String> ();
        try {
            JSONArray assistants = this.getJSONArray("assistants");
            for(int i=0;i<assistants.length();i++) {
                result.add(assistants.getString(i));}
        } catch (JSONException e) {}

        return result;
    }

    public boolean addAssistant(ChunkyPlayer chunkyPlayer) {
        HashSet<String> assists = getAssistants();
        boolean result = assists.add(chunkyPlayer.getName());
        try {
            put("assistants",assists);
            this.save();
        } catch (JSONException e) {
        }
        return result;
    }

    public boolean removeAssistant(ChunkyPlayer chunkyPlayer) {
        HashSet<String> assists = getAssistants();
        boolean result = assists.remove(chunkyPlayer.getName());
        try {
            put("assistants",assists);
            this.save();
        } catch (JSONException e) {
        }
        return result;
    }

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
        HashSet<ChunkyObject> ret = new HashSet<ChunkyObject>();
        ret.addAll(this.getOwnables().get(ChunkyPlayer.class.getName()));
        ret.add(getMayor());
        return ret;
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
        chunkyPlayer.setOwner(this,false,false);
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
        if(chunk.getOwner().equals(this)) {
            if(!pay(buyer,getCost(chunk))) return false;
            chunk.setOwner(buyer,false,true);
            setChunkNotForSale(chunk);
            Language.sendGood(buyer,"You have purchased this chunk!");
            return true;
        }

        if(!ChunkyTownManager.pay((ChunkyPlayer)chunk.getOwner(),buyer, getCost(chunk))) return false;
        chunk.setOwner(buyer,false,true);
        setChunkNotForSale(chunk);
        Language.sendGood(buyer,"You have purchased this chunk!");
        return true;

    }

    public int maxChunks() {
        return getResidents().size() * Config.getChunkBonusPerPlayer() + Config.getStartingChunks();
    }

    public int claimedChunkCount() {
        int i =0;
        i+= this.getOwnables().get(ChunkyChunk.class.getName()).size();
        for(ChunkyObject chunkyPlayer : getResidents()) {
            i += chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName()).size();}
        return i;
    }

    public double taxPlayers(double tax) {
        Method.MethodAccount account = getAccount();
        double sum=0;

        for(ChunkyObject chunkyObject : getResidents()) {
            Method.MethodAccount res = ChunkyTownManager.getAccount(chunkyObject);
            double amount = res.balance()*tax;
            sum+=amount;
            res.subtract(amount);
            Language.sendMessage((ChunkyPlayer)chunkyObject, ChatColor.AQUA + "You paid " + Chunky.getMethod().format(amount) + " in taxes.");
        }
        account.add(sum);
        return sum;
    }


}

