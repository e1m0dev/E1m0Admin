package tvgirl.elmodev.e1m0admin.api.repo.gui;

import java.util.UUID;

public interface SecretCodeRepositoryAPI {

    /* STAFF | 🧑‍🔬 */
    void staffSetSecretCode(UUID adminID, UUID staffID, int code); // | Устанавливает SecretCode от определенного администратора.

    /* SYSTEM | 💾 */
    void systemSetSecretCode(UUID adminID, int code); // | Устанавливает SecretCode от КОНСОЛИ/СИСТЕМЫ, а не от администратора.

    void systemDeleteAdmin(UUID adminID); // | Удаляет секретный код администратора, обычно вместе с администратором на момент Commit 1.10 -> 1.0-BETA Version

    boolean checkSecretCode(UUID adminID); // | Просто получает ответ есть ли Secret из Базы Данных по UUID администратора.

    int getSecretCode(UUID adminID); // | Просто получает SecretCode из Базы Данных по UUID администратора.
}
