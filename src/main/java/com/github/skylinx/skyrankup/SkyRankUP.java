package com.github.skylinx.skyrankup;

import com.github.skylinx.skyrankup.commands.CommandRankUP;
import com.github.skylinx.skyrankup.commands.CommandRanks;
import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.config.MessagesConfig;
import com.github.skylinx.skyrankup.listener.PlayerListener;
import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.github.skylinx.skyrankup.schedulers.AutoRankUP;
import com.github.skylinx.skyrankup.schedulers.PlayerRankSave;
import com.github.skylinx.skyrankup.storage.DatabaseFactory;
import com.github.skylinx.skyrankup.storage.types.MySQLDatabaseType;
import com.github.skylinx.skyrankup.storage.types.SQLiteDatabaseType;
import com.github.skylinx.skyrankup.ui.RanksUI;
import com.github.skylinx.skyrankup.utils.CustomConfig;
import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import com.marinho.config.provider.ConfigProvider;
import com.marinho.config.provider.exception.InvalidIndexException;
import lombok.Getter;
import lombok.val;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

@Getter
public final class SkyRankUP extends JavaPlugin {

    private Economy economy;

    private DatabaseFactory databaseFactory;

    private CustomConfig ranksConfig;
    private CustomConfig messagesConfig;

    private RankManager rankManager;
    private PlayerRankManager playerRankManager;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().warning("Vault & Plugin de Economia não" +
                    " foram encontrados, plugin desabilitado.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        // Initialize database.
        databaseFactory = getConfig().getString("settings.data_storage.storage_type")
                .equalsIgnoreCase("MYSQL")
                ?
                new MySQLDatabaseType(
                        getConfig().getString("settings.data_storage.mysql_credentials.host"),
                        getConfig().getString("settings.data_storage.mysql_credentials.port"),
                        getConfig().getString("settings.data_storage.mysql_credentials.database"),
                        getConfig().getString("settings.data_storage.mysql_credentials.username"),
                        getConfig().getString("settings.data_storage.mysql_credentials.password"))
                :
                new SQLiteDatabaseType(
                        this,
                        getConfig().getString("settings.data_storage.sqlite_file.name")
                );
        databaseFactory.createTable();

        // Loading configurations files.
        try {
            ranksConfig = new CustomConfig("ranks", this);
            messagesConfig = new CustomConfig("messages", this);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Loading configurations settings and messages.
        try {
            ConfigProvider.load(LocalConfig.class.newInstance(), getConfig());
            ConfigProvider.load(MessagesConfig.class.newInstance(), messagesConfig);
        } catch (InvalidIndexException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        rankManager = new RankManager(this);
        // Loading ranks from ranks configuration.
        rankManager.loadRanks(ranksConfig);

        playerRankManager = new PlayerRankManager();

        for (val player : Bukkit.getOnlinePlayers()) {
            val uniqueId = player.getUniqueId();

            if (!playerRankManager.getCache().containsKey(uniqueId))
                playerRankManager.getCache().put(uniqueId, databaseFactory.find(uniqueId));
        }

        // Registering created listeners.
        InventoryManager.enable(this);
        getPluginManager().registerEvents(new PlayerListener(
                playerRankManager, databaseFactory), this);

        // Registering created commands.
        val frame = new BukkitFrame(this);
        frame.registerCommands(
                new CommandRanks(new RanksUI(), rankManager),
                new CommandRankUP(playerRankManager, this)
        );

        // Active AutoRankUP task.
        if (LocalConfig.isAUTO_RANK_UP())
            // Check AutoRankUP task.
            getScheduler().runTaskTimerAsynchronously(this,
                    new AutoRankUP(playerRankManager, this),
                    20L * LocalConfig.getRANK_CHECK_DELAY(),
                    20L * LocalConfig.getRANK_CHECK_DELAY());

        // Active Auto Save Data task.
        if (LocalConfig.isAUTO_SAVE_DATA())
            // Save data task and remove inactive cache.
            getScheduler().runTaskTimerAsynchronously(this,
                    new PlayerRankSave(playerRankManager, databaseFactory),
                    1200L * LocalConfig.getSAVE_CHECK_DELAY(),
                    1200L * LocalConfig.getSAVE_CHECK_DELAY());
    }

    @Override
    public void onDisable() {
        if (playerRankManager.getCache().isEmpty())
            return;

        for (PlayerRank playerRank : playerRankManager.getCache().values()) {
            databaseFactory.save(playerRank);

            if (Bukkit.getPlayer(playerRank.getUniqueId()) == null)
                playerRankManager.getCache().remove(playerRank.getUniqueId());
            else
                playerRank.setModified(false);
        }

        databaseFactory.closeConnection();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        val rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        economy = rsp.getProvider();
        return economy != null;
    }

    public static SkyRankUP getInstance() {
        return getPlugin(SkyRankUP.class);
    }

}
