Commit Version 1.1 | PIN-Generation

Добавлена система PIN-кодов
Добавлены возможности для STAFF по поритету PIN

Добавлены огромные кол-ва точек входа, команд, работающих с SecretCode
Добавлена GUI работа с SecretCode и проверки доступа у администратора.

Переделана система приоритетов и permission связанная с PIN-Code
Добавлены новые State and StateManager для работы с Permission Manager
Переписана API PermissionManager под новый State

Commit Version 1.2 | Permission Review
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

1.3 Admin Optional && Player Structure