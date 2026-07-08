package tvgirl.elmodev.e1m0admin.api.repo.gui;

import tvgirl.elmodev.e1m0admin.api.state.report.Report;

public interface ReportSystemRepositoryAPI {
    void gameReportSend(Report report); // Ведение диалога с базой и отправка самого репорта в Базу Данных.
}
