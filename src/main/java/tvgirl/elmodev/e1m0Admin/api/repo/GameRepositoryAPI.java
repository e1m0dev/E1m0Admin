package tvgirl.elmodev.e1m0Admin.api.repo;

import java.sql.Timestamp;
import java.util.UUID;

public interface GameServiceRepo {
    void sendReport(UUID player, String request, Timestamp time);

    void recipientReport(UUID player, UUID admin, String request, String response, Timestamp time);
}
