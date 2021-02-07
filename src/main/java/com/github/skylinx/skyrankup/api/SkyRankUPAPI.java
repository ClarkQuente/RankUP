package com.github.skylinx.skyrankup.api;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public String getRankName(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return rankManager.findByPriority(playerRank.getRankId()).getName();
    }

    // Takes tag of the current rank of the informed player.
    public String getRankTag(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return rankManager.findByPriority(playerRank.getRankId()).getTag();
    }

    // Takes the name of the next rank of the informed player.
    public String getNextRankName(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return playerRank.getNextRank().getName();
    }

    // Takes the tag of the next rank of the informed player.
    public String getNextRankTag(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return playerRank.getNextRank().getTag();
    }

    // Takes icon of the current rank of the informed player.
    public ItemStack getRankIcon(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return rankManager.findByPriority(playerRank.getRankId()).getIcon().clone();
    }

    // Takes the icon of the next rank of the informed player.
    public ItemStack getNextRankIcon(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        if (playerRank == null)
            playerRank = databaseFactory.find(playerName);

        if (playerRank == null)
            return null;

        return playerRank.getNextRank().getIcon().clone();
    }

    // Sets the rank of the informed player.
    public void setPlayerRank(String playerName, String rankName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        boolean cache = false;
        if (playerRank == null) {
            playerRank = databaseFactory.find(playerName);
            cache = true;
        }

        if (playerRank == null)
            return;

        playerRank.setRankId(rankManager.findByName(rankName).getPriority());
        playerRank.setModified(true);

        if (cache)
            playerRankManager.insertCache(playerRank);
    }

    // Evolves the rank of the informed player.
    public void evolveRank(String playerName) {
        var playerRank = playerRankManager.findByUniqueId(
                Bukkit.getPlayer(playerName).getUniqueId());

        boolean cache = false;
        if (playerRank == null) {
            playerRank = databaseFactory.find(playerName);
            cache = true;
        }

        if (playerRank == null)
            return;

        if (playerRank.getNextRank() == null)
            return;

        playerRank.evolve();

        if (cache)
            playerRankManager.insertCache(playerRank);
    }

}
