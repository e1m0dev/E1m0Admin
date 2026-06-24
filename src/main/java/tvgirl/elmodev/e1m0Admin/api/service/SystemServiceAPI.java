package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface SystemServiceAPI {

    void adminPay();
    void fastEmergency(UUID id, String message);

}
