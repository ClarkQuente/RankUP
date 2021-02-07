package com.github.skylinx.skyrankup.managers;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.objects.Rank;
import com.github.skylinx.skyrankup.utils.ItemBuilder;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.*;

@RequiredArgsConstructor
public class RankManager {

    @Getter
    private final Set<Rank> ranks = Sets.newLinkedHashSet();

    private final SkyRankUP plugin;

    public Rank findByPriority(int priority) {
        for (val rank : ranks)
            if (rank.getPriority() == priority)
                return rank;

        return null;
    }

    public Rank findByName(String name) {
        for (val rank : ranks)
            if (rank.getName().equalsIgnoreCase(name))
                return rank;

        return null;
    }

    public void loadRanks(FileConfiguration config) {
        val section = config.getConfigurationSection("ranks");
        if (section == null)
            return;

        for (val key : section.getKeys(false)) {
            val value = section.getConfigurationSection(key);

            val name = value.getString("name");
            val tag = value.getString("tag").replace("&", "§");

            val cost = value.getDouble("cost");

            val quantity = value.getInt("icon.quantity");
            val display = value.getString("icon.display");
            val description = value.getStringList("icon.description")
                    .stream()
                    .map(l -> l.replace("{cost}", String.valueOf(cost)))
                    .collect(Collectors.toList());

            val icon = value.contains("icon.skull_texture")
                    ? new ItemBuilder(value.getString("icon.skull_texture"))
                    .setQuantity(quantity)
                    .setDisplayName(display)
                    .setLore(description)
                    .build() : value.contains("icon.durability")
                    ? new ItemBuilder(Material.valueOf(value.getString("icon.material")),
                    quantity, value.getInt("icon.durability"))
                    .setDisplayName(display)
                    .setLore(description)
                    .build()
                    : new ItemBuilder(Material.valueOf(value.getString("icon.material")), quantity)
                    .setDisplayName(display)
                    .setLore(description)
                    .build();

            val slot = value.getInt("icon.slot");
            val priority = value.getInt("priority");

            var commands = new ArrayList<String>();

            if (value.contains("commands"))
                commands = (ArrayList<String>) value.getStringList("commands");

            var actions = new ArrayList<String>();

            if(value.contains("actions"))
                actions = (ArrayList<String>) value.getStringList("actions");

            ranks.add(new Rank(name, tag, icon, slot, priority, cost, commands, actions));
        }

        plugin.getLogger().info(format("%d ranks loaded from the configuration file.", ranks.size()));
    }

}
