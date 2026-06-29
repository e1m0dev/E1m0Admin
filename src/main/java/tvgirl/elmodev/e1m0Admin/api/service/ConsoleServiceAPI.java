package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface ConsoleServiceAPI {
    void setSecretConsole(UUID adminID, UUID consoleID, int code);

    void delAdminConsole(UUID adminID, UUID consoleID, String reason);
    void setAdminConsole(UUID adminID, UUID consoleID, int weight);

    void upAdminConsole(UUID adminID, UUID consoleID);
    void downAdminConsole(UUID adminID, UUID consoleID);
}
