package com.github.skylinx.skyrankup.config;

import com.marinho.config.provider.annotation.ConfigValue;

import java.util.List;

public class MessagesConfig {

    @ConfigValue(index = "already_last_rank", replaceColor = "&")
    public static String ALREADY_LAST_RANK;

    @ConfigValue(index = "without_coins", replaceColor = "&")
    public static String WITHOUT_COINS;

    @ConfigValue(index = "chat_ranks.header", replaceColor = "&")
    public static List<String> RANKS_HEADER;

    @ConfigValue(index = "chat_ranks.footer", replaceColor = "&")
    public static List<String> RANKS_FOOTER;

    @ConfigValue(index = "chat_ranks.rank_info", replaceColor = "&")
    public static String RANK_INFO;

    @ConfigValue(index = "chat_ranks.cost", replaceColor = "&")
    public static String RANK_COST;

    @ConfigValue(index = "chat_ranks.no_cost", replaceColor = "&")
    public static String NO_COST;

    @ConfigValue(index = "chat_ranks.last_rank", replaceColor = "&")
    public static String LAST_RANK;

}
