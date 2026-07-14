package tvgirl.elmodev.e1m0admin.api.service;

import java.util.UUID;

public interface ConsoleServiceAPI {
    void setSecretConsole(UUID adminID, UUID consoleID, int code);      // | Поставить секретный код от лица самой консоли: "Извне".

    void delAdminConsole(UUID adminID, UUID consoleID, String reason);  // | Уволить администратора от лица самой консоли: "Извне".

    void setAdminConsole(UUID adminID, UUID consoleID, int weight);     // | Поставить администратора от лица самой консоли: "Извне".

    void upAdminConsole(UUID adminID, UUID consoleID);                  // | Повысить администратора от лица самой консоли: "Извне".

    void downAdminConsole(UUID adminID, UUID consoleID);                // | Понизить администратора от лица самой консоли: "Извне".

    void giveBonusAllConsole(UUID consoleID, int sum, String message);                   // | Понизить администратора от лица самой консоли: "Извне".

    void giveBonusConsole(UUID consoleID, UUID adminID, int sum, String message);        // | Понизить администратора от лица самой консоли: "Извне".
}
