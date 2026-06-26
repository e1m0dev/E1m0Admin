Commit Version 1.1-DEV | PIN-Generation

Добавлена система PIN-кодов
Добавлены возможности для STAFF по поритету PIN

Добавлены огромные кол-ва точек входа, команд, работающих с SecretCode
Добавлена GUI работа с SecretCode и проверки доступа у администратора.

Переделана система приоритетов и permission связанная с PIN-Code
Добавлены новые State and StateManager для работы с Permission Manager
Переписана API PermissionManager под новый State

Commit Version 1.2-DEV | Permission Review
🧑‍💻 E1m0 | И так, прошлое решение с командами было большой ошибкой и overengineering, по этому делаю новую систему:

Commands:
—— 💚 Admin && Staff | Изменены все команды на проверку на SecretCode;
—— 💚 Admin && Staff | Добавлено немного ясности, переработана структура;

Service:
💚 SecretCodeService | Опять же немного ясности + имплементация SecretCodeManager;
💛 SecretCodeService | Исправлена ошибки CFG, отправлялись не те сообщения!
🦖 SecretCodeService | Добавлены точки входа ❗

Events:
❗ Раскидал по папкам:
💾 Bukkit: Система Minecraft.
🧑‍💻 E1m0: Система плагина.

    🧑‍💻 E1m0

💚 AdminAccessEvent | Добавлен новый факт системы, позволяет отслеживать сам факт когда администратор прошел
регистрацию + подтверждение админ кода.

    💾 Bukkit

💚 OnQuitEvent | Имплементация SecretCodeManager;
💛 OnQuitEvent | Упрощение и мелкая переработка структуры;

👂 Listener:
💚 AdminAccessListener | Добавлен обработчик к AdminAccessEvent. Теперь можно не засорять проверяющий service действиями,
а делегировать обязанности на другие.

Permissions:
💛 PermissionsManagerAPI был переработан, убран addAccess;
💛 Переработана структура через State + SecretCodeManager;

State:
❤️ Удален CommandState && CommandStateManager | Причина: Архитектурная ошибка | Overengineering;
💚 Добавлен новый отдел SecretCodeManager для управление кэшем на замену старым решениям;
💚 Переработан SecretCodeState, немного ясности;

CFG:
💚 Новые сообщения;
❤️ Убрана система добавления команд;
💛 Немного переработки структуры сообщений;

Commit 1.3-DEV | Build, Packed, Tested:
🧑‍💻 | E1m0: Подвожу итог разработки версии 1.0, коммиты идут, но версия то никуда не меняется, по этому -- сейчас
собираю все это и запускаю, я думаю исправлять буду раз 30, плагин большой, так что работы у меня много
Admin && Staff:
Controllers;
Repository;
Listener;
Commands;
Events:

        💚 Были добавлены полные тестеры, с спец-пометкой и полной архитектурной цепью. Пример:
        E1m0/AdminAccessListener 30с: "AdminAccessListener | Точка входа COMMAND-SERVICE-GUI-CONTROLLER-SERVICE-EVENT-LISTENER: Администратор зарегистрировался."

Commands:
❗ system/ | Нововведение в папку commands. Специально была сделана для системных команд, API не имеет - нет надобности.
Тут не команды, а системные обработчики-контроллеры, очень удобная штука.
💚 system/AdminReportAccept | Обработчик системы Emergency Report из чата, открыл звено.

💚 AdminSetSecretCode | Теперь заместо себя + выполняет функции AdminChangeSecretCode.
💚 AdminDeleteCommand | Добавлена функция удаления администратора с функцией логирования состояния.

💛 AdminBonusAllCommand | Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Переименован в /abonusall 777
💛 AdminBonusCommand | Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Переименован в /abonus E1m0 777

💛 AdminDownCommand | Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Точка входа переименована: /adown
admin
💛 AdminUpCommand | Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Точка входа переименована: /aup admin

💛 AdminSetCommand | Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Точка входа переименована: /aset admin
weight

💛 ReportCommand | 🔄 Исправлена ошибка проверки прав, добавлена адаптивность | 🔄️ Точка входа переименована: /arep
message
💛 InvisibilityCommand | 🔄️ Точка входа переименована: /ainv
💛 RewatchCommand | 🔄️ Точка входа переименована: /arec

💛 ReportCommand -> PlayerReportCommand | Причина: Не то что архитектурная ошибка, скорее больше упрощение, вместо разных
папок теперь просто нейм.

❤️ AdminChangeSecretCode | Удален. Причина: Зачем делать два метода делающие одно и то же если можно сделать одиню.

CFG:
❤️ Вырезан старый функционал авто-выдачи permissions из вкладки управления сервером.

DAO:
💚 AdminDAO | Был дополнен новыми методами: deleteAdminStatus: Удаление из базы && deleteAdminStatusLog: Внесение в
логирование учета снятых администраторов.

Permissions:
💛 E1m0PermissionManager | Убрано лишнее TODO, статус: Выполнен, проверен.

Database:
💚 Новая таблица логирования: e1admin_deletedAdminsLogs - Отвечает за логирование снятых администраторов.

Service:
💚 AdminStaffService | Добавлен обработчик deleteAdmin, логирование + удаление. Позже при надобности будет добавлен
системный факт + действия.
💚 AdminSystemService | handleReportAccept - Был добавлен и теперь появилась связующая цепь через Emergency Report.
💛ReportSystemService | clickToReport - Был переработан, исправлен основной баг теперь полноценная связующая цепь.

❤️ AdminSystemService/fastEmergency | Удален метод fastEmergency. Причина: Заброшен, больше не нужен, нашлось решение
лучше. fastReport(adm.getUniqueId(), report); в AdminGameService.

Holder:
💚 ReportHolder | Добавлены геттеры, думал обойтись без них но не получилось.

TabCompleter:
💚 MainTabCompleter | Был полностью дополнен и добит командами, точками входа.

API:
💚 StaffServiceAPI | Добавлен метод удаления администратора с полным логирование | deleteAdmin.
💚 StaffRepositoryAPI | Добавлены методы такие как deleteAdminStatus: Удаление из базы && deleteAdminStatusLog: Внесение
в логирование учета снятых администраторов.

💛 GameServiceAPI | Исправление переменных, улучшение читаемости, начинаются правки по читаемости, а не по логике.
Изначально - Логика + Архитектура, второстепенно - Читаемость.
💛 StaffRepositoryAPI | Переименовал giveBonus в giveBonusLog. Причина: Архитектурный метод может запутать обладателя API
и вместо getAdminBonus будет setAdminBonus. По этому я явно показываю что это факт системы как Kafka.

E1m0/Main:
💚 E1m0Admin | ПОЛНОСТЬЮ архитектурно связан по предварительному чек листу выхода на тестовый стенд: FIRST PICKUP START
PACKAGE CHECKLIST.
❤️ E1m0Admin | Убран кэш этапов SecretCode. Причина: Архитектурно замещен и переработан: SecretCodeManager +
SecretCodeState.

plugin:
💚 plugin.yml | Добавлена поддержка команд и их регистрация на сервере на котором установлен плагин.

2.0 Admin Optional && Player Structure
