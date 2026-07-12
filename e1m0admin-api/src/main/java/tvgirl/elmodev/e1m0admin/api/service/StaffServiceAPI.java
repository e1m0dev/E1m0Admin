package tvgirl.elmodev.e1m0admin.api.service;

import java.util.UUID;

public interface StaffServiceAPI {
    void upStatus(UUID adminID, UUID staffID);   // Поднять админ-уровень.

    void downStatus(UUID adminID, UUID staffID); // Понизить админ-уровень.

    void setAdmin(UUID adminID, UUID staffID, int weight);  // Поставить администратора.

    void deleteAdmin(UUID adminID, UUID staffID, String reason); // Снять администратора.

    void adminBonusGive(UUID adminID, UUID staffID, int sum, String message); // Выдать админ-бонус администратору
    void adminBonusAll(UUID staffID, int sum, String message); // Выдать админ-бонус администраторам

    void setSecretPassword(UUID adminID, UUID staffID, int code); // Сменить администратору код

    void adminUnBanSystem(UUID adminID, UUID staffID); // Разморозить доступ к командам и системам администратору
}
