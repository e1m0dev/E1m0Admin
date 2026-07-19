package tvgirl.elmodev.e1m0admin.api.service;

import tvgirl.elmodev.e1m0admin.api.state.report.Report;

import java.util.UUID;

public interface GameServiceAPI {
    void handleInvisibility(UUID adminID); // | Инвиз

    void handleRewatch(UUID adminID, UUID playerID); // | Реватч

    void handleReoff(UUID adminID); // | Отключится от реватча

    void sendReport(Report report); // | Отправить репорт

    void fastReport(UUID adminID, Report report); // Быстро закрыть репорт

    void adminBlockAccess(UUID targetID, UUID adminID); // Защита администрации и игроков от слива другого администратора, проще говоря - блок доступа.

    void adminHelp(UUID adminID); // Помощь по командам для администратора в чате.

    // PLAYER LAYER
    void getAdminList(UUID playerID); // Здесь есть возможность получить список активных администраторов для игроков и админов по этому метод находится в adminGameService но под грифом Player Layer

    void addCompliment(UUID playerID, UUID adminID); // Возможность добавлять администратору очки помощи от игрока.
}
