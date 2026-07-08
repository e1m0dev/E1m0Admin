package tvgirl.elmodev.e1m0admin.api.service;

import tvgirl.elmodev.e1m0admin.api.state.report.Report;

import java.util.UUID;

public interface GameServiceAPI {
    void handleInvisibility(UUID adminID); // | Инвиз

    void handleRewatch(UUID adminID, UUID playerID); // | Реватч

    void handleReoff(UUID adminID); // | Отключится от реватча

    void sendReport(Report report); // | Отправить репорт

    void fastReport(UUID adminID, Report report); // Быстро закрыть репорт
}
