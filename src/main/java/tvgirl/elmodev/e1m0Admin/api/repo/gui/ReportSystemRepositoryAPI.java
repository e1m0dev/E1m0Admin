package tvgirl.elmodev.e1m0Admin.api.repo.gui;

import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.List;
import java.util.UUID;

public interface ReportSystemRepositoryAPI {
    void gameReportSend(Report report); // Ведение диалога с базой и отправка самого репорта в Базу Данных.
}
