package tvgirl.elmodev.e1m0Admin.state.secretcode;

import org.bukkit.Bukkit;
import tvgirl.elmodev.e1m0admin.api.state.secretcode.SecretCodeState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class SecretCodeManager {

    private final HashMap<UUID, SecretCodeState> codeState;
    private final HashSet<UUID> blockedAdmins;

    public SecretCodeManager(HashMap<UUID, SecretCodeState> codeState, HashSet<UUID> blockedAdmins) {
        this.codeState = codeState;
        this.blockedAdmins = blockedAdmins;
    }

    public void addAdminAccess(SecretCodeState state) {
        codeState.put(state.getAdminID(), state);
    }

    public void takeAdminAccess(UUID id) {
        codeState.remove(id);
    }

    public SecretCodeState getAdminByID(UUID id) {
        return codeState.get(id);
    }

    public boolean isBlocked(UUID id) {
        return blockedAdmins.contains(id);
    }

    public void addBlockAdmin(UUID id) {
        blockedAdmins.add(id);
    }

    public void removeBlockAdmin(UUID id) {
        blockedAdmins.remove(id);
    }
}
