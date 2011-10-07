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
import org.getchunky.chunkyvillage.util.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ChunkyTown extends ChunkyObject {

    public boolean isMayor(ChunkyPlayer chunkyPlayer) {
        return this.getOwner() == chunkyPlayer;
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
        ChunkyObject oldOwner = this.getOwner();
        this.setOwner(mayor, true, false);
        if(oldOwner!=null) {
            oldOwner.setOwner(this,true,false);
            oldOwner.remove("mayor");
        }
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
        if(this.getOwnables().get(ChunkyPlayer.class.getName())!=null) ret.addAll(this.getOwnables().get(ChunkyPlayer.class.getName()));
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
        chunkyPlayer.setOwner(this,false,true);
        return this;
    }

    public ChunkyTown kickResident(ChunkyPlayer chunkyPlayer) {
        chunkyPlayer.setOwner(null, false, true);
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
        return chunk.has("cost") && chunk.isOwnedBy(this);
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
            HashSet<ChunkyObject> chunks = chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName());
            if(chunks != null) i+= chunks.size();
        }
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

    public JSONObject getVotes() {
        if(this.has("votes")) try {return this.getJSONObject("votes");} catch (JSONException e) {}

        JSONObject jsonObject = new JSONObject();
        try {this.put("votes",jsonObject);} catch (JSONException e) {}
        return jsonObject;
    }

    public int addVote(ChunkyPlayer chunkyPlayer, ChunkyPlayer candidate) {

        try {
            JSONObject votes = getVotes();
            votes.put(chunkyPlayer.getName(), candidate.getName());
            save();
            int i=0;
            Iterator keys = votes.keys();
            String name = candidate.getName();
            while(keys.hasNext()) {
                if(votes.getString(keys.next().toString()).equals(name)) i++;}
            return i;
        } catch (Exception ex) {}
        return 0;
    }

    public void clearVotes() {
        this.remove("votes");
    }

    public void printVotes(ChunkyPlayer chunkyPlayer) {
        HashMap<String, Integer> standings = new HashMap<String, Integer>();
        JSONObject votes = getVotes();
        Iterator keys = votes.keys();
        while (keys.hasNext()) {
            String voter = keys.next().toString();
            String candidate = null;
            try {candidate = votes.getString(voter);} catch (JSONException e) {};

            if(!standings.containsKey(candidate)) standings.put(candidate,1);
            else {
                Integer v = standings.get(candidate);
                v++;
                standings.put(candidate,v);
            }
        }
        Language.sendMessage(chunkyPlayer,ChatColor.GRAY + "|-------------------" +ChatColor.GREEN + "[Votes]" + ChatColor.GRAY + "-------------------|");
        for(String candidate : standings.keySet()) {
            Language.sendMessage(chunkyPlayer,ChatColor.GREEN + candidate + ": " + ChatColor.YELLOW + standings.get(candidate) + " votes");
        }
    }

    public void delete() {
        for(HashSet<ChunkyObject> chunkyObjects : this.getOwnables().values()) {
            for(ChunkyObject chunkyObject : chunkyObjects) {
                chunkyObject.setOwner(null,true,false);
            }}
        this.setOwner(null,false,true);
        save();
    }


}

