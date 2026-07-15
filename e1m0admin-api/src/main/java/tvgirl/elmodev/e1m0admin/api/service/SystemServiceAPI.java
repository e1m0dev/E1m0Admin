package tvgirl.elmodev.e1m0admin.api.service;

import java.util.UUID;

public interface SystemServiceAPI {
    void adminPay();

    // 🧑‍💻 | Admins System
    void autoLeakActions(UUID adminID, UUID staffID);

    void autoDelAdmin(UUID adminID, UUID staffID);

    void autoSetAdmin(UUID adminID, UUID staffID, int weight);

    // 🌐 | Controllers
    void handleReportAccept(UUID adminID, UUID reportID); // | Контроллер который системно принимает в общем запросы самого плагина по этому и System.
}