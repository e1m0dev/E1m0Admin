package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface SystemServiceAPI {
    void adminPay();

    // 🌐 | Controllers
    void handleReportAccept(UUID adminID, UUID reportID); // | Контроллер который системно принимает в общем запросы самого плагина по этому и System.
}