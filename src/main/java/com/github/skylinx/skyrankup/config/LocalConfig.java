package com.github.skylinx.skyrankup.config;

import com.marinho.config.provider.annotation.ConfigValue;
import lombok.Getter;

import java.util.List;

public class LocalConfig {

    @Getter
    @ConfigValue(index = "settings.auto_rank_up.active")
    private static boolean AUTO_RANK_UP;

    @Getter
    @ConfigValue(index = "settings.auto_rank_up.check_delay")
    private static int RANK_CHECK_DELAY;

    @Getter
    @ConfigValue(index = "settings.auto_save_data.active")
    private static boolean AUTO_SAVE_DATA;

    @Getter
    @ConfigValue(index = "settings.auto_save_data.check_delay")
    private static int SAVE_CHECK_DELAY;

    @Getter
    @ConfigValue(index = "view.ranks.title", replaceColor = "&")
    private static String RANKS_TITLE;

    @Getter
    @ConfigValue(index = "view.ranks.size")
    private static int RANKS_SIZE;

    @Getter
    @ConfigValue(index = "settings.ranks_menu")
    private static boolean RANKS_MENU;

    @Getter
    @ConfigValue(index = "settings.rank_up_confirmation")
    private static boolean RANK_UP_CONFIRMATION;

    @Getter
    @ConfigValue(index = "view.confirmation.title", replaceColor = "&")
    private static String CONFIRMATION_TITLE;

    @Getter
    @ConfigValue(index = "view.confirmation.info", replaceColor = "&")
    private static List<String> INFO_COST;

    @Getter
    @ConfigValue(index = "view.confirmation.accept.display", replaceColor = "&")
    private static String ACCEPT_DISPLAY;

    @Getter
    @ConfigValue(index = "view.confirmation.cancel.display", replaceColor = "&")
    private static String CANCEL_DISPLAY;

    @Getter
    @ConfigValue(index = "view.confirmation.accept.description", replaceColor = "&")
    private static List<String> ACCEPT_DESCRIPTION;

    @Getter
    @ConfigValue(index = "view.confirmation.cancel.description", replaceColor = "&")
    private static List<String> CANCEL_DESCRIPTION;

    //@Getter
    //@ConfigValue(index = "view.confirmation.size")
    //private static int CONFIRMATION_SIZE;

}
