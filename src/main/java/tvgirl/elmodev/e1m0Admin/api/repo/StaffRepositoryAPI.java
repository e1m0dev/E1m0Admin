package tvgirl.elmodev.e1m0Admin.api.repo;

import java.util.UUID;

public interface StaffRepositoryAPI {
    void setAdminStatus(UUID adminID, String nick, int weight, int salary, String prefix, String IP); // | Занести в базу администратора. Почему не State Admin? Потому что по сути никто не будет создавать обьект который идет через API чтобы заполнить бланк. Администратор это не состояние а обьект, вот репорт это состояние, он временный и не совсем важный, для него выделен отдельный State в его API хоть я и не люблю так делать.

    void deleteAdminStatus(UUID id); // | Удалить администратора из базы.

    void systemDeleteAdminStatusLog(UUID adminID, UUID staffID, String reason); // | Занести лог об увольнении администратора в базу от лица системы.

    void deleteAdminStatusLog(UUID adminID, UUID staffID, String reason); // | Занести лог об увольнении администратора в базу.

    void downAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary); // | Отправить запрос на понижение администратора в базе данных.

    void upAdminStatus(UUID adminID, String newPrefix, int newWeight, int newSalary); // | Отправить запрос на повышение администратора в базе данных.

    void giveBonusLog(UUID adminID, UUID staffID, int sum, String message); // | Отправить лог о выданном бонусе администратору в базу данных.
}