package tvgirl.elmodev.e1m0admin.api.service.gui;

import java.util.UUID;

public interface SecretCodeServiceAPI {
    boolean checkSecret(UUID id);  // | Есть ли вообще код у администратора?

    void oneStepHandler(UUID id, int i);  // | Вызов второго этапа GUI. Почему именно так а не главное меню? Потому что иногда надо начинать с кода чтобы сразу был обработчик события, а для обычного открытия есть просто ReportGuiAPI/openReportGUI.

    String getInputCode(UUID id); // | Отвечает на вопрос: Какой код установлен у администратора?
}
