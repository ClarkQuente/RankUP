package com.github.skylinx.skyrankup.commands;

import com.github.skylinx.skyrankup.SkyRankUP;
import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.config.MessagesConfig;
import com.github.skylinx.skyrankup.managers.PlayerRankManager;
import com.github.skylinx.skyrankup.ui.EvolutionUI;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CommandRankUP {

    private final PlayerRankManager playerRankManager;
    private final SkyRankUP plugin;

    @Command(
            name = "rankup",
            aliases = {"evoluir", "upar", "nextrank"},
            target = CommandTarget.PLAYER
    )
    public void ranksCommand(Context<Player> context) {
        val playerRank = playerRankManager.findByUniqueId(context.getSender().getUniqueId());

        if (playerRank == null)
            return;

        val nextRank = playerRank.getNextRank();

        if (nextRank == null) {
            context.sendMessage(MessagesConfig.getALREADY_LAST_RANK());
            return;
        }

        if (plugin.getEconomy().getBalance(context.getSender()) < nextRank.getCost()) {
            context.sendMessage(MessagesConfig.getWITHOUT_COINS()
                    .replace("{cost}", String.valueOf(nextRank.getCost())));
            return;
        }

        if (LocalConfig.isRANK_UP_CONFIRMATION()) {
            new EvolutionUI().openInventory(context.getSender(),
                    viewer -> viewer.getPropertyMap().set("playerRank", playerRank));
            return;
        }

        playerRank.evolve();
    }

}
