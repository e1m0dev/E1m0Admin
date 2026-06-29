package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.SystemRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public class AdminSystemRepository implements SystemRepositoryAPI {

    private final Jdbi jdbi;

    public AdminSystemRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public boolean checkAdminInBase(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID.toString());

        return admin != null;
    }

    @Override
    public int getAdminSalary(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID.toString());

        if (admin == null) {
            Bukkit.getLogger().warning("❗ | Администратора с UUID: %uuid - не существует"
                    .replace("uuid", adminID.toString()));

            return -1;
        }

        return admin.salary();
    }

    @Override
    public int getAdminWeight(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID.toString());

        if (admin == null) {
            Bukkit.getLogger().warning("❗ | Администратора с UUID: %uuid - не существует"
                    .replace("uuid", adminID.toString()));


            return -1;
        }

        return admin.weight();
    }

    @Override
    public String getAdminPrefix(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID.toString());

        if (admin == null) {
            Bukkit.getLogger().warning("❗ | Администратора с UUID: %uuid - не существует"
                    .replace("uuid", adminID.toString()));

            return "NULL";
        }

        return admin.prefix();
    }
}
