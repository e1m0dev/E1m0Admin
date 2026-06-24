package tvgirl.elmodev.e1m0Admin.api.utils;

import java.util.UUID;

public interface PermissionsManagerAPI {

    void getAccess(UUID id);
    boolean checkAccessCommand(UUID id, String command);

}
