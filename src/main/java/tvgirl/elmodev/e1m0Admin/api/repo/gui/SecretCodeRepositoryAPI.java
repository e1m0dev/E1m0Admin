package tvgirl.elmodev.e1m0Admin.api.repo.gui;

import java.util.UUID;

public interface SecretCodeRepositoryAPI {

    /* STAFF | 🧑‍🔬 */
    void staffSetSecretCode(UUID adminID, UUID staffID, int code);

    /* SYSTEM | 💾 */
    void systemSetSecretCode(UUID adminID, int code);
    byte getSecretCode(UUID adminID);

}
