package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface SystemService {

    void adminPay();
    void fastEmergency(UUID id, String message);

}
