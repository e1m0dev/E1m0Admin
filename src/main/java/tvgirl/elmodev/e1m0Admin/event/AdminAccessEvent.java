package tvgirl.elmodev.e1m0Admin.event;

import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AdminAccessEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player admin;

    public AdminAccessEvent(Player admin) {
        this.admin = admin;
    }

    public Player getAdmin() {
        return admin;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
