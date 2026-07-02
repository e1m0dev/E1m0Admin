package tvgirl.elmodev.e1m0Admin.service.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.gui.ReportSystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportSystemService implements ReportSystemServiceAPI {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final Map<UUID, Report> playerReportCache;
    private final ReportSystemRepository reportRepository;

    public ReportSystemService(E1m0Sender sender, FileConfiguration cfg, Map<UUID, Report> playerReportCache, ReportSystemRepository reportRepository) {
        this.cfg = cfg;
        this.sender = sender;
        this.reportRepository = reportRepository;
        this.playerReportCache = playerReportCache;
    }

    public void clickToReport(UUID adminID, UUID reportID, String response) {
        Bukkit.getLogger().info("ReportController | Точка входа COMMAND-SERVICE-GUI-CONTROLLER-SERVICE: Обработка репорта ID: " + reportID); // ТЕСТЕР
        Report report = reportRepository.getReport(reportID);

        Player admin = Bukkit.getPlayer(adminID);
        Player player = Bukkit.getPlayer(report.getPlayerID());

        Report newReport = new Report(
                report.getUuid(),
                adminID,
                report.getPlayerID(),
                admin.getName(),
                player.getName(),
                report.getReport(),
                response,
                report.getStatus(),
                report.getCreatedAt()
        );

        sender.sendPath(admin, cfg.getString("Messages.reportTake")
                .replace("%content", report.getReport())
                .replace("%player", report.getPlayerNick()));

        sender.sendString(player, response);

        reportRepository.gameReportSend(newReport);
        playerReportCache.remove(report.getPlayerID());
    }
}