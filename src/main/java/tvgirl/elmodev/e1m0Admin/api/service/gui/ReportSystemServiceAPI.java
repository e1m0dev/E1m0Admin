package tvgirl.elmodev.e1m0Admin.api.service.gui;

import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.List;
import java.util.UUID;

public interface ReportSystemServiceAPI {

    void clickToReport(UUID adminID, UUID reportID, String response);

}
