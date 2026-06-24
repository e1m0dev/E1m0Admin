package tvgirl.elmodev.e1m0Admin.api.service;

import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.UUID;

public interface GameServiceAPI {
    void handleInvisibility(UUID id);

    void handleRewatch(UUID adm, UUID player);
    void handleReoff(UUID adm);

    void sendReport(Report report);
    void fastReport(UUID adminID, Report report);
    void openReportGUI(UUID adminID, String response);

    void handleAccess(UUID id);
}
