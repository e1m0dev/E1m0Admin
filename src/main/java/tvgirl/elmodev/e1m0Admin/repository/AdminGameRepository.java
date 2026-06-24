package tvgirl.elmodev.e1m0Admin.repository;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.GameRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.ReportDAO;

import java.util.UUID;

public class AdminGameRepository implements GameRepositoryAPI {

    private final Jdbi jdbi;

    public AdminGameRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void gameReport(UUID playerID, UUID adminID, String request, String response, String status) {
        ReportDAO reportDAO = jdbi.onDemand(ReportDAO.class);

        Player admin = Bukkit.getPlayer(adminID);
        Player player = Bukkit.getPlayer(playerID);

        reportDAO.sendReport(UUID.randomUUID(), adminID, playerID, admin.getName(), player.getName(), request, response, status);
    }
}
