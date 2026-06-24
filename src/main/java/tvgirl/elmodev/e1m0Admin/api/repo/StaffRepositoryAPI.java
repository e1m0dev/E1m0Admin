package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface StaffRepositoryAPI {
    void setAdminStatus(UUID id, String nick, int weight, int salary, String prefix, String IP);

    void upAdminStatus(UUID adminID);
    void downAdminStatus(UUID adminID);

    void giveBonus(UUID staffID, UUID adminID, int sum, String message);
}