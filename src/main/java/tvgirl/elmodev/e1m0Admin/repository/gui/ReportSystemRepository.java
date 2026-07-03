package tvgirl.elmodev.e1m0Admin.repository.gui;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.gui.ReportSystemRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.ReportDAO;
import tvgirl.elmodev.e1m0Admin.state.report.Report;

public class ReportSystemRepository implements ReportSystemRepositoryAPI {

    private final ReportDAO reportDAO;

    public ReportSystemRepository(Jdbi jdbi) {
        this.reportDAO = jdbi.onDemand(ReportDAO.class);
    }

    @Override
    public void gameReportSend(Report report) {
        reportDAO.sendReport(report.getUuid().toString(), report.getAdminID().toString(), report.getPlayerID().toString(), report.getAdminNick(), report.getPlayerNick(), report.getReport(), report.getResponse(), report.getStatus());
    }
}
