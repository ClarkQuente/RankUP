package com.github.skylinx.skyrankup.commands;

import com.github.skylinx.skyrankup.config.LocalConfig;
import com.github.skylinx.skyrankup.config.MessagesConfig;
import com.github.skylinx.skyrankup.managers.RankManager;
import com.github.skylinx.skyrankup.objects.Rank;
import com.github.skylinx.skyrankup.ui.RanksUI;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CommandRanks {

    private final RanksUI ranksUI;
    private final RankManager rankManager;

    @Command(
            name = "ranks",
            aliases = {"ranklist"},
            target = CommandTarget.PLAYER
    )
    public void ranksCommand(Context<Player> context) {
        if (LocalConfig.isRANKS_MENU()) {
            ranksUI.openInventory(context.getSender());
            return;
        }

        MessagesConfig.getRANKS_HEADER().forEach(context::sendMessage);

        for (Rank rank : rankManager.getRanks()) {
            val nextRank = rankManager.findByPriority(rank.getPriority() + 1);

            context.sendMessage(MessagesConfig.getRANK_INFO()
                    .replace("{rankName}", rank.getName())
                    .replace("{rankTag}", rank.getTag())
                    .replace("{nextRankName}", nextRank == null
                            ? MessagesConfig.getLAST_RANK()
                            : nextRank.getName())
                    .replace("{nextRankTag}", nextRank == null
                            ? MessagesConfig.getLAST_RANK()
                            : nextRank.getTag())
                    .replace("{cost}", nextRank == null
                            ? MessagesConfig.getNO_COST()
                            : MessagesConfig.getRANK_COST()
                            .replace("{cost}", String.valueOf(nextRank.getCost()))
                    )
            );
        }

        MessagesConfig.getRANKS_FOOTER().forEach(context::sendMessage);
    }

}
