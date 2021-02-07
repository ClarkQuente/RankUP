package com.github.skylinx.skyrankup.schedulers;

import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PlayerRankSave extends BukkitRunnable {

    private final PlayerRankManager playerRankManager;
    private final DatabaseFactory databaseFactory;

    @Override
    public void run() {
        if (playerRankManager.getCache().isEmpty())
            return;

        for (val playerRank : playerRankManager.getCache().values()) {
            CompletableFuture.runAsync(() -> databaseFactory.save(playerRank));

            if (Bukkit.getPlayer(playerRank.getUniqueId()) == null)
                playerRankManager.getCache().remove(playerRank.getUniqueId());
            else
                playerRank.setModified(false);
        }
    }

}
