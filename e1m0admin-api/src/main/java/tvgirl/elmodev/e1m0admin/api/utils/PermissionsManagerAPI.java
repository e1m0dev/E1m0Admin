package tvgirl.elmodev.e1m0admin.api.utils;

import java.util.UUID;

public interface PermissionsManagerAPI {
    boolean checkSystem(UUID id); // | Есть ли у администратора доступ вообще к командам?
    boolean checkSecretCodeAccess(UUID id); // | Есть ли у администратора доступ к админке?
}
