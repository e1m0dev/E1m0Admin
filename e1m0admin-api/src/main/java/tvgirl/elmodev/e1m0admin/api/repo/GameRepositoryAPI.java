package tvgirl.elmodev.e1m0admin.api.repo;

import java.util.UUID;

public interface GameRepositoryAPI {
    void addCompliment(UUID adminID); // | GAME: Отправить похвалу для администратора.

    int getCompliments(UUID adminID); // | GAME: Сколько похвал у администратора?
}
