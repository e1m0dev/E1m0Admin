package tvgirl.elmodev.e1m0Admin.state.access;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AdminCommandManager {

    private final HashMap<UUID, AdminCommandState> accessCommand;

    public AdminCommandManager(HashMap<UUID, AdminCommandState> accessCommand) {
        this.accessCommand = accessCommand;
    }

    public void addAdminAccess(AdminCommandState state) {
        accessCommand.put(state.getAdminID(), state);
    }

    public void takeAdminAccess(UUID id) {
        accessCommand.remove(id);
    }

    public AdminCommandState getAdminByID(UUID id) {
        return accessCommand.get(id);
    }

    public boolean getCommandAccess(UUID id, String command) {
        boolean access = false;

        for(HashMap.Entry<UUID, AdminCommandState> i : accessCommand.entrySet()) {
            if(i.getKey() == id) {
                AdminCommandState state = i.getValue();

                List<String> allow = state.getAllowedCommands();
                for(String s : allow) {
                    access = s.contains(command);
                }
            }
        }

        return access;
    }
}
