package tvgirl.elmodev.e1m0Admin.api.gui.service;

import java.util.UUID;

public interface ReportSystemServiceAPI {

    void openReportMenu(UUID admin);
    void clickToReport(UUID reportID, UUID adminID, UUID playerID, String reportMessage, String responseMessage);

}
