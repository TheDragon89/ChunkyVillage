package org.getchunky.chunkyvillage.listeners;

import org.bukkit.ChatColor;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkChangeEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerListener;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyCoordinates;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class ChunkyEvents extends ChunkyPlayerListener {
    @Override
    public void onPlayerChunkClaim(ChunkyPlayerChunkClaimEvent event) {
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(event.getChunkyPlayer());
        if(chunkyTown == null) return;
        event.setCancelled(true);
        if(chunkyTown.isForSale(event.getChunkyChunk())) {
            if(event.getChunkyChunk().isDirectlyOwnedBy(event.getChunkyPlayer())) {
                Language.sendBad(event.getChunkyPlayer(),"You cannot buy your own chunk.");
                return;
            }
            chunkyTown.buyChunk(event.getChunkyChunk(),event.getChunkyPlayer());
            return;}

        if(chunkyTown.isAssistantOrMayor(event.getChunkyPlayer())) {
            if(chunkyTown.claimedChunkCount() >= chunkyTown.maxChunks()) {
                Language.sendBad(event.getChunkyPlayer(),"You need to have more residents before you can expand.");
                return;
            }
            if(!event.getChunkyChunk().isOwned()) {
                if(!isAdjacent(event.getChunkyChunk().getCoord(),chunkyTown)){
                    Language.sendBad(event.getChunkyPlayer(), "You may only expand next to owned chunks.");
                    return;
                }
                event.getChunkyChunk().setOwner(chunkyTown,true,true);
                Language.sendGood(event.getChunkyPlayer(),"You expanded " + chunkyTown.getName());
                event.getChunkyChunk().save();
                return;
            }
            else
                Language.CHUNK_OWNED.bad(event.getChunkyPlayer(),event.getChunkyChunk().getOwner().getName());
        }
    }


    private boolean isAdjacent(ChunkyCoordinates coordinates, ChunkyTown chunkyTown) {
        int X = coordinates.getX();
        int Z = coordinates.getZ();

        String world = coordinates.getWorld();
        for(int x=-1;x<2;x++) {
            for(int z=-1;z<2;z++) {
                if(x==0 && z==0) continue;
                ChunkyCoordinates coord = new ChunkyCoordinates(world,X+x,Z+z);
                ChunkyChunk chunk = ChunkyManager.getChunk(coord);
                if(chunk.isOwned() && chunk.isOwnedBy(chunkyTown)) return true;}}
        return false;
    }

    @Override
    public void onPlayerChunkChange(ChunkyPlayerChunkChangeEvent event) {
        ChunkyTown chunkyTown = ChunkyTownManager.getTown(event.getChunkyPlayer());
        if(chunkyTown == null) return;
        if(chunkyTown.isForSale(event.getToChunk()))
            event.setMessage(event.getToChunk().getOwner().getName() + " - on sale for: " + ChatColor.YELLOW  + Chunky.getMethod().format(chunkyTown.getCost(event.getToChunk())));
    }
}

