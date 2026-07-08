package tvgirl.elmodev.e1m0admin.api.repo;

import java.util.UUID;

public interface SystemRepositoryAPI {
    boolean checkAdminInBase(UUID adminID); // | Существует ли администратор вообще в базе данных?

    String getAdminPrefix(UUID adminID);  // | Какой префикс у администратора?

    int getAdminWeight(UUID adminID); // | Какой уровень у администратора?

    int getAdminSalary(UUID adminID); // | Какая зарплата у администратора?
}
