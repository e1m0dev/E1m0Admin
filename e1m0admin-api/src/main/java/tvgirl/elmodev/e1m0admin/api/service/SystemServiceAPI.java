package tvgirl.elmodev.e1m0admin.api.service;

import java.util.UUID;

public interface SystemServiceAPI {
    void adminPay();

    // 🧑‍💻 | Admins System

    void autoLeakActions(UUID adminID, UUID staffID); // Запуск действий при сливе адм из конфига.

    void autoComplimentActions(UUID adminID); // Запуск действий из конфига при похвале администратора.

    void autoSetAdmin(UUID adminID, UUID staffID, int weight); // Выдает все из конфига системно когда система осознает что появился факт постановления админа.

    void autoDelAdmin(UUID adminID, UUID staffID); // Забирает все из конфига системно когда система осознает что появился факт снятия админа.

    // 🌐 | Controllers
    void handleReportAccept(UUID adminID, UUID reportID); // | Контроллер который системно принимает в общем запросы самого плагина по этому и System.
}