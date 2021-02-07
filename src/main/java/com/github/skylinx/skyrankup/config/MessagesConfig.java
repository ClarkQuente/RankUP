package com.github.skylinx.skyrankup.config;

import com.marinho.config.provider.annotation.ConfigValue;
import lombok.Getter;

import java.util.List;

public class MessagesConfig {

    @Getter
    @ConfigValue(index = "already_last_rank", replaceColor = "&")
    private static String ALREADY_LAST_RANK;

    @Getter
    @ConfigValue(index = "without_coins", replaceColor = "&")
    private static String WITHOUT_COINS;

    @Getter
    @ConfigValue(index = "chat_ranks.header", replaceColor = "&")
    private static List<String> RANKS_HEADER;

    @Getter
    @ConfigValue(index = "chat_ranks.footer", replaceColor = "&")
    private static List<String> RANKS_FOOTER;

    @Getter
    @ConfigValue(index = "chat_ranks.rank_info", replaceColor = "&")
    private static String RANK_INFO;

    @Getter
    @ConfigValue(index = "chat_ranks.cost", replaceColor = "&")
    private static String RANK_COST;

    @Getter
    @ConfigValue(index = "chat_ranks.no_cost", replaceColor = "&")
    private static String NO_COST;

    @Getter
    @ConfigValue(index = "chat_ranks.last_rank", replaceColor = "&")
    private static String LAST_RANK;

}
