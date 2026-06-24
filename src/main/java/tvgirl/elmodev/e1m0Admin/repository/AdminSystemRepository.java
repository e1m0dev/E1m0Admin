package tvgirl.elmodev.e1m0Admin.repository;

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
    public int getAdminSalary(UUID id) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        return admin.salary();
    }

    @Override
    public String getAdminPrefix(UUID id) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        return admin.prefix();
    }

    @Override
    public int getAdminWeight(UUID id) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        return admin.weight();
    }
}
