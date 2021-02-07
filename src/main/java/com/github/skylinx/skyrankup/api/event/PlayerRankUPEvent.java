package com.github.skylinx.skyrankup.api.event;

import com.github.skylinx.skyrankup.objects.Rank;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerRankUPEvent extends Event implements Cancellable {

    public static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final Rank oldRank;
    private final Rank nextRank;
    private final double cost;

    private boolean isCancelled;

    public PlayerRankUPEvent(Player player, Rank oldRank, Rank nextRank, double cost) {
        this.player = player;
        this.oldRank = oldRank;
        this.nextRank = nextRank;
        this.cost = cost;

        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlersList() {
        return HANDLERS_LIST;
    }

}
