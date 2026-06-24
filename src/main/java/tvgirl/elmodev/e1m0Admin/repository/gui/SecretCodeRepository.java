package tvgirl.elmodev.e1m0Admin.repository.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.gui.SecretCodeRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.SecretCodeDAO;

import java.util.UUID;

public class SecretCodeRepository implements SecretCodeRepositoryAPI {

    private final SecretCodeDAO secretDAO;

    public SecretCodeRepository(Jdbi jdbi) {
        this.secretDAO = jdbi.onDemand(SecretCodeDAO.class);;
    }

    @Override
    public void staffChangeSecretCode(UUID adminID, UUID staffID, byte code) {
        Player staff = Bukkit.getPlayer(staffID);

        secretDAO.staffChangeSecretCode(adminID, staff.getName(), code);
    }

    @Override
    public void staffSetSecretCode(UUID adminID, UUID staffID, byte code) {
        Player staff = Bukkit.getPlayer(staffID);
        Player adm = Bukkit.getPlayer(adminID);

        secretDAO.staffSetSecretCode(adminID, adm.getName(), staff.getName(), code, adm.getAddress().toString());
    }

    @Override
    public void systemSetSecretCode(UUID adminID, byte code) {
        Player adm = Bukkit.getPlayer(adminID);

        secretDAO.systemSetSecretCode(adminID, adm.getName(), code, adm.getAddress().toString());
    }

    @Override
    public byte getSecretCode(UUID adminID) {
        return secretDAO.getCode(adminID);
    }
}
