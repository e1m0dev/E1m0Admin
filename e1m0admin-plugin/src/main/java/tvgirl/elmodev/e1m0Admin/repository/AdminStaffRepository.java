package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0admin.api.repo.StaffRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0Admin.dao.BonusDAO;

import java.util.UUID;

public class AdminStaffRepository implements StaffRepositoryAPI {

    private final Jdbi jdbi;

    public AdminStaffRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void setAdminStatus(UUID id, String nick, int weight, int salary, String prefix, String IP) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.insert(id.toString(), nick, weight, salary, prefix, IP);
    }

    @Override
    public void deleteAdminStatus(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.delAdmin(adminID.toString());
    }

    @Override
    public void deleteAdminStatusLog(UUID adminID, UUID staffID, String reason) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        String uuid = UUID.randomUUID().toString();
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        adminDao.delAdminLogInsert(uuid, admin.getName(), staff.getName(), reason);
    }

    @Override
    public void systemDeleteAdminStatusLog(UUID adminID, UUID staffID, String reason) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        Player admin = Bukkit.getPlayer(adminID);
        String uuid = UUID.randomUUID().toString();

        adminDao.delAdminLogInsert(uuid, admin.getName(), "CONSOLE", reason);
    }

    @Override
    public void upAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.upStatus(adminID.toString(), newPrefix, newWeight, newSalary);
    }

    @Override
    public void downAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.downStatus(adminID.toString(), newPrefix, newWeight, newSalary);
    }

    @Override
    public void giveBonusLog(UUID adminID, UUID staffID, int sum, String message) {
        BonusDAO bonusDAO = jdbi.onDemand(BonusDAO.class);

        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        bonusDAO.insert(UUID.randomUUID().toString(), staffID.toString(), adminID.toString(), admin.getName(), staff.getName(), sum, message);
    }
}
