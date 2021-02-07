package com.github.skylinx.skyrankup.api;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import lombok.var;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkyRankUPAPI {

    private static SkyRankUPAPI instance;

    public static SkyRankUPAPI getInstance() {
        return instance == null ? instance = new SkyRankUPAPI() : instance;
    }

    private final SkyRankUP plugin = SkyRankUP.getInstance();
    private final PlayerRankManager playerRankManager = plugin.getPlayerRankManager();
    private final DatabaseFactory databaseFactory = plugin.getDatabaseFactory();
    private final RankManager rankManager = plugin.getRankManager();

    // Takes name of the current rank of the informed player.
    public String getRankName(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return rankManager.findByPriority(playerRank.getRankId()).getName();
    }

    // Takes tag of the current rank of the informed player.
    public String getRankTag(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return rankManager.findByPriority(playerRank.getRankId()).getTag();
    }

    // Takes the name of the next rank of the informed player.
    public String getNextRankName(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return playerRank.getNextRank().getName();
    }

    // Takes the tag of the next rank of the informed player.
    public String getNextRankTag(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return playerRank.getNextRank().getTag();
    }

    // Takes icon of the current rank of the informed player.
    public ItemStack getRankIcon(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return rankManager.findByPriority(playerRank.getRankId()).getIcon().clone();
    }

    // Takes the icon of the next rank of the informed player.
    public ItemStack getNextRankIcon(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        if (playerRank == null)
            playerRank = databaseFactory.find(uniqueId);

        return playerRank.getNextRank().getIcon().clone();
    }

    // Sets the rank of the informed player.
    public void setPlayerRank(UUID uniqueId, String rankName) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        boolean cache = false;
        if (playerRank == null) {
            playerRank = databaseFactory.find(uniqueId);
            cache = true;
        }

        if (playerRank != null && rankManager.findByName(rankName) != null) {
            playerRank.setRankId(rankManager.findByName(rankName).getPriority());
            playerRank.setModified(true);

            if (cache)
                playerRankManager.insertCache(playerRank);
        }
    }

    // Evolves the rank of the informed player.
    public void evolveRank(UUID uniqueId) {
        var playerRank = playerRankManager.findByUniqueId(uniqueId);

        boolean cache = false;
        if (playerRank == null) {
            playerRank = databaseFactory.find(uniqueId);
            cache = true;
        }

        if (playerRank.getNextRank() == null)
            return;

        playerRank.evolve();

        if (cache)
            playerRankManager.insertCache(playerRank);
    }

}
