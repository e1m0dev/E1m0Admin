package tvgirl.elmodev.e1m0Admin.repository.gui;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.gui.ReportSystemRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.dao.ReportDAO;
import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.List;
import java.util.UUID;

public class ReportSystemRepository implements ReportSystemRepositoryAPI {

    private final ReportDAO reportDAO;

    public ReportSystemRepository(Jdbi jdbi) {
        this.reportDAO = jdbi.onDemand(ReportDAO.class);
    }

    @Override
    public List<Report> getReportList(String status, int limit) {
        return reportDAO.getReportList(status, limit);
    }

    @Override
    public Report getReport(UUID id) {
        return reportDAO.getReport(id);
    }

    @Override
    public void updateReport(Report report) {
        reportDAO.updateReport(report.getUuid(), report.getAdminID(), report.getAdminNick(), report.getResponse(), report.getStatus());
    }
}
