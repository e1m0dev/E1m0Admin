package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.StaffRepositoryAPI;
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
        Bukkit.getLogger().info("AdminsDAO | Администратор удален.");
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        adminDao.delAdmin(adminID.toString());
    }

    @Override
    public void deleteAdminStatusLog(UUID adminID, UUID staffID, String reason) {
        Bukkit.getLogger().info("AdminsDAO | Администратор удален и отпечатан лог");
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        String uuid = UUID.randomUUID().toString();
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        adminDao.delAdminLogInsert(uuid, admin.getName(), staff.getName(), reason);
    }

    @Override
    public void systemDeleteAdminStatusLog(UUID adminID, UUID staffID, String reason) {
        Bukkit.getLogger().info("AdminsDAO | Администратор удален и отпечатан лог");
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        String uuid = UUID.randomUUID().toString();
        Player admin = Bukkit.getPlayer(adminID);

        adminDao.delAdminLogInsert(uuid, admin.getName(), "CONSOLE", reason);
    }

    @Override
    public void upAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary) {
        Bukkit.getLogger().info("AdminsDAO | Адмиминистратор был повышен!");
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.upStatus(adminID.toString(), newPrefix, newWeight, newSalary);
    }

    @Override
    public void downAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary) {
        Bukkit.getLogger().info("AdminsDAO | Администратор был понижен.");
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);

        adminDao.downStatus(adminID.toString(), newPrefix, newWeight, newSalary);
    }

    @Override
    public void giveBonusLog(UUID staffID, UUID adminID, int sum, String message) {
        Bukkit.getLogger().info("AdminsDAO | Администратор получил бонус.");
        BonusDAO bonusDAO = jdbi.onDemand(BonusDAO.class);

        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        bonusDAO.insert(UUID.randomUUID().toString(), staffID.toString(), adminID.toString(), staff.getName(), admin.getName(), sum, message);
    }
}
