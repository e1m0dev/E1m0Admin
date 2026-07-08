package tvgirl.elmodev.e1m0Admin.repository.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0admin.api.repo.gui.SecretCodeRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.SecretCodeDAO;

import java.util.UUID;

public class SecretCodeRepository implements SecretCodeRepositoryAPI {

    private final SecretCodeDAO secretDAO;

    public SecretCodeRepository(Jdbi jdbi) {
        this.secretDAO = jdbi.onDemand(SecretCodeDAO.class);;
    }

    @Override
    public void staffSetSecretCode(UUID adminID, UUID staffID, int code) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        secretDAO.staffSetSecretCode(adminID.toString(), admin.getName(), staff.getName(), code, admin.getAddress().toString());
    }

    @Override
    public void systemSetSecretCode(UUID adminID, int code) {
        Player adm = Bukkit.getPlayer(adminID);
        secretDAO.systemSetSecretCode(adminID.toString(), adm.getName(), code, adm.getAddress().toString());
    }

    @Override
    public void systemDeleteAdmin(UUID adminID) {
        Player adm = Bukkit.getPlayer(adminID);
        secretDAO.delAdminSecret(adminID.toString());
    }

    @Override
    public int getSecretCode(UUID adminID) {
        return secretDAO.getCode(adminID.toString());
    }
}
