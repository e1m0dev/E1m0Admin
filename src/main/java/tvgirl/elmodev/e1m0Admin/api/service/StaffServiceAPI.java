package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface StaffServiceAPI {
    void upStatus(UUID staffID, UUID adminID);   // Поднять админ-уровень.
    void downStatus(UUID staffID, UUID adminID); // Понизить админ-уровень.

    void setAdmin(UUID staffID, UUID adminID, int weight);  // Поставить администратора.

    void adminBonusGive(UUID staffID, UUID id, int sum, String message); // Выдать админ-бонус администратору
    void adminBonusAll(UUID staffID, int sum, String message); // Выдать админ-бонус администраторам

    void changeSecretPassword(UUID adminID, UUID staffID, byte code);
}
