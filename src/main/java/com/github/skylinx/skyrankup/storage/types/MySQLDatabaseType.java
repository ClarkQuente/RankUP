package com.github.skylinx.skyrankup.storage.types;

import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import com.zaxxer.hikari.HikariDataSource;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MySQLDatabaseType implements DatabaseFactory {

    protected final HikariDataSource dataSource = new HikariDataSource();

    public MySQLDatabaseType(String host, String port, String database, String user, String password) {
        this.dataSource.setMaximumPoolSize(13);

        this.dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.dataSource.setUsername(user);
        this.dataSource.setPassword(password);

        this.dataSource.addDataSourceProperty("autoReconnect", "true");
        this.dataSource.addDataSourceProperty("useSLL", "false");
        this.dataSource.addDataSourceProperty("characterEncoding", "utf-8");
        this.dataSource.addDataSourceProperty("encoding", "UTF-8");
        this.dataSource.addDataSourceProperty("useUnicode", "true");
        this.dataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
        this.dataSource.addDataSourceProperty("jdbcCompliantTruncation", "false");
        this.dataSource.addDataSourceProperty("cachePrepStmts", "true");
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", "275");
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    @Override
    public void closeConnection() {
        this.dataSource.close();
    }

    @Override
    public void createTable() {
        try (
                final Connection connection = getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS rankup" +
                                " (id INTEGER NOT NULL AUTO_INCREMENT," +
                                " uniqueId CHAR(36) NOT NULL UNIQUE," +
                                " name VARCHAR(16) NOT NULL," +
                                " rankId SMALLINT NOT NULL," +
                                " PRIMARY KEY (id));")
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
                        "INSERT INTO rankup" +
                                " (uniqueId, name, rankId)" +
                                " VALUES" +
                                " (?, ?, ?)" +
                                " ON DUPLICATE KEY UPDATE" +
                                " rankId = ?;")
        ) {
            statement.setString(1, String.valueOf(playerRank.getUniqueId()));
            statement.setString(2, playerRank.getName());
            statement.setInt(3, playerRank.getRankId());
            statement.setInt(4, playerRank.getRankId());

            statement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

}
