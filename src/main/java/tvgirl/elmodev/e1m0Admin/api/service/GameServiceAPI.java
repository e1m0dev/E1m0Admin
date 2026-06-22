package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.Optional;
import java.util.UUID;

public interface GameService {
    void handleInvisibility(UUID id);

    void handleRewatch(UUID adm, UUID player);
    void handleReoff(UUID adm);
}
