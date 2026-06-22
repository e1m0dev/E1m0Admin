package tvgirl.elmodev.e1m0Admin.repository;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.StaffRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0Admin.state.Admin;

import java.util.UUID;

public class AdminStaffRepository implements StaffRepositoryAPI {

    private final Jdbi jdbi;

    public AdminStaffRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void setAdminStatus(UUID id, String nick, int weight, int salary, String prefix) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        adminDao.insert(id, nick, weight, salary, prefix);
    }

    @Override
    public void upAdminStatus(UUID id, int weight, int salary, String prefix) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        adminDao.upStatus(id, weight, salary, prefix);
    }

    @Override
    public void downAdminStatus(UUID id, int weight, int salary, String prefix) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(id);
        adminDao.downStatus(id, weight, salary, prefix);
    }
}
