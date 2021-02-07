package com.github.skylinx.skyrankup.schedulers;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.objects.PlayerRank;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class AutoRankUP extends BukkitRunnable {

    private final PlayerRankManager playerRankManager;
    private final SkyRankUP plugin;

    @Override
    public void run() {
        if (!LocalConfig.isAUTO_RANK_UP())
            return;

        for (PlayerRank playerRank : playerRankManager.getCache().values()) {
            if (Bukkit.getPlayer(playerRank.getUniqueId()) == null)
                continue;

            if(playerRank.getNextRank() == null)
                continue;

            if (plugin.getEconomy().getBalance(playerRank.getName()) < playerRank.getNextRank().getCost())
                continue;

            playerRank.evolve();
        }
    }
}
