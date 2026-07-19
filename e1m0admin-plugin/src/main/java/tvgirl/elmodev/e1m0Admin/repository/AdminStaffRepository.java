package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.dao.BlockDAO;
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

    @Override
    public void setAdminABan(UUID suspectID, UUID adminID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);

        String transactionID = UUID.randomUUID().toString();
        Player suspect = Bukkit.getPlayer(suspectID);
        Player admin = Bukkit.getPlayer(adminID);

        blockDAO.insertToABan(transactionID, adminID.toString(), suspectID.toString(), admin.getName(), suspect.getName(), admin.getAddress().toString());
    }

    @Override
    public void setAdminABanConsole(UUID adminID, UUID staffID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);

        String transactionID = UUID.randomUUID().toString();
        Player suspect = Bukkit.getPlayer(adminID);

        blockDAO.insertToABan(transactionID, staffID.toString(), adminID.toString(), "CONSOLE", suspect.getName(), suspect.getAddress().toString());
    }

    @Override
    public void delAdminABan(UUID adminID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);
        blockDAO.delAdminABan(adminID.toString());
    }

    @Override
    public boolean checkAdminABan(UUID suspectID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);
        return blockDAO.checkInABan(suspectID.toString()) != null;
    }

    @Override
    public void setAdminBlackList(UUID adminID, UUID staffID, String reason) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);

        String transactionID = UUID.randomUUID().toString();
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        blockDAO.insertToBlackList(transactionID, adminID.toString(), staffID.toString(), admin.getName(), staff.getName(), reason, admin.getAddress().toString());
    }

    @Override
    public void delAdminBlackList(UUID adminID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);
        blockDAO.delAdminBlockList(adminID.toString());
    }

    @Override
    public boolean checkAdminBlackList(UUID adminID) {
        BlockDAO blockDAO = jdbi.onDemand(BlockDAO.class);
        return blockDAO.checkInBlockList(adminID.toString()) != null;
    }
}
