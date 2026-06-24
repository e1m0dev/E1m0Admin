package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface SystemRepositoryAPI {

    int getAdminSalary(UUID id);
    int getAdminWeight(UUID id);

    String getAdminPrefix(UUID id);
}
