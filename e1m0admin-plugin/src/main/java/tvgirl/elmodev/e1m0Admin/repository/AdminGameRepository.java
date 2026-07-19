package tvgirl.elmodev.e1m0Admin.repository;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0admin.api.repo.GameRepositoryAPI;

import java.util.UUID;

public class AdminGameRepository implements GameRepositoryAPI {

    private final Jdbi jdbi;

    public AdminGameRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void addCompliment(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.addCompliment(adminID.toString());
    }

    @Override
    public int getCompliments(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        return adminDao.getCompliments(adminID.toString());
    }
}
