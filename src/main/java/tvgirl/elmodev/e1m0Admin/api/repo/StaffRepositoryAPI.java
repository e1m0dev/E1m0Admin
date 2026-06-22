package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface StaffRepository {
    String getAdminPrefix(UUID id);
    int getAdminWeight(UUID id);

    void setAdminStatus(UUID id, String nick, int weight, int salary, String prefix);

    void upAdminStatus(UUID id, int weight, int salary, String prefix);
    void downAdminStatus(UUID id, int weight, int salary, String prefix);
}
