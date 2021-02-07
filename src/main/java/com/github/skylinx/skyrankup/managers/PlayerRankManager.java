package com.github.skylinx.skyrankup.managers;

import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public class PlayerRankManager {

    @Getter
    private final Map<UUID, PlayerRank> cache = Maps.newHashMap();

    public PlayerRank findByUniqueId(UUID uniqueId) {
        return cache.get(uniqueId);
    }

    public void insertCache(PlayerRank playerRank) {
        cache.put(playerRank.getUniqueId(), playerRank);
    }

    public void removeCache(UUID uniqueId) {
        cache.remove(uniqueId);
    }

}
