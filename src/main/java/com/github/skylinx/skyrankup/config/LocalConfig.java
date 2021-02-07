package com.github.skylinx.skyrankup.config;

import com.marinho.config.provider.annotation.ConfigValue;

import java.util.List;

public class LocalConfig {

    @ConfigValue(index = "settings.auto_rank_up.active")
    public static boolean AUTO_RANK_UP;

    @ConfigValue(index = "settings.auto_rank_up.check_delay")
    public static int RANK_CHECK_DELAY;

    @ConfigValue(index = "settings.auto_save_data.active")
    public static boolean AUTO_SAVE_DATA;

    @ConfigValue(index = "settings.auto_save_data.check_delay")
    public static int SAVE_CHECK_DELAY;

    @ConfigValue(index = "view.ranks.title", replaceColor = "&")
    public static String RANKS_TITLE;

    @ConfigValue(index = "view.ranks.size")
    public static int RANKS_SIZE;

    @ConfigValue(index = "settings.ranks_menu")
    public static boolean RANKS_MENU;

    @ConfigValue(index = "settings.rank_up_confirmation")
    public static boolean RANK_UP_CONFIRMATION;

    @ConfigValue(index = "view.confirmation.title", replaceColor = "&")
    public static String CONFIRMATION_TITLE;

    @ConfigValue(index = "view.confirmation.info", replaceColor = "&")
    public static List<String> INFO_COST;

    @ConfigValue(index = "view.confirmation.accept.display", replaceColor = "&")
    public static String ACCEPT_DISPLAY;

    @ConfigValue(index = "view.confirmation.cancel.display", replaceColor = "&")
    public static String CANCEL_DISPLAY;

    @ConfigValue(index = "view.confirmation.accept.description", replaceColor = "&")
    public static List<String> ACCEPT_DESCRIPTION;

    @ConfigValue(index = "view.confirmation.cancel.description", replaceColor = "&")
    public static List<String> CANCEL_DESCRIPTION;

    //@Getter
    //@ConfigValue(index = "view.confirmation.size")
    //public static int CONFIRMATION_SIZE;

}
