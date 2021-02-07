package com.github.skylinx.skyrankup.ui;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.objects.PlayerRank;
import com.github.skylinx.skyrankup.objects.Rank;
import com.github.skylinx.skyrankup.utils.ItemBuilder;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import lombok.val;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EvolutionUI extends SimpleInventory {

    private final SkyRankUP plugin = SkyRankUP.getInstance();
    private final RankManager rankManager = plugin.getRankManager();

    public EvolutionUI() {
        super("evolution.inventory", LocalConfig.getCONFIRMATION_TITLE(), 6 * 9);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        final PlayerRank playerRank = viewer.getPropertyMap().get("playerRank");

        if (playerRank == null)
            return;

        val rank = rankManager.findByPriority(playerRank.getRankId());
        val nextRank = playerRank.getNextRank();

        editor.setItem(13, InventoryItem.of(
                new ItemBuilder("2a3b8f681daad8bf436cae8da3fe8131f62a162ab81af639c3e0644aa6abac2f")
                        .setDisplayName(rank.getTag() + " §f» " + nextRank.getTag())
                        .setLore(LocalConfig.getINFO_COST()
                                .stream()
                                .map(l -> l.replace("{cost}", String.valueOf(nextRank.getCost())))
                                .collect(Collectors.toList())
                        )
                        .build()
        ));

        editor.setItem(20, InventoryItem.of(
                new ItemBuilder(rank.getIcon())
                        .setDisplayName(rank.getTag())
                        .setLore(new ArrayList<>())
                        .build())
        );

        editor.setItem(24, InventoryItem.of(
                new ItemBuilder(nextRank.getIcon())
                        .setDisplayName(nextRank.getTag())
                        .setLore(new ArrayList<>())
                        .build())
        );

        editor.setItem(39, InventoryItem.of(
                new ItemBuilder(Material.WOOL, 1, 14)
                        .setDisplayName(LocalConfig.getCANCEL_DISPLAY())
                        .setLore(LocalConfig.getCANCEL_DESCRIPTION())
                        .build()
        ).defaultCallback(event -> event.getPlayer().closeInventory()));

        editor.setItem(41, InventoryItem.of(
                new ItemBuilder(Material.WOOL, 1, 5)
                        .setDisplayName(LocalConfig.getACCEPT_DISPLAY())
                        .setLore(LocalConfig.getACCEPT_DESCRIPTION())
                        .build()
        ).defaultCallback(event -> {
            event.getPlayer().closeInventory();
            playerRank.evolve();
        }));
    }

}
