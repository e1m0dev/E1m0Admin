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
    private final Map<UUID, UUID> reportPlayers;
    private final ReportSystemRepository reportRepository;

    public ReportSystemService(E1m0Sender sender, FileConfiguration cfg, ReportSystemRepository reportRepository, Map<UUID, UUID> reportPlayers) {
        this.cfg = cfg;
        this.sender = sender;
        this.reportRepository = reportRepository;
        this.reportPlayers = reportPlayers;
    }

    @Override
    public List<Report> getReports() {
        String status = cfg.getString("Admin.Report.status_inJob");
        int limit = cfg.getInt("Admin.Report.reportInDataLimit");

        return reportRepository.getReportList(status, limit);
    }

    public void clickToReport(UUID reportID) {
        Report report = reportRepository.getReport(reportID);

        Player adm = Bukkit.getPlayer(report.getAdminID());
        Player player = Bukkit.getPlayer(report.getPlayerID());

        // TODO: Добавить кнопку инвиза + телепортации в ховер?
        sender.sendPath(adm, cfg.getString("Messages.reportTake")
                .replace("%content", report.getReport())
                .replace("%player", report.getPlayerNick()));

        reportRepository.updateReport(report);
        reportPlayers.remove(report.getPlayerID());
    }
}