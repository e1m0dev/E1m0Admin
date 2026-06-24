package tvgirl.elmodev.e1m0Admin.api.repo.gui;

import java.util.UUID;

public interface SecretCodeRepositoryAPI {

    /* STAFF | 🧑‍🔬 */
    void staffSetSecretCode(UUID adminID, UUID staffID, byte code);
    void staffChangeSecretCode(UUID adminID, UUID staffID, byte code);

    /* SYSTEM | 💾 */
    void systemSetSecretCode(UUID adminID, byte code);
    byte getSecretCode(UUID adminID);

}
