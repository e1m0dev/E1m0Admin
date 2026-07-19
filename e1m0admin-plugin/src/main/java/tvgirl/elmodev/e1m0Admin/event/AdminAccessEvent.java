package tvgirl.elmodev.e1m0Admin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdminAccessEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UUID adminID;

    public AdminAccessEvent(UUID adminID) {
        this.adminID = adminID;
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
