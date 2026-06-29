package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface SystemRepositoryAPI {

    boolean checkAdminInBase(UUID adminID); // | Существует ли администратор вообще в базе данных?

    int getAdminSalary(UUID adminID);

    int getAdminWeight(UUID adminID);

    String getAdminPrefix(UUID adminID);
}
