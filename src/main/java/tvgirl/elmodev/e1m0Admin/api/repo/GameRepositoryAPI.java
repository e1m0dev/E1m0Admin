package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface GameRepositoryAPI {
    void gameReport(UUID playerID, UUID adminID, String request, String response, String status);
}
