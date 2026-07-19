package tvgirl.elmodev.e1m0Admin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdminComplimentEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int compliments;
    private final UUID playerID;
    private final UUID adminID;

    public AdminComplimentEvent(int compliments, UUID playerID, UUID adminID) {
        this.compliments = compliments;
        this.playerID = playerID;
        this.adminID = adminID;
    }

    public int getCompliments() {
        return compliments;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public UUID getAdminID() {
        return adminID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
