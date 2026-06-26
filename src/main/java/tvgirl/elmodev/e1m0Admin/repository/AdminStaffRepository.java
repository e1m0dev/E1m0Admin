package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.StaffRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.AdminsDAO;
import tvgirl.elmodev.e1m0Admin.dao.BonusDAO;
import tvgirl.elmodev.e1m0Admin.dao.ReportDAO;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public class AdminStaffRepository implements StaffRepositoryAPI {

    private final Jdbi jdbi;

    public AdminStaffRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void setAdminStatus(UUID id, String nick, int weight, int salary, String prefix, String IP) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        adminDao.insert(id, nick, weight, salary, prefix, IP);
    }

    @Override
    public void deleteAdminStatus(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        adminDao.delAdmin(adminID);
    }

    @Override
    public void deleteAdminStatusLog(UUID staffID, UUID adminID, String reason) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        adminDao.delAdminLog(staffID, adminID, reason);
    }

    @Override
    public void upAdminStatus(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID);
        adminDao.upStatus(admin.uuid(), admin.weight(), admin.salary(), admin.prefix());
    }

    @Override
    public void downAdminStatus(UUID adminID) {
        AdminsDAO adminDao = jdbi.onDemand(AdminsDAO.class);
        Admin admin = adminDao.findByUuid(adminID);
        adminDao.downStatus(admin.uuid(), admin.weight(), admin.salary(), admin.prefix());
    }

    @Override
    public void giveBonusLog(UUID staffID, UUID adminID, int sum, String message) {
        BonusDAO bonusDAO = jdbi.onDemand(BonusDAO.class);

        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        bonusDAO.insert(UUID.randomUUID(), staffID, adminID, staff.getName(), admin.getName(), sum, message);
    }
}
