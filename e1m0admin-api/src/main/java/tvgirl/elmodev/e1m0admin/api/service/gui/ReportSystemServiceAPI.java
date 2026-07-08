package tvgirl.elmodev.e1m0admin.api.service.gui;

import java.util.UUID;

public interface ReportSystemServiceAPI {

    void clickToReport(UUID adminID, UUID reportID, String response); // | Обработка нажатия по репорту, вызов всех проверок и заполнение в базу.

}
