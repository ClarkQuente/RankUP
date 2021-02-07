package com.github.skylinx.skyrankup.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@AllArgsConstructor
public class Rank {

    private final String name, tag;
    private final ItemStack icon;
    private final int slot, priority;

    private double cost;
    private List<String> commands;
    private List<String> actions;

}
