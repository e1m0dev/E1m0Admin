package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface StaffRepositoryAPI {
    void setAdminStatus(UUID adminID, String nick, int weight, int salary, String prefix, String IP);

    void deleteAdminStatus(UUID id);

    void deleteAdminStatusLog(UUID staffID, UUID adminID, String reason);

    void upAdminStatus(UUID adminID);
    void downAdminStatus(UUID adminID);

    void giveBonusLog(UUID staffID, UUID adminID, int sum, String message);
}