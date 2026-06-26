package tvgirl.elmodev.e1m0Admin.api.utils;

import java.util.UUID;

public interface PermissionsManagerAPI {
    boolean checkSecretCodeAccess(UUID id);
}
