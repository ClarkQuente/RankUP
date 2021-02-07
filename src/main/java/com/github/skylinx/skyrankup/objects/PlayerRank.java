package com.github.skylinx.skyrankup.objects;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.api.event.PlayerRankUPEvent;
import com.github.skylinx.skyrankup.utils.nms.ActionBar;
import com.github.skylinx.skyrankup.utils.nms.Title;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;

import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerRank {

    private static final Random RANDOM = new Random();

    private final SkyRankUP plugin = SkyRankUP.getInstance();

    private final UUID uniqueId;
    private final String name;

    private int rankId;
    private boolean modified;

    public static PlayerRank of(UUID uniqueId) {
        return new PlayerRank(uniqueId, Bukkit.getPlayer(uniqueId).getName(), 1, false);
    }

    public Rank getNextRank() {
        return plugin.getRankManager().findByPriority(this.rankId + 1);
    }

    public void evolve() {
        val nextRank = getNextRank();
        val rank = plugin.getRankManager().findByPriority(this.rankId);

        val event = new PlayerRankUPEvent(
                Bukkit.getPlayer(this.uniqueId),
                nextRank,
                rank,
                nextRank.getCost()
        );

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        plugin.getEconomy().withdrawPlayer(Bukkit.getPlayer(this.uniqueId), nextRank.getCost());

        if (!nextRank.getActions().isEmpty())
            for (val action : nextRank.getActions()) {
                if (action.startsWith("[TITLE]")) {
                    val value = action.split("->")[1]
                            .replace("{oldRank}", String.valueOf(rank.getName()))
                            .replace("{newRank}", nextRank.getName())
                            .replace("{oldRankTag}", rank.getTag())
                            .replace("{newRankTag}", nextRank.getTag())
                            .replace("{player}", this.name)
                            .replace("&", "§");

                    var title = "";
                    var subTitle = "";

                    if (value.contains("{nl}")) {
                        title = value.split("\\{nl}")[0];
                        subTitle = value.split("\\{nl}")[1];
                    }

                    new Title(title, subTitle, 50, 50, 50)
                            .send(Bukkit.getPlayer(this.uniqueId));
                } else if (action.startsWith("[ACTIONBAR]")) {
                    val value = action.split("->")[1]
                            .replace("{oldRank}", String.valueOf(rank.getName()))
                            .replace("{newRank}", nextRank.getName())
                            .replace("{oldRankTag}", rank.getTag())
                            .replace("{newRankTag}", nextRank.getTag())
                            .replace("{player}", this.name)
                            .replace("&", "§");
                    new ActionBar(value).send(Bukkit.getPlayer(this.uniqueId));
                } else if (action.startsWith("[TITLE-ALL]")) {
                    val value = action.split("->")[1]
                            .replace("{oldRank}", String.valueOf(rank.getName()))
                            .replace("{newRank}", nextRank.getName())
                            .replace("{oldRankTag}", rank.getTag())
                            .replace("{newRankTag}", nextRank.getTag())
                            .replace("{player}", this.name)
                            .replace("&", "§");

                    var title = "";
                    var subTitle = "";

                    if (value.contains("{nl}")) {
                        title = value.split("\\{nl}")[0];
                        subTitle = value.split("\\{nl}")[1];
                    }

                    new Title(title, subTitle, 50, 50, 50)
                            .sendAll();
                } else if (action.startsWith("[ACTIONBAR-ALL]")) {
                    val value = action.split("->")[1]
                            .replace("{oldRank}", String.valueOf(rank.getName()))
                            .replace("{newRank}", nextRank.getName())
                            .replace("{oldRankTag}", rank.getTag())
                            .replace("{newRankTag}", nextRank.getTag())
                            .replace("{player}", this.name)
                            .replace("&", "§");
                    new ActionBar(value).sendAll();
                }
            }

        if (!nextRank.getCommands().isEmpty())
            for (val key : nextRank.getCommands()) {
                val values = key.split(":");

                val chance = Double.parseDouble(values[0]);
                val command = values[1]
                        .replace("{oldRank}", String.valueOf(rank.getName()))
                        .replace("{newRank}", nextRank.getName())
                        .replace("{oldRankTag}", rank.getTag())
                        .replace("{newRankTag}", nextRank.getTag())
                        .replace("{player}", this.name);

                if (chance > 100 || chance < 0)
                    throw new IllegalArgumentException("Percentage cannot be greater than 100 or less than 0.");

                val result = RANDOM.nextDouble() * 100;

                if (result <= chance)
                    Bukkit.getScheduler().runTask(plugin,
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            }

        ++this.rankId;
        this.modified = true;
    }

}
