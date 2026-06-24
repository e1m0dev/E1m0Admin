package tvgirl.elmodev.e1m0Admin.state.access;

import java.util.List;
import java.util.UUID;

public class AdminCommandState {

    private final UUID adminID;
    private final List<String> allowedCommands;

    public AdminCommandState(UUID adminID, List<String> allowedCommands) {
        this.adminID = adminID;
        this.allowedCommands = allowedCommands;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public List<String> getAllowedCommands() {
        return List.copyOf(allowedCommands);
    }
}
