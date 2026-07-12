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

Commit 1.10 | pre-release check and review code for E1m0

API:

    Repository:
        💚 ReportGuiAPI | Добавлены комментарии для большего понимания и использования.
        💚 SecretCodeGuiAPI | Добавлены комментарии для большего понимания и использования.
    
        💚 ReportSystemRepositoryAPI | Добавлены комментарии для большего понимания и использования.
        💚 SecretCodeRepositoryAPI | Добавлены комментарии для большего понимания и использования, небольшая реконструкция внутреннего расположения.
    
        💚 GameRepositoryAPI | TODO..
        💚 StaffRepositoryAPI | Добавлены комментарии для большего понимания и использования, небольшая реконструкция внутреннего расположения.

    Service:
        💚 ConsoleServiceAPI  |  Добавлены комментарии для большего понимания и использования.
        💚 SystemServiceAPI   |   Добавлены комментарии для большего понимания и использования.
        💚 StaffServiceAPI    |    Добавлены комментарии для большего понимания и использования.
        💚 GameServiceAPI     |     Добавлены комментарии для большего понимания и использования.

    Utils:
        💚 PermissionsManagerAPI | Добавлены комментарии для большего понимания и использования.
        💚  SenderAPI | Добавлены комментарии для большего понимания и использования.

Service:

    🩷 StaffServiceAPI | Ревью новой меты для порядка и красоты архитектуры: upStatus(UUID staffID, UUID adminID); -> upStatus(UUID adminID, UUID staffID);
    🩷 StaffServiceAPI | Ревью новой меты для порядка и красоты архитектуры: downStatus(UUID staffID, UUID adminID); -> adminBonusGive(UUID adminID, UUID staffID, int sum, String message);
    
    🩷 StaffServiceAPI | Ревью новой меты для порядка и красоты архитектуры: setAdmin(UUID staffID, UUID adminID, int weight); -> setAdmin(UUID adminID, UUID staffID, int weight);
    🩷 StaffServiceAPI | Ревью новой меты для порядка и красоты архитектуры: deleteAdmin(UUID staffID, UUID adminID, String reason); -> deleteAdmin(UUID adminID, UUID staffID, String reason);
    
    🩷 StaffServiceAPI | Ревью новой меты для порядка и красоты архитектуры: adminBonusGive(UUID staffID, UUID id, int sum, String message); -> adminBonusGive(UUID adminID, UUID staffID, int sum, String message);

Repository:

    💚 SecretCodeRepositoryAPI  | Новый метод systemDeleteAdmin(UUID adminID). Очень важная штука, буквально триггер для удаления информации с карты кодов.

    🩷 StaffRepositoryAPI | Ревью новой меты для порядка и красоты архитектуры: deleteAdminStatusLog(UUID staffID, UUID adminID, String reason) -> deleteAdminStatusLog(UUID adminID, UUID staffID, String reason);
    🩷 StaffRepositoryAPI | Ревью новой меты для порядка и красоты архитектуры: systemDeleteAdminStatusLog(UUID staffID, UUID adminID, String reason) -> systemDeleteAdminStatusLog(UUID adminID, UUID staffID, String reason);
    
    💛 AdminStaffRepository | Небольшие правки в расположении переменных, ну чисто моя прихоть по моей мете.

    ❤️ ReportSystemRepositorAPI | List<Report> getReportList(String status, int limit) -> Был удален. Отказ в угоду хэширования state игрового состояния с коммита 1.9.
    ❤️ ReportSystemRepositorAPI | updateReport(Report report) -> Был удален. Отказ в угоду хэширования state игрового состояния с коммита 1.9.
    ❤️ ReportSystemRepositorAPI | Report getReport(UUID id) -> Был удален. Отказ в угоду хэширования state игрового состояния с коммита 1.9.

CFG:
💚 config.yml | Admin.Report.ReportGUI: В список предметов я вернул контроллер на закрытие меню, это заготовка для
будущих страниц если понадобится, так же я добавил возможность ставить lore, name, и т.д

💚 config.yml | Новое сообщение в Messages/Errors/: adminSendReportToAdmins - Отвечает за ошибку администратору который
пытался отправить репорт..
💚 config.yml | Новое сообщение в Messages/Errors/: secretCodeHasInputted - Отвечает за ошибку администратору который
пытался ввести пин код по новой.

💚 config.yml | Новое сообщение в Messages/: reportAdminResponse - Отвечает за отправку сообщения игроку о взятии репорта

💚 config.yml | Новое сообщение в Messages/: successfulDeleteAdmin - Отвечает за отправку удачной транзакции и действия
стафф администратору.
💚 config.yml | Новое сообщение в Messages/: deleteAdminByStaff - Отвечает за отправку уведомления о снятии
администратору..

💚 config.yml | Новое сообщение в Messages/Errors: isAdminContainsData - Отвечает за отправку уведомления о ошибке, ведь
администратор уже есть в базе данных
💚 config.yml | Новое сообщение в Messages/: successfulSetAdmin - Отвечает за отправку удачной транзакции и действия
стафф администратору.
💚 config.yml | Новое сообщение в Messages/: successfulSetStaff - Отвечает за отправку уведомления о постановлении
администратору..

💛 config.yml | Исправление внутренностей upAdminWeightError, потому что у меня там два больших тире —— потому что
выглядело красиво..
💛 config.yml | Исправление внутренностей setAdminWeightNotFound, потому что у меня там два больших тире —— потому что
выглядело красиво..
💛 config.yml | Я заменил все маленькие тире на большие, потому что мне так вид нравится больше, в связи с прошлыми
правками, так же как на айфоне :3

💜 config.yml | ReportGui -> ReportGUI, был баг с многими такими строками взаимодействия
💜 config.yml | Несколько багов были из-за НИЖНЕГО РЕГИСТРА в material.valueOf() это стоит запомнить ❗

Commands:

     — | ADMIN COMMANDS REVIEW:
        🩷 AccessCommand       | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости;
        🩷 ReportCommand       | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок;
        🩷 RewatchCommand      | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок;
        🩷 InvisibilityCommand | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости;

     — | CONSOLE COMMANDS REVIEW:
        🩷 ConsoleDownAdminCommand | Проверка кода, перенесение логических элементов из адаптера, добавлены новые проверки и переносы локалов.
        🩷 ConsoleSetSecretCommand | Проверка кода, перенесение логических элементов из адаптера, добавлены новые проверки и переносы локалов.
        🩷 ConsoleUpAdminCommand   | Проверка кода, перенесение логических элементов из адаптера, добавлены новые проверки и переносы локалов.
        🩷 ConsoleDelAdminCommand  | Проверка кода, новое условие reason.isEmpty(), скан решений, добавлены новые проверки и переносы локалов.
        🩷 ConsoleSetAdminCommand  | Проверка кода, перенесение логических элементов из адаптера, добавлены новые проверки и переносы локалов.

     — | PLAYER COMMANDS REVIEW:
        🩷 PlayerReportCommand  | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок;

     — | STAFF COMMANDS REVIEW:
        🩷 AdminSetSecretCode   | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
        🩷 AdminDeleteCommand   | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
        🩷 AdminDownCommand     | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
        🩷 AdminSetCommand      | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
        🩷 AdminUpCommand       | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
    
        🩷 AdminBonusCommand    | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;
        🩷 AdminBonusAllCommand | Ревью новой меты для порядка и красоты архитектуры: Добавлены вспомогательные локальные переменные, новые условия проверки для легкости читаемости, перестроена структура проверок и переменных;

     — | SYSTEM COMMANDS REVIEW:
        🩷 ReportAcceptController | Ревью новой меты для порядка и красоты архитектуры: Перестроена структура проверок и переменных;


    — | "SUN CHECK":
        💚 AdminBonusAllCommand | Исправил проблему со строками Array.
        💚 AdminBonusCommand | Исправил проблему со строками Array.
    
        💚 AccessCommand     | Добавлена новая проверка на админ-доступ, вместе с новым сообщением secretCodeHasInputted.
    
        💜 ReportCommand     | Исправлено логическое состояние проверки условия args, оно было ивертировано.
            ❤️ ReportCommand | Была убрана старая проверка permissions (Legacy Garbage | Старый мусор.)
            💛 ReportCommand | Исправлен баг со строками в response.
            💚 ReportCommand | Были добавлены тестеры для отладки.
        
        💚 PlayerReportCommand | Новая проверка, если отправитель админ -> отправляется новый Message: "Messages.Errors.adminSendReportToAdmins"
        
        💜 PlayerReportCommand | Исправил баг, который хотел поправить еще вчера. Баг был в пути к конфигу, потому что я заменял фукнции, отладил смотрю сравнивается 0 >= 0 - залатал.
        💜 PlayerReportCommand | TODO: Сделать проверку на 50 репортов, потому что пока что - одна страница. | ➕ Выполнено, удалено.
        
        🧑‍💻 PlayerReportCommand | Для проверки /report отлаживаю и отслеживаю движения тестарами.

Service:
🩷 AdminsStaffService | Ревью новой меты для порядка и красоты архитектуры: setAdmin(UUID staffID, UUID adminID, int
weight) -> setAdmin(UUID adminID, UUID staffID, int weight)

💚 ReportSystemService | Для большего понимания, добавил сообщение "Messages.reportAdminResponse" с аргументами для
игрока с ответом администратора.
💚 AdminStaffService | Для более широкого "Спектра эмоций" для DelAdmin, и проецирования состояния на обе стороны были
добавлены сообщения. Для Staff: "Messages.successfulDeleteAdmin", для админа - уже игрока: deleteAdminByStaff
💚 AdminStaffService | Для более широкого "Спектра эмоций" для SetAdmin, и проецирования состояния на обе стороны были
добавлены сообщения. Для Staff: "Messages.successfulSetStaff", для игрока - уже админа successfulSetAdmin
💚 AdminStaffService | Для более широкого "Спектра эмоций" для setAdmin, и проецирования состояния ошибки для стаффа
когда он пытается поставить администратора, который есть придумано условие и новое сообщение isAdminContainsData

💚 AdminStaffService | Из secretCodeRepository был добавлен триггер метод systemDeleteAdmin который позволяет удалять
информацию с карт SecretCode, permissions - сделаю потом когда будет обновление.
💚 ConsoleService | Из secretCodeRepository был добавлен триггер метод systemDeleteAdmin который позволяет удалять
информацию с карт SecretCode, permissions - сделаю потом когда будет обновление.

💛 ReportSystemService | Почти полностью переработан блок релизации clickToReport, новые решения и переход со старой
системы от базы.
💛 AdminSystemService | Почти полностью переработан блок релизации handleReportAccept, новые решения и переход со старой
системы от базы.

💛 AdminGameService | Починил баг со звуками, которые были не у админов, а у игроков 🤣
💛 AdminGameService | Поменял старую мету отправки сообщений когда словил NullPointerException, при конечном рефакторинге
и ревью кода надо будет обязательно посмотреть на такие участки, мне бы вынести конечно репорт в его сервис а не
GameService по старой мете..
💛 AdminGameService | Сделал реворк системы логического сравнения, теперь все должно быть нормально, у меня было наверху
условие: Если emergency off = цепь дальше не идет, и когда я об этом вспомнил я разделил одну цепь на два каскада и три
разных условия два из которых (Default rep/Donate rep) связаны и находятся в одном каскаде. Теперь если что присылается
сразу 3 сообщения, если это срочный репорт от донатера например что точно заметят администраторы.

💜 ReportSystemService | Исправлен баг и убрано Legacy с сообщением "Messages.reportTake" в clickToReport.
💜 AdminStaffService | Исправлен баг с отправкой сообщения upAdminLevelError не тому 🤣

Repository:
❤️ ReportSystemRepositor | Реализация List<Report> getReportList(String status, int limit) -> Был удален. Отказ в угоду
хэширования state игрового состояния с коммита 1.9.
❤️ ReportSystemRepositor | Реализация updateReport(Report report) -> Был удален. Отказ в угоду хэширования state
игрового состояния с коммита 1.9.
❤️ ReportSystemRepositor | Реализация Report getReport(UUID id) -> Был удален. Отказ в угоду хэширования state игрового
состояния с коммита 1.9.

DAO:
💚 SecretCodeDAO | void delAdminSecret(UUID adminID) -> Новый метод для вырезания админ-кода при увольнении.

❤️ ReportDAO | Транспортировка и запрос List<Report> getReportList(String status, int limit) -> Был удален. Отказ в
угоду хэширования state игрового состояния с коммита 1.9.
❤️ ReportDAO | Транспортировка и запрос updateReport(Report report) -> Был удален. Отказ в угоду хэширования state
игрового состояния с коммита 1.9.
❤️ ReportDAO | Транспортировка и запрос Report getReport(UUID id) -> Был удален. Отказ в угоду хэширования state
игрового состояния с коммита 1.9.

GUI:
💚 ReportGUI | Была добавлена новая проверкаа на null, isEmpty: noReportsInMemory.

💚 ReportGUI | Были добавлены E1m0Color && E1m0Sender, тоже заготовка на будущее.
💚 ReportGUI | В список предметов я вернул контроллер на закрытие меню, это заготовка для будущих страниц если
понадобится, так же я добавил возможность ставить lore, name, и т.д

💜 ReportGUI | Словил NullPoint, оказалось дело в пути ReportGui != ReportGUI. Когда я проводил рефакторинг кажется,
забыл поменять пути в некоторых старых методах, в новых все нормально.
💜 ReportGUI | Так же исправлены баги с путями с itemSlot (Уже ревью система, но все равно пройтись надо будет)

TabCompleter:
💚 MainTabCompleter | Новые aliases в комплитер.

💜 Поставил не достающий case "report": break.

E1m0: 🧑‍💻 "Новые идеи для TODO list 2.0 | -> DEVLOG.md"

PICK-CHECK COMMANDS:
PLAYER:

        /report Alberto Читер | Отработала, исправлена | Прошла тестеры: ➕

ADMIN:

        /rep Спешу к Вам на помощь | Отработала, исправлена | Прошла тестеры: ➕
        ➕ Цепь: Command-GUI-Controller-Service-Repository-DAO-Database.

Version 1.0 - Commit 1.11: fix last bugs && release in platforms:

E1m0: Последние

❗ | Architecture/Plugin:

Зачем нужены? - Я решил отказаться от "God" класса в виде "dev" или "dev.md", и раскидать решения: задачи, диалоги с
собой и решения по разным файлам и разным мета контекстам.

💚 /.e1m0dev.*.md | Новая папка в которой будет архитектурные правила, дейсвтия, таски, и прочее, все - должно быть
красиво.

💚 CHECKLIST.MD | Что нужно сделать перед определенным действием? Например: Перед выходом новой версии сделать ревью
нового кода.
💚 E1M0DEV.MD | Какие решения нужно рассмотреть и какие вопросы обсудить в будущем? Например: Добавлять ли /ahelp? -
Через время, я отвечаю по мере своей нагрузки и с новым контекстом.
💚 TODO.MD | Что нужно выполнить а что перенести? Например: В разработке новой версии закрыть TODO функции, брать идеи и
уже подстраиваясь к архитектуре - делать решения и выполнять эти "Таски".

❤️ dev | Был растерзан и удален, теперь его ответственность расползлась по трем новым классам | Покойся с миром, и
спасибо мне же что вел списки задач беря на себя тройную нагрузку.

Commands:

    💚 RewatchCommand | Добавил возможность отключения;
    💚 InvisibilityCommand | Добавил возможность отключения;

    💚 ReportCommand | Добавил возможность отключения;
    💚 PlayerReportCommand | Добавил возможность отключения;

    💚 AdminBonusAllCommand | Добавил возможность отключения;
    💚 AdminBonusCommand | Добавил возможность отключения;

    💚 PlayerReportCommand | Добавил reportSended, отвечает за ответ игроку который отправил репорт

    💜 ReportCommand | Исправлена проблема с Array Strings, которую я упустил вчера;

    💜 PlayerReportCommand | Исправил баг с путем в конфиг, убрал старый legacy код из нового метода сендера 
    💜 PlayerReportCommand | Исправлена проблема с Array Strings, которую я упустил вчера;

GUI:
💜 ReportGUI | Поправил путь к итемсам в ReportItem

Service:

    💚 AdminStaffService | Новое условие: Если новый ранг администратора выше чем стаффа -> Ошибка.

    💜 AdminGameService | Исправил маленький баг массива данных с конфига, задумался на счет sendList(Player user, List<String> list) в SenderAPI

Sender:
💚 Новый метод парсинга parse(List<String> messages) для стринговых значений, очень поможет в будушем переходе на List
архитектуру сообщений.

Version 1.0 - Commit 1.12: api release, managers api && delete testers

ALL:
Commands, Service, Repo: Удалены все тестеры!

API:
❗💚 api/E1m0Admin | Был добавлен новый класс - E1m0Admin, это API связующее для сегодняшнего запроса на выпуск вместе с
деплойным Git Actions.

CORE:
💛 README.txt: Были подправлены и уточнены несколько моментов

Version 1.1 - Commit 1.1.1: API rework && Rework PLUGIN! ecosystem || MultiModule system.

❗ Теперь - ROOT - E1m0Root, Плагин - E1m0Admin, API - E1m0AdminAPI;

🧑‍💻 E1m0 | В E1m0AdminAPI я переименовал методы для выражения логики, то есть было AdminSystemRepositoryAPI стало -
systemStorage. Это важное упрощения для людей чтобы им было легче не запутатся в этом всем..

# 08.07.2025

🧑‍💻 E1m0 | Я не знаю, делаю мультимодульник 3-ий день, пересобираю проект уже в третий раз, я уже схожу с ума третье
утро.
🧑‍💻 E1m0 | Я наконец то смог сделать мультик, как же давно я этого не делал..

API:
💚 Модели State: Report && SecretCode | Успешно перенесены в API
💚 Модель Event: AdminAccessEvent | Успешно перенесен в API

Version 1.2 - Commit 1.2.1: Rework shared, publish system, push maven central

📊 E1m0 DevBlog || CheckPoints:

    🧑‍💻 E1m0: Делаю систему авто-публиша как на крупных проектах через Git Actions, Jenkins - трогать не стал, у них свободная экосистема и много заморочек для Maven Central.
    🧑‍💻 E1m0: Лицензию думал от Apache взять, но подумал что решение от мозилы будет более гибким для моего плагина, по этому взял MPL
    🧑‍💻 E1m0: Домен создан на централе, провожу последние доработки в централе и навожу марафет в плагине..

API:
💛 SenderAPI | Решил отказаться от системы Bukkit.Player и сделать всё, как во всех классах - через UUID, без огромных
интерфейсов и их зависимостей, а не то пожалею как с Report и другими обьектами.

❤️ SenderAPI && E1m0AdminAPI | Был полностью вырезан из публичного API, я его добавлял для себя больше, а потом подумал,
зачем он клиентам, только частые случаи и то это не ответственность плагина.

Utils:
🩷 E1m0Sender | Был переработан в связи изменения его API, так же были уточнены несколько моментов которые мне не сильно
нравились, переменные в общем, на глаз были не приятны, теперь - приведено к моему стандарту.

README:
💚 README.txt | Был допо лнен строчками о новом CENTRAL API!

Project:

    ❤️ pom.xml (API)    | Решил отказатся от зависимостей от Paper-API, Bukkit, и прочих систем связанных с зависимостями API, для него - нет ограничений.
    💚 pom.xml (Root)   | Добавлен менеджер плагинов зависимостей, рефакторинг для моих глаз, делаю упор в долговечность потому что заходить туда кроме как менять версию я уже не хочу, а если и зайду посмотреть сразу увижу что и где как лежит.
    💚 pom.xml (Plugin) | Перестроена система зависимосьей

    💚 pom.xml (Root) | Добавлны плагины на Secret
    💚 pom.xml (Root) | Добавлны плагины на Source
    💚 pom.xml (Root) | Добавлны плагины на Center

Version 2.0 - Commit 2.0.0: E1m0Sender modification && Structure rework

Structure:

    ❗ .e1m0dev/UPDATES | В проекте появилась новая ветка/файл. Старая ветка разработки и новый файл в ней.
        💚 UPDATES.md   | Я решил - что игроки, и пользователи, не должны видеть то что внутри, в общем по классам и так далее, потому что искать полезную информацию среди моих комментариев, фиксов, и прочего это конечно интересно но не дает результата прямо сейчас как любят многие, так что я записываю то что я хочу сделать и реализовываю из TODO.md -> UPDATE.md. Процесс снимаю реконом и записываю в DEVLOG.md
            То есть - идет цепь. Идея попадает из головы/комментариев/разного рода источников в TODO, затем когда я захочу ее реализовать она переходит в UPDATE с моими комментариями, и процесс записывается фактом в DEVLOG
    
    ❗ .e1m0dev/ -> e1m0dev/ | Я привык листать вниз для нужных мне файлов, а список всегда наверху, по этому я в угоду своей привычки решил переименовать папку, все равно ее никто читать не будет.

Commands:

    💚 ConsoleUpAdminCommand   | Все отправленные сообщения для консоли - были заменены новым методом sendConsole из обновленного E1m0Sender  + Новая зависимость по композиции E1m0Sender;
    💚 ConsoleDelAdminCommand  | Все отправленные сообщения для консоли - были заменены новым методом sendConsole из обновленного E1m0Sender + Новая зависимость по композиции E1m0Sender;
    💚 ConsoleSetAdminCommand  | Все отправленные сообщения для консоли - были заменены новым методом sendConsole из обновленного E1m0Sender  + Новая зависимость по композиции E1m0Sender;
    💚 ConsoleSetSecretCommand | Все отправленные сообщения для консоли - были заменены новым методом sendConsole из обновленного E1m0Sender  + Новая зависимость по композиции E1m0Sender;
    💚 ConsoleDownAdminCommand | Все отправленные сообщения для консоли - были заменены новым методом sendConsole из обновленного E1m0Sender  + Новая зависимость по композиции E1m0Sender;

    🩷 ConsoleDelAdminCommand | Небольшое ревью для глаз, потому что команда, а точнее слой самого вызова должен быть чист как слеза младенца.

Service:
❤️ AdminGameService | Удален маленький комментарий: // Перенести в дамп E1m0 34 строка, так как был выполнен - 💚 В
прошлых обновлениях

Config:

    💚 config.yml/Messages/Console        | Был добавлен псевдо-отдел CONSOLE;
    💚 config.yml/Messages/Errors/Console | Был добавлен псевдо-отдел CONSOLE;

    🧑‍💻 E1m0: Почему в Errors с начала глагол а только потом уточнения? - Потому что это ошибка, а вот в факте successful - я фиксирую сам факт: successful, у кого? - Admin, и только потом: действие - Delete, факт от ошибки, отличается тем, что один проходит и закрепляется а другой проходит и умирает.  
    💚 config.yml/Messages/Console | Было добавлено новое сообщение: successfulAdminDelete - Отвечает за успешный лог о успехе действия!
    💚 config.yml/Messages/Console | Было добавлено новое сообщение: successfulAdminDown - Отвечает за успешный лог о успехе действия!
    💚 config.yml/Messages/Console | Было добавлено новое сообщение: successfulAdminSet - Отвечает за успешный лог о успехе действия!
    💚 config.yml/Messages/Console | Было добавлено новое сообщение: successfulAdminUp - Отвечает за успешный лог о успехе действия!

    💚 config.yml/Messages/Errors/Console/Up | Было добавлено новое сообщение: upAdminNotAdminError - Отвечает за ошибку условия: введенный user - не является администратором!
    
    💚 config.yml/Messages/Errors/Console/Set | Было добавлено новое сообщение: setAdminIsAdminError - Отвечает за ошибку условия: введенный user - УЖЕ явялется администратором!
    💚 config.yml/Messages/Errors/Console/Set | Было добавлено новое сообщение: setAdminWeightError  - Отвечает за ошибку условия: У введенного юзера не найден его вес в системе!

💚 config.yml | Были добавлены новые комментарии в отдел Messages.
💚 config.yml | Был добавлен пример разветвления на несколько сообщений в teleportToAZ;

.e1m0dev
💚 UPDATES.md | Было добавлено объявление о обновлении 2.0.
💚 UPDATES.md | Были добавлены строки об обновлении, мои комментарии да и в общем по делу.

💚 TODO.md | Новые идеи успешно загружены, забыл упомянуть идею про цвета которые хотел сделать. Я не стал оформлять
в 1.0, потому что люди сами это прекрасно делают, мне всегда приходилось как либо изменять конфиги других плагинов.

❤️ TODO.md | Был удален TODO: Сделать List message cfg для прямо дотошной кастомизации |
TODO: Перенос 2.0. Причина: Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Сделать отдельный list sender в 2.0 для крутой полной настройки |
TODO: Перенос 2.0. Причина: Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Сделать отдельные сообщения для console E1M0Sender вместо Bukkit.getLogger().warning |
TODO: Перенос 2.0 Причина: Выполнен - 💚.

💚 UPDATES.md -> UPDATES.txt | Был переведен в новый формат, так как мне очень не нравится форматирование строк в обычном
MD, это стандарт, но мне на него стандартизированно по

Utils
💚 E1m0Sender | Новый метод и новая реализация отправки сообщений для ТОЧНОГО обозначения, что это - консоль, а не тестер
и что это удалять не нужно, потому что комментарии не сильно останавливают не то что людей, а меня порой тоже, мелочь -
а приятно, затем с этим можно что то придумать, это как двойное дно, задел на будущее короче да

💛 E1m0Permission | Условие метода E1m0Permission - Немного переработаны/упрощены.
💛 E1m0Sender | Переработка логики: sendString && sendPath, теперь - можно отправлять и обычную строку и СТРОКИ, то есть
вместо одного сообщения:
e1m0Message: "Вы под наблюдением!"
Можно отправить/разветвить для заметности или красоты:
e1m0Message:
- "❗"
- "Вы под наблюдением!"
- "❗"

Version 2.0 - Commit 2.1.0: Console Commit (CLS + giveBonus) && New opportunities for rewatch

API:
💚 ConsoleServiceAPI | Были добавлены новые методы, такие как giveBonusConsole && giveBonusAllConsole, по сути: Копия
логики для Staff но в обертке API с большими возможностями.
💚 SecretCodeRepositoryAPI | Был добавлен новый метод для проверки: Есть ли у администратора SecretCode?

Commands:
💚 ReportCommand | Добавлено консольное логирование | CLS
💚 RewatchCommand | Добавлено консольное логирование | CLS
💚 InvisibilityCommand | Добавлено консольное логирование | CLS

💜 ReportCommand | Была чутка приведена под стандарт, да я знаю что command.getName()... - Ничего практически не дает,
это стабилизация состояния разработчика, в частности - моего. Потому что я бегаю по 70+ файлам и иногда теряюсь даже с
реконом.

💚 ConsoleGiveBonusAllCommand | Новая команда: cbonusall: Позволяет удаленно выдать бонус администраторам, например:
Скриптом.
💚 ConsoleGiveBonusCommand | Новая команда: cbonus: Позволяет удаленно выдать бонус администратору, например: С сайта.

Servcie:
💚 SecretCodeService | Добавлено консольное логирование | CLS.
💚 AdminGameService | В связи с обновлением рекона, были добавлены новые возможности из конфига а так

TabCompleter:
💚 MainTabCompleter: Команды cbonusall && cbonus были успешно добавлены в TabCompleter.

utils:
💚 E1m0Color | Был добавлен новый способ взаимодействия именно с String а не с Component что мешало работе. Метод:
parseLegacy создан в общем и для порта на более мелкие версии где нет Component.

E1m0Admin:
💚 E1m0Admin | В мейн добавлены новые команды как "cbonusall" && "cbonus" из классов: ConsoleGiveBonusAllCommand &&
ConsoleGiveBonusCommand.
💚 E1m0Admin | В мейн добавлены зависимости sender к командам из коммита 2.0.0 (consoleSender или же sendConsole).

resources:
💚 plugin.yml | Добавлены новые команды: "cbonusall" && "cbonus". В регистр команд плагина!
❤️ plugin.yml | Удалены usages потому что это уже выпуск, а не бета, коммиты кто и что делают команды - были сохранены
так что больше не нужны, я планирую добавить сценарии /ahelp.=

Config:
💚 config.yml/Messages/ConsoleLogs | Новые сообщения для логирования в консоль, полезная штука, реконструкции, логи,
возможность ветки в API и еще куча моментов развития.
💚 accessLog | Отвечает за отправку лога об

💚 config.yml/Settings/ | Новая настройка "вентиль": consoleLogActive. Отвечает за то: Будут ли логироватся сообщения в
консоль?
💚 config.yml/Settings/Dev | Новые настройки геометрической архитектуры Vector: rewatchVectorX, rewatchVectorY,
rewatchVectorZ, позволяющие - открывать новые возможности, если есть возможность адаптивности - ее можно развить.
Например: Для каждого администратора хранить состояние его рекона, чтобы его не телепортировало на одну точку, а
например я хочу отснять кадр повыше, нажимаю на например пробел - это регистрирует контроллер и перестраивает вектор
моей слежки, в правильных руках система еще лучше чем GM3.

💚 config.yml/Messages | Ревоки сообщений идут вместе с их цветами, расставил по значимости что то позитивное, успех -
&a, не успех/не удача - &c, важная информация/выделить на фоне? -> &6.
💛 config.yml/Messages | Реворки сообщений по большей степени, мелкие доработки и уточнения. Например:
secretCodeAccess: "Вы успешно авторизовались!" -> secretCodeAccess: "&cВы — успешно авторизовались под своим именем в
системе администрации!"
❤️ config.yml/Messages | Строка: upAdmin - Удалена, больше не используется, не адаптивна, проигрывает новым
successfulUpStaff && successfulUpAdmin.

💜 config.yml/Messages/Errors | Строка: lengthError: Была переработана, там используется /ahelp который еще не вышел.

e1m0dev:
❤️ TODO.md | Был удален TODO: Сделать GiveBonus для консоли/API | TODO: Перенос 2.0 | Причина: Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Сделать возможность менять код в команде set code если он есть | TODO: Перенос 2.0
Причина: Выполнен, но вроде еще давно когда я отказался от идеи changeCode, в угоду адаптивной системы setCode().
❤️ TODO.md | Был удален TODO: Сделать GiveBonusAll для консоли/API | TODO: Перенос 2.0 | Причина: Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Добавить адаптивности rewatch, vector через кфг | Причина: Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Переработать утилиту E1m0Color для легаси, компонентов и обычных текстов по типу меню |
TODO: Перенос 2.0 | Причина: Выполнен - 💚.

❤️ TODO.md | Был удален ?:  /ahelp | Добавить сценарии для администрации | Был перенесен в центр принятия решений:
E1M0DEV.md.

E1m0DEV:
* CLS - Console Log System - Коммит версии 2.0: 2.1.0 (Этот же коммит)

    ❓ | Почему в AccessCommand - Нет CLS, а под него задействован SecretCodeService? - Потому что лог это факт системы.
        Вот что может случиться с реконом? А что может случиться с ainv? А вот код - так как он многопереходное меню - 
        может не пройти по системе логики и вот тогда будет? Не очень, когда я посылаю ивент о AdminAccess в телеграм
        которого не было потому что условие не было пройдено, это может сломать не скок систему сколько представление.
  Надежность фактов - превыше всего, по этому я не ломаю систему, а делаю ее более глубокой для некоторых участков.

    ❓ | Но репорт ведь тоже ведет в меню, почему у него тоже CLS идет не в его сервисе? Потому что у кода - для защиты
        4 разных меню с разными состояниями которые НЕ знают друг о друге, и не должны знать, они - чистый UI, но! Так уж
        получилось что они еще и принимающие, по этому я записал факт о том что администратор ОТКРЫЛ меню а не ответил
        на репорт, с таким то таким то ответом.

Version 2.0 - Commit 2.2.0: New more command add && happy birthday

Happy birthday to me | Праздничный коммит в мое день рождение 🩷🥳
https://hips.hearstapps.com/hmg-prod/images/years-old-birthday-cake-to-old-woman-royalty-free-image-1718042584.jpg?crop=0.668xw:1.00xh;0.178xw,0&resize=1200

API:
💚 AdminsStaffServiceAPI | Была добавлена новая возможность/метод: adminUnBanSystem. Это реализация к новой команде
/aunban.

💚 GameServiceAPI | Была добавлена новая возможность/метод: adminBlockAccess. Это реализация к новой команде /aban.
💚 GameServiceAPI | Была добавлена новая возможность/метод: getAdminList. Это реализация к новой команде /admins.
💚 GameServiceAPI | Была добавлена новая возможность/метод: adminHelp. Это реализация к новой команде /ahelp.

💚 GameServiceAPI | Был добавлен новый псевдо отсек: PLAYER LAYER. Здесь логика Игрок - Админ, все как с репортами, пока
что - принято решение не делать отдельный PlayerService, слишком мало взаимодейсвтий чтобы раздувать систему как с
SecretCode или Report.
💚 GameServiceAPI | Был добавлен новый метод в //PLAYER LAYER -> void getAdminList(UUID playerID); | Отвечает за показ
сразу двух состояний администрации, в игре и на посту, удобная штука.

Commands
💚 AdminUnBanCommand | Новая команда! /aunban вернуть человеку доступ из админ блока сервера;
💚 ABlockCommand | Новая команда! /admins позволяет просмотреть список администрации в сети в разных состояниях;
💚 AdminsCommand | Новая команда! /ablock позволяет забанить доступ администратору в случае слива и ЧС;
💚 AHelpCommand | Новая команда! /ahelp позволяет посмотреть сводку по серверу для администратора;

Service
💚 AdminsStaffService | Новая реализация добавлена в класс с его новым adminUnBanSystem(UUID adminID, UUID staffID);

💚 AdminsStaffService | Новая система в метод: upStatus(UUID adminID, UUID staffId)! Был добавлен триггер при сливе адм!
Если администратор попытается повысить старшего - бан админки.
💚 AdminsStaffService | Новая система в метод: downStatus(UUID adminID, UUID staffID)! Был добавлен триггер при сливе
адм! Если администратор попытается понизить старшего - бан админки.
💚 AdminsStaffService | Новая система в метод: deleteAdmin(UUID adminID, UUID staffID, String reason)! Был добавлен
триггер при сливе адм! Если администратор попытается снять старшего - бан админки.
💚 AdminsStaffService | Новая система в метод: setSecretPassword(UUID adminID, UUID staffID, int code)! Был добавлен
триггер при сливе адм, а так же небольшие доработки. Если администратор попытается скинуть пароль старшего - бан
админки.

💛 AdminsStaffService | Небольшая правка: upStatus(UUID adminID, UUID staffId) -> upStatus(UUID adminID, UUID staffID)
💛 AdminsStaffService | Небольшая правка: downStatus(UUID adminID, UUID staffId) -> downStatus(UUID adminID, UUID
staffID)

💚 AdminGameService | Была добавлена первая реализация getAdminList, я старался чтобы все не скатилось в for(for(for()))
но получилось как получилось, позже придумаю что-либо лучше.
💚 AdminGameService | Была добавлена реализация adminHelp.
💚 AdminGameService | Была добавлена реализация ablock.

State:
💚 SecretCodeManager | Было добавлено новое обновление: HashSet<UUID> blockedAdmins; Это - защита от попытки
проникновения на аккаунт.
💚 SecretCodeManager | blockedAdmins и его методы isBlocked, addBlockAdmin, removeBlockAdmin - Это защита в первую
очередь администрации и игроков проекта от сливов, любой администратор может путем смерти свой админки отобрать доступ у
другого если заметит слив.

TabCompleter:
💚 MainTabCompleter | Добавлен новый метод и переработаны некоторые свитчи: getOnlineAdmins, отделяет рабочий класс от
обычных игроков. Так мне по крайней мере легче ориентироваться, используется в таких местах как например setsecret или
aup и так далее.

💚 MainTabCompleter | Добавлена обработка-стоппер команды admins;
💚 MainTabCompleter | Добавлена обработка-стоппер команды ahelp;

💚 MainTabCompleter | Добавлена обработка команды aunban;
💚 MainTabCompleter | Добавлена обработка команды aban;

Utils:
💚 E1m0Permission | Переработка логики и добавление новых зависимостей при проверке! Была добавлена зашита от участников
блокировки (/aban)

💚 E1m0Sender | Поставил проверки на null в sendPath && sendString (Консоль - может быть null, но не в этом случае),
когда хотел сделать отправку сообщений и потом подумал что это будет чуть долговато и за это время админ может выйти. И
хотел сделать прооверку прямо в сервисе но вспомнил что у меня есть ведь спец утилита.

💛 E1m0Sender | sendString -> sendStringList. Потому что я заметил логическую не исправность когда использовал sendString
ahelp, оказывается я заранее подумал о листах и сделал обработку о чем сам же и забыл 😛

E1m0Admin:
💚 E1m0Admin | DI: gameService = new AdminGameService - Была добавлена новая зависимость от E1m0Permission в связи с
новым /admins.
💚 E1m0Admin | Зарегистрирована новая команда /admins;
💚 E1m0Admin | Зарегистрирована новая команда /aunban;
💚 E1m0Admin | Зарегистрирована новая команда /ahelp;
💚 E1m0Admin | Зарегистрирована новая команда /aban;

resources:
💚 plugin.yml: Добавлена в плагин новая команда /admins;
💚 plugin.yml: Добавлена в плагин новая команда /aunban;
💚 plugin.yml: Добавлена в плагин новая команда /ahelp;
💚 plugin.yml: Добавлена в плагин новая команда /aban;

Config:
💚 config.yml/Messages/Errors/notAdmin | Новое сообщение для администратора использующего игрока который не является
администратором, было добавлено вместо с /ablock
💚 config.yml/Messages/Errors/adminNotBanned | Новое сообщение для стаффа использующего администратора который не был в
/ablock.

💚 config.yml/Messages/unbannedSystem | Новое сообщение для админа которому вернули доступ к командам.
💚 config.yml/Messages/successfulUnbannedSystem | Новое сообщение для стаффа который вернул человеку доступ из /ablock.

💚 config.yml/Messages/successfulBlocked | Новое сообщение для администратора использующего /ablock на другог
администратора

💚 config.yml/Messages/Errors/adminBlockInternalError | Новое сообщение для консоли не в под-отделе действий консоли, это
именно ошибка на стороне логики и сервера, НЕ ЛОГ.
💚 config.yml/Messages/Errors/youAdminAccessIsBlocked | Новое сообщение для админа у которого была отобрана админка по
подозрению в сливе.

💚 config.yml/Server/aban | Вентиль для /ablock или /aban. Показывает: Будет ли вообще работать команда на этом сервере?
💚 config.yml/Permissions/aban | Разрешение для использования /aban, на некоторых серверах разрешения могут запрещать.

💚 config.yml/Server/admins | Вентиль для AdminList. Показывает: Будет ли вообще работать команда на этом сервере?
💚 config.yml/Permissions/admins | Разрешение для использования admins, на некоторых серверах разрешения могут запрещать
или продавать в донате.

💚 config.yml/Admin/AdminList | Была добавлена новая настройка в связи с добавлением новой команды /admins. Отвечает за
внешний вид самого сообщения Admins.

e1m0dev:
💚 E1M0DEV.md | Принято решение по /ahelp. Статус - Выполнен 💚.

❤️ TODO.md | Был удален TODO: Сделать команду /admins для просмотра администраторов | TODO: Перенос 2.0 / ❗ | Причина:
Выполнен - 💚.
❤️ TODO.md | Был удален TODO: Добавить команду /ahelp, выбрать меню или чат | TODO: Перенос 2.0 / ❗ | Причина:
Выполнен - 💚.