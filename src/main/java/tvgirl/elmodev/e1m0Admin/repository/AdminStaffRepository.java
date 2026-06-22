package tvgirl.elmodev.e1m0Admin.repository;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.SystemRepository;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0Admin.state.Admin;

import java.util.UUID;

public class AdminSystemRepository implements SystemRepository {

    private final Jdbi jdbi;

    public AdminSystemRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public int adminSalary(UUID id) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        return admin.salary();
    }

}
