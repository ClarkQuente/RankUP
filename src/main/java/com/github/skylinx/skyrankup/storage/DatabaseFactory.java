package com.github.skylinx.skyrankup.storage;

import com.github.skylinx.skyrankup.objects.PlayerRank;

import java.sql.Connection;
import java.util.UUID;

public interface DatabaseFactory {

    void closeConnection();
    void createTable();

    Connection getConnection();

    PlayerRank find(UUID uniqueId);
    PlayerRank find(String name);

    void save(PlayerRank playerRank);

}
