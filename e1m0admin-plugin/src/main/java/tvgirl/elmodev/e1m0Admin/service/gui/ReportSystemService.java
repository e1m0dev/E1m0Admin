package tvgirl.elmodev.e1m0Admin.service.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0admin.api.service.gui.ReportSystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.state.report.Report;

import java.util.Map;
import java.util.UUID;

public class ReportSystemService implements ReportSystemServiceAPI {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final Map<UUID, Report> playerReportCache;
    private final AdminStaffRepository staffRepository;
    private final ReportSystemRepository reportRepository;

    public ReportSystemService(E1m0Sender sender, FileConfiguration cfg, Map<UUID, Report> playerReportCache, AdminStaffRepository staffRepository, ReportSystemRepository reportRepository) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffRepository = staffRepository;
        this.reportRepository = reportRepository;
        this.playerReportCache = playerReportCache;
    }

    public void clickToReport(UUID adminID, UUID reportID, String response) {

        Player admin = Bukkit.getPlayer(adminID);
        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (isBlockedAdmin) {
            sender.sendPath(admin, "Messages.Errors.youAdminAccessIsBlocked");
            admin.closeInventory();
            return;
        }

        for (Map.Entry<UUID, Report> reportKey : playerReportCache.entrySet()) {
            boolean f = reportID.equals(reportKey);

            Bukkit.getLogger().warning("Boolean: " + f);

            if (reportKey.getValue().getUuid().equals(reportID)) {

                Report report = reportKey.getValue();
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

                sender.sendPath(admin, "Messages.reportTake",
                        "%content", report.getReport(),
                        "%player", report.getPlayerNick());

                sender.sendPath(player, "Messages.reportAdminResponse",
                        "%admin", admin.getName(),
                        "%response", response);


                admin.closeInventory();
                reportRepository.gameReportSend(newReport);
                playerReportCache.remove(report.getPlayerID());
            } else {
                Bukkit.getLogger().warning("Репорт: " + reportID + " НЕ ДЕЙСТВИТЕЛЕН, ОПАСНАЯ НЕ ОПРЕДЕЛЕННОСТЬ!");
                admin.closeInventory();

                for (Map.Entry<UUID, Report> reportKeyOff : playerReportCache.entrySet()) {
                    if (reportKey.getValue().getUuid() == reportID) {
                        Report report = reportKey.getValue();
                        playerReportCache.remove(report.getPlayerID());
                    }
                }
            }
        }
    }
}