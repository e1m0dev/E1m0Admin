package tvgirl.elmodev.e1m0Admin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AdminDelEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UUID adminID;
    private final UUID staffID;

    public AdminDelEvent(UUID adminID, UUID staffID) {
        this.adminID = adminID;
        this.staffID = staffID;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public UUID getStaffID() {
        return staffID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
