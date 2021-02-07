package com.github.skylinx.skyrankup.listener;

import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final PlayerRankManager playerRankManager;
    private final DatabaseFactory databaseFactory;

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        val uniqueId = event.getPlayer().getUniqueId();

        if (!playerRankManager.getCache().containsKey(uniqueId))
            playerRankManager.getCache().put(uniqueId, databaseFactory.find(uniqueId));
    }

}
