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

❤️ AdminChangeSecretCode || StaffChangeSecretCode | Удален. Причина: Зачем делать два метода делающие одно и то же если
можно сделать одиню.

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

🧡ReportSystemService | clickToReport - Был переработан, исправлен основной баг теперь полноценная связующая цепь.

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

Commit 1.4-DEV | fit: Commit 1.4. First startup, fix bugs in boot/host/server/package and rework Secret-Code system to
int:

CFG:
💚 config.yml | Добавлены подсказки, описания, использования и шаблоны в главный конфиг.

🧡 config.yml | Исправлен баг конфига в базе

💛 config.yml | База перекинута на самый верх в конфиге

Database:
💚 DatabaseSource | База под клиента с данными была откорректирована, подключены новые модули, указатели. Позже: Полная
система выборки драйвера с улучшениями
💛 DatabaseManager | Откорректирована так же модуляция и синтаксис таблиц в базе данных, да, даже лишние запятые :3

DAO:
💚 SecretCodeDAO | Перевод на INT систему
💛 ReportDAO | Фикс и добавление забытого uuid в методе updateReport (35).
💛 ReportDAO | Починил ReportDAO, убрал все лишнее и сделал правильное использование.
💛 AdminsDAO | Починил AdminsDAO, починил все лишнее и сделал правильное использование.

SecretCodeSystem:
❤️ Система Byte code HashMap - была удалена. Причина: Архитектурная замена.
💚 SecretCode переходит на INT, переписана API и система, раньше до коммита 1.1 архитектура была другой, связанной с
HashMap, но потом было принято решение о переработке

Plugin:
💚 ./DEVLOG.MD | Определены цели на после будущие действия

Commands:
💚 SecretCodeCommand | Переведена на систему INT.

State:
💚 SecretCodeState | Переведена на систему INT.

Service
💚 SecretCodeService | Переведен на систему INT.

API:
💚 SecretCodeRepositoryAPI | Переведен на систему INT.
💚 SecretCodeServiceAPI | Переведен на систему INT.
💚 StaffServiceAPI | Метод changeSecretPassword (16): Переведен на систему INT.

E1m0/Main:
💛 Откорректирована архитектура зависимостей, композиции и архитектурных цепей. Да - не так красиво как раньше, но
работает.

Контроль качества:
❗ Выполнен чек лист E1m0: FIRST PICKUP START PACKAGE CHECKLIST. | Прошел первый чек лист, был сгенерирован, запущен.

Commit 1.5-DEV | feat: Console/RCON architecture + NEW Console EcoSystem

Commands
❗ ALL | У всех команд пришлось поднять лог, тк.к наш лучший разработчик забыл что Java является не асинхронным языком и
очень не хотел признавать что теперь у нас факт-действие, а не действие-факт, а тестры уплотнять в два раза тоже не
хочется.
❗ /console - Специальные команды, которые при настройке выполняются удаленно терминалом, а не игроком!
💚 | ConsoleSetAdminSecretCommand - Чтобы

TabCompliter
💛 MainTabCompliter | Все же решил оставить старый вариант и понял как переработать лучше при тестах.

API:
❤️ SecretCodeRepositoryAPI | Полностью убрана поддержка staffChangeSecretCode. Причина: Коммит 1.2, все таки принято
решение даже из RepoAPI вырезать полностью.
💚 ConsoleServiceAPI | Новая ветка! ConsoleAPI позволяющая разделять ответственность, поможет в будущем при создании
React Web Client на сервере :3

Repository
💛 SecretCodeRepository | Переработаны моменты смены staffChangeSecretCode на staffSetSecretCode

DAO:
❤️ SecretCodeDAO | Полностью убрана поддержка staffChangeSecretCode. Причина: Коммит 1.2, все таки принято решение даже
из RepoAPI вырезать полностью.

Service:
💚 ConsoleService - Создан новый обработчик команд для консоли!

Plugin:
💚 ./DEVLOG.MD | Новые записи
💚 plugin.yml | Добавлены новые команды.

💚 plugin.yml | Исправлены ошибки usage в этих же новых командах :3
💚 plugin.yml | Добавлены новые команды связанные с консолью и удаленкой.

E1m0/Main:
💚 E1m0 | Добавлена новая поддержка в регистрации команд и TABComp.

🧑‍💻 E1m0: ❗ "И так, если возникнет вопрос, почему именно нужно передавать consoleID, а не сделать его константой в
сервисе? Если мы планируем расширятся, ну как, я планирую - "
" значит нужно думать заранее, по этому я создал отдельную касту системных не игровых команд которыми можно управлять "
Из далека" по этому UUID можно вставить - любой."
"Если я вижу все семерки: Сразу понимаю - Это запрос терминала, если например все однерки - Это запрос от RCON значит
был сделан с веб-сервиса/приложения."
"Это может помочь понять откуда все же был слив или атака, с консоли или с сайта, и что нужно чинить в первую очередь.
За тем - может появиться какой-либо"
" ресурс для модераторов сервера, там например все двойки, ну думаю мысль понятна. Работы в два раза больше для меня -
за то проще клиентам,"
" большая возможность расширяемости, сплетенная API экосистема которую я и так удешевил с репо сделав дополнительные
функции :3"

Решенные задачи:
💜 Нашел затык при правах с * или op, у меня на сервере была опка, и как раз из-за нее выскакивал ивент у администраторов
которых не существовало.
💜 Понял что через базу данных администраторов ставить вряд ли будут, это слишком тяжело для клиента, по этому сделал
команду для удаленного доступа, если сделать через API сайта будет вообще шедевр.
💜 Понял что делать буквенный UUID не лучшая идея, исправил данный баг с заделом на будущее API и легкость
использования. | csetadmin
💜 Пролема в затыке в consoleSetSecretCommand - Поменял сигнатуру входных параметров, теперь: A) Над кем идет действие,
B) Допы. Да банально, но я уже забыл об этом в Front-End

PICK-CHECK COMMANDS:
ADMIN:

STAFF:

CONSOLE:
$csetadmin - Отработала отлично, работает прекрасно. | Прошла тестеры: ➕
$csetsecret - Отработала отлично, работает прекрасно. | Прошла тестеры: ➕

Commit 1.6-DEV: E1m0Sender all fix && Tests commands, fix API, fix commands, and review commands

API:
💚 SystemRepositoryAPI | Новый метод checkAdminInBase который позволяет удостовериться и ответить на вопрос: Eсть ли
администратор в базе?
💚 SenderAPI | Добавлена возможность добавлять Replacement без надобности переписывать структуру..

💛 SystemRepositoryAPI | Замена мелочи для читаемости: id -> adminID. То есть уточнение что это ИМЕННО админ, потому что
getADMIN... | Добавление защиты от NullPointerException 🧑‍💻

Sender:
💚 E1m0Sender | Была проведена работа по Replacement, аннотациями и вставками, да, я знал что так с ключами и получится
но решил проверить и забыл что не нужно пускать все. Причина; Ключи - не передают данные, не забывать ❗
💚 Service, Repo, Managers, Commanmds | И везде где был replace + E1m0Sender - Были успешно заменены

Commands:
🧡 ConsoleDelAdminCommand | Исправлен баг с проверкой аргументов.
🧡 ConsoleSetAdminCommand | Исправлен баг с проверкой аргументов.
🧡 ConsoleSetSecretCommand | Исправлен баг с проверкой аргументов.

Service:

💚 ConsoleService && AdminStaffService | Защита от вставки уже существующего админа - добавлена в setAdminConsole через
checkAdminInBase()
💚 ConsoleService && AdminStaffService | В связи с последними работами над NullPointerException. Добавлены защиты от базы
❗

🧡 ConsoleService && StaffService | Исправлен баг проверки в методе downAdminConsole между cfgWeight и targetWeight,
кажется я либо забыл либо забил.. Это странно, уже третий баг именно в down а не в UP, хотя up я писал первее..
🧡 ConsoleService && AdminStaffService | Исправлена одна и та же логическая ошибка в методе понижения администрации, у
кого downStatus, у кого consoleDownAdmin, короче в двух исправлен баг.
🧡 ConsoleService | Исправлен интересный баг опечатка, что в consoleUp - был downAdminStatus

💛 ConsoleService && AdminStaffService | Переписаны проверки на новый лад в downStatus && upStatus, выходит дороже - но
можно парировать это асинхронностью в 2.0

💛 AdminSystemService | Убрал легаси adm.sendMessage от Bukkit, закралась мысли что где то - Есть еще не пропатченные
sender'ом сообщения, стоит проверить.
💛 AdminStaffService | Так и знал, все таки остались куски легаси при быстром написании, надо быть внимательнее и ловить
их. Так как я думаю над логикой в первую очередь - могу упускать.

DAO && DATABASE:
💚 DAO && DATABASE | Переход с UUID - StringUUID. Причина: Так как я пользовался postgreSQL долгое время, я забыл что
UUID то метода в других БД - нет. По этому принял решение перейти на String UUID, проблем никаких нет, единственная
доп.задача - преобразование, но для JVM эта задача на раз плюнуть особенно есть JIT

CFG:
🧡 config.yml | Очень кривое нарушение табуляции привело к null среди ошибок, как взглянул на цепь сразу понял в чем
дело. Мдамс.. Быть внимательнее❗ Хотя писал я основу еще очень давно, а сейчас взялся за нее, так что внимание
повышенное.

❤️ config.yml | От permissions в "Admin.AdminRanks" - Решил отказаться. Причина: Архитектурный задел на будущий факт
системы который раскроет и разрешения, и скины, и прочее. Решение: Кастомные permissions у рангов - отказано.

Event:
🧡 PlayerQuitEvent | Исправлен баг/недочет PlayerJoinEvent -> PlayerQuitEvent e.

Manager:
💚 AdminSessionManager | В связи с последними работами над NullPointerException. Добавлены защиты от базы ❗

DAO:
💚 SecretCodeDAO | Добавлен конструктор для mutable state;
💚 AdminsDAO | Добавлен конструктор для immutable state;
💚 ReportDAO | Добавлен конструктор для mutable state;

💛 AdminsDAO | Изменено: delAdminLog -> delAdminLogInsert. Причина: Уточнение, лучше читается лучше исправляется -
Порядок всегда важен.
💛 AdminsDAO | Исправлена ошибка синтаксиса UUID в delAdmin.

Repository:
💚 AdminSystemRepository | Новый метод checkAdminInBase который позволяет удостовериться и ответить на вопрос: Eсть ли
администратор в базе?

🧡 AdminStaffRepository | Метод deleteAdminStatusLog(consoleID, adminID, reason) переделан под стандарт ELM
deleteAdminStatusLog(adminID, consoleID, reason). В нем был баг ❗
💛 AdminSystemRepository | Замена мелочи для читаемости: id -> adminID. То есть уточнение что это ИМЕННО админ, потому
что getADMIN... | Добавление защиты от NullPointerException 🧑‍💻

E1m0: 🧑‍💻 "Нормально оформлен TODO list 2.0 | -> DEVLOG.md"
E1m0: 🧑‍💻 "Нормально оформлены таски, теперь 🧡 - Затык или баг | -> DEVLOG.md"

PICK-CHECK COMMANDS:

    ADMIN:

    STAFF:
    
    CONSOLE:
        $cup E1m0 | Отработала, исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕
        $cdel E1m0 | Отработала, опять исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕
        $cdown E1m0 | Отработала, опять исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕

2.0 Admin Optional && Player Structure:
Сделать поддержку СБД
