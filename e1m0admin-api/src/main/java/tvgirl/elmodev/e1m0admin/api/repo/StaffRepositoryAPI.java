package tvgirl.elmodev.e1m0admin.api.repo;

import java.util.UUID;

public interface StaffRepositoryAPI {
    void setAdminStatus(UUID adminID, String nick, int weight, int salary, String prefix, String IP); // | Занести в базу администратора. Почему не State Admin? Потому что по сути никто не будет создавать обьект который идет через API чтобы заполнить бланк. Администратор это не состояние а обьект, вот репорт это состояние, он временный и не совсем важный, для него выделен отдельный State в его API хоть я и не люблю так делать.

    void deleteAdminStatus(UUID id); // | Удалить администратора из базы.

    void deleteAdminStatusLog(UUID adminID, UUID staffID, String reason); // | Занести лог об увольнении администратора в базу.
    void systemDeleteAdminStatusLog(UUID adminID, UUID staffID, String reason); // | Занести лог об увольнении администратора в базу от лица системы.

    void downAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary); // | Отправить запрос на понижение администратора в базе данных.
    void upAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary); // | Отправить запрос на повышение администратора в базе данных.

    void giveBonusLog(UUID adminID, UUID staffID, int sum, String message); // | Отправить лог о выданном бонусе администратору в базу данных.

    void setAdminABan(UUID suspectID, UUID adminID); // Занести в базу информацию о предположительном сливщике админ-поста и отобрать права.

    void setAdminABanConsole(UUID adminID, UUID staffID); // Занести в базу информацию о предположительном сливщике админ-поста и отобрать права системно

    void delAdminABan(UUID adminID); // Вынести из базы подозрений по поводу слива админки.

    boolean checkAdminABan(UUID adminID); // Есть ли человек в базе подозреваемых по сливу?

    void setAdminBlockList(UUID adminID, UUID staffID, String reason); // Занести в базу информацию о пользователе в Черном Списке Администрации (ЧСА)

    void delAdminBlockList(UUID adminID); // Вынести из Черного Списка Администрации

    boolean checkAdminBlockList(UUID adminID); // Проверить, находится ли человек в Черном Списке Администрации?
}