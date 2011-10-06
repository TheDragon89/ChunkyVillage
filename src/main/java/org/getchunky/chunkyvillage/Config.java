package org.getchunky.chunkyvillage;

import org.bukkit.util.config.Configuration;

public class Config {

    private static Configuration configuration;

    public static void load() {
        if(!ChunkyVillage.getInstance().getDataFolder().exists()) ChunkyVillage.getInstance().getDataFolder().mkdir();
        configuration = ChunkyVillage.getInstance().getConfiguration();
        configuration.load();
        loadDefaults();
        configuration.save();
    }

    private static void loadDefaults() {
        getChunkBonusPerPlayer();
        getStartingChunks();
    }

    public static int getChunkBonusPerPlayer() {
        return configuration.getInt("settings.town.chunkLimitBonusPerPlayer",4);
    }

    public static int getStartingChunks() {
        return configuration.getInt("settings.town.startingChunkLimit",10);
    }



}
