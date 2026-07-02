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

    СONSOLE:
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

    CONSOLE:
        $cup E1m0 | Отработала, исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕
        $cdel E1m0 | Отработала, опять исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕
        $cdown E1m0 | Отработала, опять исправлена вместе с половиной StaffService && StaffRepo. | Прошла тестеры: ➕

Commit 1.7-DEV | Fix SecretCode architecture engine, command, listeners, controllers upd..

API:
💛 SecretCodeRepositoryAPI | byte getSecretCode(UUID adminID) -> int getSecretCode(UUID adminID); Причина: Отказ от byte
архитектуры.

❤️ GameServiceAPI | Удален openReportGUI! Переработка структуры, теперь AccessCommand наследует GUI, а не передает в
Service. Причина: Цепная не совместимость композиции зависимостей.
❤️ GameServiceAPI | Удален handleAccess! Переработка структуры, теперь AccessCommand наследует GUI, а не передает в
Service. Причина: Цепная не совместимость композиции зависимостей.

State:
🧡 state/session/AdminSessionManager | Ревью кода под новую мета обертку. Отделение вложенности и коррекция.
🧡 AdminSessionManager && E1m0Admin | Реворк кэша через композицию, новая мета, и так удобнее понимать что у меня в кэше
а что в базе, в будущем смогу для клиентов создать вообще без базовую основу чисто на кэше для их удобства: "Запустил и
отпустил"

Command:
💚 AccessCommand | Переработка структуры, теперь AccessCommand наследует GUI, а не передает в Service. Причина: Цепная не
совместимость композиции зависимостей.
💚 ReportCommand | Переработка структуры, теперь ReportCommand наследует GUI, а не передает в Service. Причина: Цепная не
совместимость композиции зависимостей.
🧡 AccessCommand | Мелкая доработка: Убрал отладочный Permission.

Service:
💛 SecretCodeService | Все методы кода принимающие в BYTE были переписаны на архитектуру INT(EGER). Причина:
Архитектурный отказ от BYTE, ради целостности и красоты системы и ее дальнейшей поддержки.
🧡 SecretCodeService | Забыл про маленькое обьявление State, но при отладке поймал баг, теперь вся цепь pin-2-3-4 сроится
четко.
🧡 AdminSystemService | Дополнительная проверка НА ВСЯКИЙ случай в adminPay, она отделяет нужных администраторов от не
нужных

Repository:
💛 SecretCodeRepository | byte getSecretCode(UUID adminID) -> int getSecretCode(UUID adminID); Причина: Отказ от byte
архитектуры.

DAO:
💛 BonusDAO | Системы UUID uuid из-за комита версии 1.6-DEV переведены в String type (VARCHAR) так как он легче
поддерживается во всех базах, а не только OBJECT в Postgres, опять же усложнение для меня в перестройки и тестировании
системы на UUID в угоду клиенту.
💛 ReportDAO | Системы UUID uuid из-за комита версии 1.6-DEV переведены в String type (VARCHAR) так как он легче
поддерживается во всех базах, а не только OBJECT в Postgres, опять же усложнение для меня в перестройки и тестировании
системы на UUID в угоду клиенту.
💛 SecretCodeDAO | Системы UUID uuid из-за комита версии 1.6-DEV переведены в String type (VARCHAR) так как он легче
поддерживается во всех базах, а не только OBJECT в Postgres, опять же усложнение для меня в перестройки и тестировании
системы на UUID в угоду клиенту.

GUI:
💛 SecretCodeGUI | Решил отказаться от num при передаче в GUI, теперь этой работой у меня ведь занимается Service а
точнее getInputCode(ID).

🧡 SecretCodeGUI | Подловил оплошность между GUI -> CONTROLLER, holder был назван step_fours (Из-за красоты написания
среди других в цепи для того чтобы не выделялся) а в GUI был step_four
🧡 SecretCodeGUI | Исправлена проблема с форматированием названий холдеров, все были one_step, это почти ничего не
значит, я все равно сравниваю по другим параметрам, но все равно не приятно.
🧡 SecretCodeGUI | Метод: createInventory -> createItemsInInventory. Причина: Улучшение читаемости + сразу понятен
контекст картины, что там именно происходит.

Controller:
💛 SecretCodeController | Метод и система handlerButton (Система перевода ACTION - BYTE) была переписана на INT. Причина:
Архитектурный отказ от BYTE, ради целостности и красоты системы и ее дальнейшей поддержки.

TabCompliter:
💚 MainTabCompliter | Добавление инструкций новых команд из Console, cdel, cup и т.д!

E1m0:
💚 E1moAdmin | Смена архитектурной цепи, теперь GUI выше Command, выше чем контроллер, ниже чем service и event с
listener.
💚 E1moAdmin | Теперь COMMAND -> GUI -> CONTROLLER -> SERVICE -> EVENT -> LISTENER

🧡 E1moAdmin | Гоняясь за красотой своего main я дал в поддых структуре композиции и 40 минут гонялся за невидимым null
методом которым оказался класс service который я поставил ниже чем GUI
🧡 E1moAdmin | SecretCodeController && ReportController - Были успешно подключены ➕

Исправлено:
💜 | Починил баг с Sessions, перенес конструктор, теперь все должно быть верно и не равнятся нулю, на всякий поставил
защиту.
💜 | Отладил контроллеры, понял что они не подключены, сделал все красиво.

    ARCHITECTURE-GUI:
        /acc -> COMMAND -> GUI -> CONTROLLER -> SERVICE -> EVENT -> LISTENER | ЦЕПЬ УСПЕШНО ПРОШЛА ➕

Commit 1.8-DEV | feat: Check/Tests Admin commands and new message for config, new realisation for utils (E1m0Sender) &&
Fix permission in commands and E1m0Permission

API:
❗ SecretCodeActions - Был удален из API. Причина: Архитектурная запутанность, нашлось решение быстрее через обычные
Immutable String transfer State (+ Thread Safety)

CFG:
💚 config.yml | Messages/Errors: deleteAdminWeightError | Если вес администратора target перевешивает weight
администратора admin который выполняет команду - Ошибка. Пример: Администратор 1-го уровня не может снять разработчика.
💚 config.yml | Messages/Errors: downAdminWeightError | Если вес администратора target перевешивает weight администратора
admin который выполняет команду - Ошибка. Пример: Администратор 1-го уровня не может понизить разработчика.
💚 config.yml | Messages/Errors: upAdminWeightError | Если вес администратора target перевешивает weight администратора
admin который выполняет команду - Ошибка. Пример: Администратор 1-го уровня не может повысить разработчика.

💚 config.yml | setAdminCodeWrong -> setAdminCodeIsWrong | Улучшение читаемости и адаптивности, если будут разработчики
сразу понимают что это и зачем.
💚 config.yml | Permissions.setadmin -> Permissions.setadm | Улучшение скорости читаемости и композиции в цепи, все
должно быть понятно и красиво.

💚 config.yml | Добавил новую функцию как: prefixEnable? Свитчер отвечает за префикс соответсвенно, в утилите будет ли
показан префикс или нет?
💚 config.yml | Не большие правки архитектуры, поднял Server наверх для лучшей ориентации по конфигу для клиента.

💚 config.yml | Добавил новую строчку в Messages.Errors: permissionError, если у администратора нет права на команду -
вылетает она.

❤️ config.yml | Убрана возможность адаптивности кнопок до следующих версий. Причина: Я посчитал что репортов можно
сделать максимум 50. И просто сетать их в меню, пока - страницы не нужны.

Sender:
💚 E1m0Sender | Новый функционал в связи с добавлением prefixEnable в конфиг для моих методов sendPath и sendString 🧑‍💻

Commands:
💚 RewatchCommand | Поменял половину схематики команд re, теперь reoff не нужны аргументы, а re - да, создать отдельную
команду? А зачем? Единая система - единый обработчик.

💚 AdminUpCommand | Добавлена проверка на Staff point;
💚 AdminDelCommand | Добавлена проверка на Staff point;
💚 AdminDownCommand | Добавлена проверка на Staff point;

💛 AccessCommand | Небольшое ревью с правами.
💛 AdminSetCommand | Небольшое ревью с правами.

💜 RewatchCommand | Небольшое исправление, поменял условие аргументов с 2 на 1, потому что нужен только Player, админ
идет с команды.
💜 RewatchCommand | Небольшое исправление strings[1] -> strings[0], потому что аргумент 1, всегда забываю

Service:
💚 AdminStaffService | Добавлена защита от неподобающей должности по weight в upAdmin
💚 AdminStaffService | Добавлена защита от неподобающей должности по weight в delAdmin
💚 AdminStaffService | Добавлена защита от неподобающей должности по weight в downAdmin

💚 ConsoleService | Добавлена защита от неподобающей должности по weight в upAdmin
💚 ConsoleService | Добавлена защита от неподобающей должности по weight в downAdmin

Listener:
💛 AdminAccessListener | onAdminJoin -> onAdminRegistered. Улучшение читаемости и адаптивности, если будут разработчики
сразу понимают что это и зачем.

TabCompleter:
💚 MainTabCompliter: ПОЛНАЯ ПЕРЕСТРОЙКА ЛОГИКИ, все заменено на switch архитектуру, вместо не рабочего strings[].
💛 Маленькое ревью логики, чисто для мелкой оптимизации и читаемости.

E1m0Admin:
💚 arec -> re | Переименование. Причина: Я думаю - что rewatch, reoff - это частые команды. И они - должны быть
сокращенными, для любителей харда оставил aliases: [ roff, arewatchoff ]
💚 areoff -> reoff | Переименование. Причина: Я думаю - что rewatch, reoff - это частые команды. И они - должны быть
сокращенными, для любителей харда оставил aliases: [ roff, arewatchoff ]

💚 Новые aliases в TAB-Completer чисто для удобства админов в игре.
💚 Новый слой памяти по композиции перенесенный из SecretCodeStateManager: codeCache. Позволяет хранить пользовательские
пароли с данными.
💛 playerReport -> playerReportCache | Улучшение читаемости и адаптивности, если будут разработчики сразу понимают что
это и зачем.

Что было исправлено:
💜 ПОЧИНИЛ ЦЕПОЧКУ PERMISSIONS ВМЕСТЕ С AINV ❗
💜 Исправлена архитектура DI в связи с permissionManager. Перекинул его в самый низ.
💜 Исправлена утечка информации среди сервиса и stateManager, я понял что происходит именно не свойство типов и нашел
лик.

E1m0: 🧑‍💻 "Новые идеи для TODO list 2.0 | -> DEVLOG.md"
E1m0: Вот думаю, скорее всего растяну на несколько обновлений функции, если будет аудитория плагина надо решить вопрос с
багами, а после - с обновлением, я же знаю что они точно будут у клиентов.

PICK-CHECK COMMANDS:

    ADMIN:
        /re E1m0 - VDOVA | Отработала, исправлена | Прошла тестеры: ➕
        /reoff E1m0 | Отработала, исправлена | Прошла тестеры: ➕
        /re E1m0 | Отработала, исправлена | Прошла тестеры: ➕

        /ainv | Отработала, исправлена | Прошла тестеры: ➕

Commit 1.9-DEV | Adaptivity report cache system && fixes, testes, reworks upd..

E1m0:

    ❗ ВАЖНО | E1m0: Я - начинаю создавать адаптивный кэш внутри системы, репорты = временное состояние игрока на данную сессию. Вот и получается что если сервер внезапно взорвется - состояние потеряно, а в базе данных все еще статус "Не обновлено" и вот тебе потеря информации и memory leak в архитектуре. В общем, не очень хорошее дело, по этому мне придется помучатся сейчас и проверить огромную систему так еще и переписав кэширование как минимум четырех сервисов и репо лиж бы у клиента ничего не упало.. Этот метод гараздо более надежный чем UUID UUID, здесь, база - как сам факт игрового ответа администратора и полноценного закрытого репорта. Не скажу что это прямо архитектурная ошибка, скорее моя параноя, и прихоть, потому что я это создаю и мне нести ответственность и смотреть в глаза клиентам когда я буду выпускать это с UUID UUID, по этому лучше перестраховатся. 

💚 playerReportCache - Теперь первостепенна отвечает за новую архитектурную цепь кэша.
💛 playerReportCache -> playerReportUUIDsCache, теперь становится второстепенным в виде нового обновления строки
кэширования.
❤️ playerReportUUIDsCache -> От системы решено полностью отказатся, в угоду новой быстрой и надежной системе
playerReportCache <UUID, Report>

API:
❤️ ReportSystemServiceAPI | Удален метод List<Report> getReports(); Причина: Теперь работа с базой для репортов и меню -
мне не нужна, если хочу добиться для клиентов системы "Запустил и отпустил", первый шаг на пути к СБД и оптимизации =
кэширование.

❤️ GameRepositoryAPI && AdminGameRepository | Из него был удален метод gameReportSend и был перемещен:
💚 ReportSystemRepository | Был перемещен на свое законное место, я знаю почему я так сделал, сейчас я не считаю репорт
частью игрового процесса администратора, тогда - считал, но архитектурно лучше разделять принципиальные структуры.

💛 GameRepositoryAPI | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости | Мета:
gameReport -> gameReportSend.

CFG:
💚 config.yml | Новое сообщение в Messages/Errors/: reportSizeIsMax - Если репортов больше чем установлено на сервере ->
вызывается ошибка.
💚 config.yml | Новое сообщение в Messages/Errors/: reoffNullError - Если человек не за кем не следит - отключится он не
может.

💚 config.yml | Новое сообщение в Messages/: successfulUpStaff - Отвечает за сообщение стаффу после повышения.
💚 config.yml | Новое сообщение в Messages/: successfulUpAdmin - Отвечает за сообщение админу после повышения.

💚 config.yml | Новое сообщение в Messages/: successfulDownStaff - Отвечает за сообщение стаффу после понижения.
💚 config.yml | Новое сообщение в Messages/: successfulDownAdmin - Отвечает за сообщение админу после понижения.

💛 config.yml | Замена функции: reportInDataLimit -> reportMaxSize. Если раньше я брал данные из базы то сейчас я беру их
из кэша, по этому новая логика - новая функция.

Commands:
PLAYER:

        💚 PlayerReportCommand | Ревью модели блока Permissions.

        💚 PlayerReportCommand | Была переписана на новую архитектуру кэширования playerReportCache
            ❤️ PlayerReportCommand | Отказ от старой системы playerReportCache и переход на новую.

        💚 PlayerReportCommand | Новое условие и сообщение в конфиг: if(reportList.size() >= 49). Расшифровка: "Если репортов больше или равно 49 -> "

ADMINS:

        💚 RewatchCommand | Мелкое исправление, ревью уже есть с комита 1.8-DEV;
        💚 AccessCommand | Мелкое исправление, ревью уже есть с комита 1.8-DEV;
        🩷 InviseCommand | Ревью модели блока Permissions;
        🩷 ReportCommand | Ревью модели блока Permissions;

        🩷 AdminSetSecretCode | Ревью кода, вынесение логических инвариантов вверх, и добавление нового блока проверки str.length, забыл про него но как взглянул - сразу добавил.

        💜 AdminSetSecretCode | Нашел баг с правами, которых даже не существует: "Permissions.asecret" -> "Permissions.secretcode", это произошло скорее всего из-за табуляции в конфиге, потому что я делаю все красиво и упорядоченно.
        💜 AdminDownCommand   | Исправлен логический затык, логичсеское условие: >=. Было инвертировано: <=.
        💜 AdminDelCommand    | Удалены остатки permissions logic check, исправлен затык.

STAFF:

        💚 AdminDeleteCommand | Мелкое исправление, вместо печати сделал отдельный permissions для улучшение функционала клиентов, ревью уже есть.
        💚 AdminDownCommand   | Мелкое исправление, вместо печати сделал отдельный permissions для улучшение функционала клиентов, ревью уже есть.
        💚 AdminSetCommand    | Мелкое исправление, вместо печати сделал отдельный permissions для улучшение функционала клиентов, ревью уже есть.

        🩷 AdminBonusAllCommand | Ревью модели блока Permissions.
        🩷 AdminSetSecretCode   | Ревью модели блока Permissions.
        🩷 AdminBonusCommand    | Ревью модели блока Permissions.
        🩷 ReportCommand        | Ревью модели блока Permissions.

        💛 AdminBonusCommand    | Небольшая правка чередования зависимостей | Мета E1m0.

State:
❤️ Report | Была удалена система-конструктор answer. Причина: Новый переход на адаптивное кэшируемое системное состояние
playerReportCache, и уже никому не нужен State, если из старого можно собрать полноценный новый который будет уже фактом
а не "сборщиком состояния", это надежнее для репорта, а некогда половина его параметров = null как раньше.

GUI:
💚 ReportGUI | Переход на новую систему счисления не в базе данных а в кэше самого плагина: playerReportCache.

Service:
💚 AdminSystemService | Сервис по репорту: clickToReport -> переведен на систему <UUID, REPORT>: playerReportCache.
Причина: Новый переход на адаптивное кэшируемое системное состояние playerReportCache.
💚 AdminGameService | Сервис по репорту: fastReport -> переведен на систему <UUID, REPORT>: playerReportCache. Причина:
Новый переход на адаптивное кэшируемое системное состояние playerReportCache.

💚 AdminGameService | В методе handleReoff(UUID adminID), решил сделать проверку на rewatchTasksCache.containsKey(
adminID) и добавил сообщение "Messages.Errors.reoffNullError".
💚 AdminStaffService | В метод adminBonusAll - тоже решил добавить логирование в базе данных, очень хочется уже личную БД
сервера H2 для пользователей чтобы им было легче, но в будущем обязательно сделаю.

💚 AdminStaffService | Были добавлены новые сообщение successfulDownStaff, successfulDownAdmin | downStatus(UUID adminID,
UUID staffId)
💚 AdminStaffService | Были добавлены новые сообщение successfulUpStaff, successfulUpAdmin

🧡 AdminGameService | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости | Мета:
gameReport -> gameReportSend.

🧡 AdminStaffService | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости | Мета:
deleteAdmin(UUID staffID, UUID adminID, String reason) -> deleteAdmin(UUID adminID, UUID staffID, String reason)
🧡 AdminStaffService | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости | Мета
downStatus(UUID staffId, UUID adminID) -> downStatus(UUID adminID, UUID staffId)
🧡 AdminStaffService | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости | Мета:
adminBonusGive(UUID staffID, UUID adminID, int sum, String message) -> adminBonusGive(UUID adminID, UUID staffID, int
sum, String message)

❤️ ReportSystemService | Реализация List<Report> getReports() - была удалена. Причина: Теперь работа с базой для
репортов и меню - мне не нужна, если хочу добиться для клиентов системы "Запустил и отпустил", первый шаг на пути к СБД
и оптимизации = кэширование.

💜💛🧡 AdminStaffService | Переделка условия. Причина: Адаптивность, читаемость: !admin.hasPermission("e1admin.adm") -> !
admin.hasPermission(cfg.getString("Permissions.admin"))
💚 AdminStaffService | Еще: Добавил сообщение permissionError в проверку !admin.hasPermission(cfg.getString("
Permissions.admin")), всяко лучше чем return.

💜💛🧡 AdminStaffService | Переделка условия. Причина: Адаптивность, читаемость: if (!p.hasPermission("e1admin.adm"))
continue; -> if (!admin.hasPermission(cfg.getString("Permissions.admin"))) continue;

P.S: 💜 - Фикс бага, 💛 - Исправление самого метода в котором ведется работа, 🧡 - Переименование метода над которым
ведется работа. Вот и получается: Фикс + Исправление + Переименование под мету = 💜💛🧡

Repository:
💚 ReportSystemRepository | Метод gameReportSend был переписан на новую архитектуру решений сразу из кэшированного
репорта, здесь база - выступает как ФАКТ репорта, можно еше добавить ивент, но думаю будет лишним, хотя возможно в 2.0

🧡 AdminStaffRepository | Мелкие правки по расположению принимающих конструкторов для метода, улучшение читаемости |
Мета: giveBonusLog(UUID staffID, UUID adminID, int sum, String message) -> giveBonusLog(UUID adminID, UUID staffID, int
sum, String message)

Listener:
💚 QuitListener | Добавляется новая проверка на выход: playerReportCache.containsKey(user.getUniqueId()). Расшифровка: "
Если у человека есть репорт и он вышел из игры -> удалить его!"
🧡 QuitListener | onAdminJoin -> onUserJoin | Потому что появляется новая проверка c новым кэшированием:
playerReportCache.containsKey(user.getUniqueId())

DAO:
💜 SecretCoddeDAO | Поймал маленькую запятую в синтаксисе запроса JDBI.
💜 BonusDAO | Оплошность.. e1admin_REPORT -> e1admin_bonus. Чет я не доглядел, надо будет откат глянуть, ставлю на то что
делал все остальные DAO и забыл о bonus.

Plugin:
💜 plugin.yml | Мелкие правки, исправил arep (Админа) -> report, скорее всего просто заплутал в конфигурации когда
связывал команды, так же сделал нормальную табуляцию комментариев для самого же себя и других разработчиков
💜 plugin.yml | Добавил контроллер arepaccept для системы чтобы консоль сендер на всякий не ругался.

💜 Исправил arep -> rep. Команда arep - все еще существует просто я ее сократил.

TabCompleter:
💚 MainTabCompleter | Новые aliases в комплитер.

💛 MainTabCompleter | Вместо "Player?" в abonus, добавил список игроков, когда перестраивал логику, делал все на цепь, а
не удобство, исправил.

Решенные задачи в процессе:
💜 Добавил Vault на тест сервер для eco, не забывать, что к нему нужен адаптер по типу: Essentials, мне не нравится
добавлять два огромных плагина один из которых умер с концами во времена Bukkit, написать свой?..

E1m0: 🧑‍💻 "Новые идеи для TODO list 2.0 | -> DEVLOG.md"

PICK-CHECK COMMANDS:
ADMIN:

        /asecret Alberto 7777 | Отработала, исправлена | Прошла тестеры: ➕

        /abonus Alberto 777 Работай | Отработала, исправлена | Прошла тестеры: ➕
        /abonusall 777 Работаем | Отработала, исправлена | Прошла тестеры: ➕

        /adown Alberto | Отработала, исправлена | Прошла тестеры: ➕
        /adel Alberto | Отработала, исправлена | Прошла тестеры: ➕
        /aup Alberto | Отработала, исправлена | Прошла тестеры: ➕

2.0 Admin Optional && Player Structure:
Сделать поддержку СБД
