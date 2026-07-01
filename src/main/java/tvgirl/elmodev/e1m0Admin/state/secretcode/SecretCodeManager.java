package tvgirl.elmodev.e1m0Admin.state.secretcode;

import java.util.HashMap;
import java.util.UUID;

public class SecretCodeManager {

    private final HashMap<UUID, SecretCodeState> codeState;

    public SecretCodeManager(HashMap<UUID, SecretCodeState> codeState) {
        this.codeState = codeState;
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
}
