package tvgirl.elmodev.e1m0Admin.api.service;

import java.util.UUID;

public interface StaffService {
    void upStatus(UUID id);   // Поднять админ-уровень.
    void downStatus(UUID id); // Понизить админ-уровень.

    void setAdmin(UUID id, byte num);  // Поставить администратора.

    void adminBonus(UUID id, int sum, String message); // Выдать админ-бонус администратору(ам)
}
