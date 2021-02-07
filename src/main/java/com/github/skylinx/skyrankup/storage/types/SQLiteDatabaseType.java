package com.github.skylinx.skyrankup.storage.types;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SQLiteDatabaseType implements DatabaseFactory {

    protected final SQLiteDataSource dataSource = new SQLiteDataSource();

    public SQLiteDatabaseType(SkyRankUP plugin, String fileName) {
        val config = new SQLiteConfig();
        config.setCacheSize(10000);

        this.dataSource.setConfig(config);

        val file = new File(plugin.getDataFolder() + "/" + fileName + ".db");
        this.dataSource.setUrl("jdbc:sqlite:" + file);
    }

    @Override
    public void closeConnection() {
        if (this.getConnection() == null)
            return;

        try {
            this.getConnection().close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try (
                final Connection connection = getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS rankup" +
                                " (id INTEGER PRIMARY KEY," +
                                " uniqueId CHAR(36) NOT NULL UNIQUE," +
                                " name VARCHAR(16) NOT NULL," +
                                " rankId SMALLINT NOT NULL);")
        ) {
            statement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public PlayerRank find(UUID uniqueId) {
        var playerRankFuture = new CompletableFuture<PlayerRank>();

        try (
                final Connection connection = getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM rankup WHERE uniqueId = ?;")
        ) {
            statement.setString(1, uniqueId.toString());

            try (final ResultSet resultSet = statement.executeQuery()) {
                val playerRank = resultSet.next()
                        ? new PlayerRank(
                        uniqueId,
                        resultSet.getString("name"),
                        resultSet.getInt("rankId"),
                        false)
                        : Bukkit.getPlayer(uniqueId) != null
                        ? PlayerRank.of(uniqueId)
                        : null;

                playerRankFuture = CompletableFuture.supplyAsync(() -> playerRank);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        try {
            return playerRankFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public PlayerRank find(String name) {
        var playerRankFuture = new CompletableFuture<PlayerRank>();

        try (
                final Connection connection = getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM rankup WHERE name = ?;")
        ) {
            statement.setString(1, name);

            try (final ResultSet resultSet = statement.executeQuery()) {
                val playerRank = resultSet.next()
                        ? new PlayerRank(
                        UUID.fromString(resultSet.getString("uniqueId")),
                        name,
                        resultSet.getInt("rankId"),
                        false)
                        : Bukkit.getPlayer(name) != null
                        ? PlayerRank.of(Bukkit.getPlayer(name).getUniqueId())
                        : null;

                playerRankFuture = CompletableFuture.supplyAsync(() -> playerRank);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        try {
            return playerRankFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public void save(PlayerRank playerRank) {
        if (playerRank == null || !playerRank.isModified())
            return;

        try (
                final Connection connection = getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "INSERT OR REPLACE INTO rankup" +
                                " (uniqueId, name, rankId)" +
                                " VALUES" +
                                " (?, ?, ?);")
        ) {
            statement.setString(1, String.valueOf(playerRank.getUniqueId()));
            statement.setString(2, playerRank.getName());
            statement.setInt(3, playerRank.getRankId());

            statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

}
