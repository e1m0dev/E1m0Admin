package tvgirl.elmodev.e1m0Admin.api.service;

import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.UUID;

public interface GameServiceAPI {
    void handleInvisibility(UUID adminID);

    void handleRewatch(UUID adminID, UUID playerID);

    void handleReoff(UUID adminID);

    void sendReport(Report report);
    void fastReport(UUID adminID, Report report);
    void openReportGUI(UUID adminID, String response);

    void handleAccess(UUID adminID);
}
