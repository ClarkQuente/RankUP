package com.github.skylinx.skyrankup.ui;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.objects.Rank;
import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;

public class RanksUI extends SimpleInventory {

    private final SkyRankUP plugin = SkyRankUP.getInstance();
    private final RankManager rankManager = plugin.getRankManager();

    public RanksUI() {
        super("ranks.inventory", LocalConfig.getRANKS_TITLE(), LocalConfig.getRANKS_SIZE() * 9);
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        for (Rank rank : rankManager.getRanks())
            editor.setItem(rank.getSlot(), InventoryItem.of(rank.getIcon()));
    }

}
