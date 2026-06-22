package tvgirl.elmodev.e1m0Admin.api.gui.repository;

import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.List;
import java.util.UUID;

public interface ReportSystemRepositoryAPI {
    List<Report> getReportList(String status, int limit);

    void updateReport(Report report);

    Report getReport(UUID id);
}
