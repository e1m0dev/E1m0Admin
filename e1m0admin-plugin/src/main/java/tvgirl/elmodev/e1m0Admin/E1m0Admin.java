package tvgirl.elmodev.e1m0Admin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import tvgirl.elmodev.e1m0Admin.commands.admin.*;
import tvgirl.elmodev.e1m0Admin.commands.console.*;
import tvgirl.elmodev.e1m0Admin.commands.player.AdminsCommand;
import tvgirl.elmodev.e1m0Admin.commands.player.PlayerReportCommand;
import tvgirl.elmodev.e1m0Admin.commands.player.ThanksCommand;
import tvgirl.elmodev.e1m0Admin.commands.staff.*;
import tvgirl.elmodev.e1m0Admin.gui.controller.report.ReportController;
import tvgirl.elmodev.e1m0Admin.gui.controller.secretcode.SecretCodeController;
import tvgirl.elmodev.e1m0Admin.gui.guis.report.ReportGUI;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.listeners.bukkit.QuitListener;
import tvgirl.elmodev.e1m0Admin.listeners.e1m0.*;
import tvgirl.elmodev.e1m0Admin.repository.AdminGameRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.service.gui.ReportSystemService;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSession;
import tvgirl.elmodev.e1m0Admin.tabcompliter.MainTabCompleter;
import tvgirl.elmodev.e1m0Admin.database.DatabaseManager;
import tvgirl.elmodev.e1m0Admin.database.DatabaseSource;
import tvgirl.elmodev.e1m0Admin.listeners.bukkit.JoinListener;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.service.AdminsStaffService;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;
import tvgirl.elmodev.e1m0admin.api.state.report.Report;
import tvgirl.elmodev.e1m0admin.api.state.secretcode.SecretCodeState;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public final class E1m0Admin extends JavaPlugin {

    // | ❗ PLUGIN MEMORY:

    // 🚨 | REPORT MEMORY
    private HashMap<UUID, Report> playerReportCache = new HashMap<>(); // Игроки с их UUID репорта.

    // 🧑‍💻 | SESSION MEMORY
    private HashMap<UUID, AdminSession> sessionsCache = new HashMap<>(); // Сессии кэша админов

    // 🌐 | CODE MEMORY
    private HashSet<UUID> blockedAdmins = new HashSet<>(); // Кэширование заблокированных администраторов.
    private HashMap<UUID, Integer> attemptsInputCode = new HashMap<>(); // Отслеживание не верных попыток ввода секретного кода.
    private HashMap<UUID, SecretCodeState> codeCache = new HashMap<>(); // Кэширование игроков с кодом для utils.

    // 🪼 | Invise Memory
    private HashSet<UUID> inviseCache = new HashSet<>(); // Администраторы в инвизе;

    // 🧑‍💻 | RECON MEMORY
    private HashMap<UUID, UUID> reconCache = new HashMap<>(); // Администраторы в рекона;
    private HashMap<UUID, BukkitTask> rewatchTasksCache = new HashMap<>(); // Таски для переноса и защитки;

    // ✅ | THANKS MEMORY
    private HashSet<UUID> thanksPlayersCache = new HashSet<>(); // Игроки которые отправили похвалу администратору за помощь.

    // 🌐 | NAMESPACED KEY
    private NamespacedKey reportActions = new NamespacedKey(this, "reportActions");
    private NamespacedKey reportKey = new NamespacedKey(this, "reportKey");

    private NamespacedKey secretKey = new NamespacedKey(this, "secretActions");

    // BOOT:

    /* UTILS */
    private E1m0Color color;
    private E1m0Sender sender;

    /* DATABASE */
    private DatabaseSource databaseSource;
    private DatabaseManager databaseManager;

    /* State */
    private SecretCodeManager secretCodeManager;
    private AdminSessionManager sessionManager;

    /* Repository */
    private ReportSystemRepository reportSystemRepository;
    private SecretCodeRepository secretCodeRepository;

    private AdminSystemRepository systemRepository;
    private AdminStaffRepository staffRepository;
    private AdminGameRepository gameRepository;

    /* Service */
    private ConsoleService consoleService;

    private ReportSystemService reportService;
    private SecretCodeService secretCodeService;

    private AdminSystemService systemService;
    private AdminsStaffService staffService;
    private AdminGameService gameService;

    /* GUI */
    private SecretCodeGui secretCodeGui;
    private ReportGUI reportGui;

    /* GUI CONTROLLERS */
    private SecretCodeController secretCodeController;
    private ReportController reportController;

    /* Manager */
    private PluginManager lManager = Bukkit.getPluginManager();

    /* Manager */
    private E1m0Permission permissionManager;

    // | 💬 CONFIGS:
    private FileConfiguration messageConfig;
    private File messages;


    @Override
    public void onEnable() {
        bootstrap();
    }

    @Override
    public void onDisable() {
        if (databaseSource != null) {
            databaseSource.shutdown();
        }
    }

    private void bootstrap() {

        // 📋 | Config
        saveDefaultConfig();
        createMessagesConfig();
        getConfig().options().copyDefaults();

        // 💬 | UTILS
        sender = new E1m0Sender(getConfig(), messageConfig);
        color = new E1m0Color();

        // ⚙️ | Database
        databaseSource = new DatabaseSource(getConfig());
        databaseSource.init();

        databaseManager = new DatabaseManager(databaseSource.getSource(), this);
        databaseManager.initDatabase(databaseSource.getSource());

        // 📊 | Repository
        reportSystemRepository = new ReportSystemRepository(databaseManager.getJdbi());
        secretCodeRepository = new SecretCodeRepository(databaseManager.getJdbi());

        systemRepository = new AdminSystemRepository(databaseManager.getJdbi());
        staffRepository = new AdminStaffRepository(databaseManager.getJdbi());
        gameRepository = new AdminGameRepository(databaseManager.getJdbi());

        // ♾️ | State
        secretCodeManager = new SecretCodeManager(codeCache, blockedAdmins);
        sessionManager = new AdminSessionManager(systemRepository, sessionsCache);

        // ⌚ | Permissions
        permissionManager = new E1m0Permission(systemRepository, secretCodeManager, blockedAdmins, getConfig(), sender);

        // 🧑‍🔬 | Service
        consoleService = new ConsoleService(secretCodeRepository, sessionManager, systemRepository, staffRepository, secretCodeManager, getConfig(), sender);

        reportService = new ReportSystemService(sender, getConfig(), playerReportCache, staffRepository, reportSystemRepository);
        secretCodeService = new SecretCodeService(codeCache, secretCodeRepository, attemptsInputCode, secretCodeManager, permissionManager, getConfig(), sender);

        systemService = new AdminSystemService(reportSystemRepository, sessionManager, systemRepository, staffRepository, playerReportCache, getConfig(), sender, this);
        staffService = new AdminsStaffService(secretCodeRepository, sessionManager, staffRepository, systemRepository, secretCodeManager, getConfig(), sender);
        gameService = new AdminGameService(reportSystemRepository, staffRepository, gameRepository, thanksPlayersCache, secretCodeGui, color, inviseCache, rewatchTasksCache, playerReportCache, reconCache, getConfig(), permissionManager, reportGui, sender, this);

        // 🌐 | GUI
        secretCodeGui = new SecretCodeGui(secretCodeService, secretKey, getConfig(), sender, color);
        reportGui = new ReportGUI(playerReportCache, reportKey, reportActions, reportService, getConfig(), this, sender);

        // - | Listeners - E1m0
        lManager.registerEvents(new AdminAccessListener(sender, getConfig()), this);

        lManager.registerEvents(new AdminLeakListener(sender, getConfig(), messageConfig, systemService, secretCodeManager), this);

        lManager.registerEvents(new AdminComplimentListener(sender, getConfig(), systemService), this);

        lManager.registerEvents(new AdminSetListener(sender, getConfig(), systemService), this);
        lManager.registerEvents(new AdminDelListener(sender, getConfig(), systemService), this);

        // 🗣️ | Listeners - Bukkit
        lManager.registerEvents(new JoinListener(sender, getConfig(), sessionManager), this);
        lManager.registerEvents(new QuitListener(getConfig(), systemService, sessionManager, secretCodeManager, playerReportCache), this);

        // Controllers
        secretCodeController = new SecretCodeController(secretCodeGui, secretKey, secretCodeService);
        lManager.registerEvents(secretCodeController, this);

        reportController = new ReportController(reportKey, reportActions, reportService);
        lManager.registerEvents(reportController, this);

        // 🤖 | Commands
        // - | Admin
        getCommand("aaccess").setExecutor(new AccessCommand(sender, getConfig(), secretCodeService, secretCodeGui, permissionManager));
        getCommand("arep").setExecutor(new ReportCommand(sender, reportGui, getConfig(), gameService, permissionManager));
        getCommand("rep").setExecutor(new ReportCommand(sender, reportGui, getConfig(), gameService, permissionManager));
        getCommand("apardon").setExecutor(new AdminUnBanCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("aunban").setExecutor(new AdminUnBanCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("ainv").setExecutor(new InvisibilityCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("reoff").setExecutor(new RewatchCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("are").setExecutor(new RewatchCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("re").setExecutor(new RewatchCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("aban").setExecutor(new ABlockCommand(sender, getConfig(), gameService));
        getCommand("ahelp").setExecutor(new AHelpCommand(sender, getConfig(), gameService));
        getCommand("aban").setExecutor(new ABlockCommand(sender, getConfig(), gameService));

        // - | Player
        getCommand("thanks").setExecutor(new ThanksCommand(sender, getConfig(), gameService));
        getCommand("report").setExecutor(new PlayerReportCommand(sender, getConfig(), gameService, playerReportCache));
        getCommand("admins").setExecutor(new AdminsCommand(sender, getConfig(), gameService));

        // - | Staff
        getCommand("adown").setExecutor(new AdminDownCommand(sender, getConfig(), staffService, permissionManager, systemRepository));
        getCommand("ablist").setExecutor(new AdminAddBlackListCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("abonusall").setExecutor(new AdminBonusAllCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("asecret").setExecutor(new AdminSetSecretCode(sender, getConfig(), staffService, permissionManager));
        getCommand("abonus").setExecutor(new AdminBonusCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("adel").setExecutor(new AdminDeleteCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("abdlist").setExecutor(new AdminUpCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("aset").setExecutor(new AdminSetCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("aup").setExecutor(new AdminUpCommand(sender, getConfig(), staffService, permissionManager));


        // - | Console
        getCommand("cdel").setExecutor(new ConsoleDelAdminCommand(sender, consoleService));
        getCommand("cban").setExecutor(new ConsoleBanAdminCommand(sender, consoleService));

        getCommand("cup").setExecutor(new ConsoleUpAdminCommand(sender, consoleService));
        getCommand("cset").setExecutor(new ConsoleUpAdminCommand(sender, consoleService));
        getCommand("cdown").setExecutor(new ConsoleDownAdminCommand(sender, consoleService));
        getCommand("cunban").setExecutor(new ConsoleUnBanAdminCommand(sender, consoleService));

        getCommand("cbonus").setExecutor(new ConsoleGiveBonusCommand(sender, consoleService));
        getCommand("cbonusall").setExecutor(new ConsoleGiveBonusCommand(sender, consoleService));

        getCommand("csetadmin").setExecutor(new ConsoleSetAdminCommand(sender, consoleService));
        getCommand("csetsecret").setExecutor(new ConsoleSetSecretCommand(sender, consoleService));

        // ❓ | Tab-Completer.
        getCommand("re").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("rec").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("rep").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("acc").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("arep").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("ainv").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("aban").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("ahelp").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("recon").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("reoff").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("aban").setTabCompleter(new MainTabCompleter(getConfig()));

        getCommand("thanks").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("report").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("admins").setTabCompleter(new MainTabCompleter(getConfig()));

        getCommand("abonusall").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("setsecret").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("asecret").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("abdlist").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("ablist").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("abonus").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("adown").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("adel").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("aset").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("aup").setTabCompleter(new MainTabCompleter(getConfig()));

        getCommand("cdel").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("cup").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("cdown").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("csetadmin").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("csetsecret").setTabCompleter(new MainTabCompleter(getConfig()));

        // 💳 | AdminPay
        systemService.adminPay();
    }

    private void createMessagesConfig() {
        String lang = getConfig().getString("Settings.mainLanguage");
        messages = new File(getDataFolder(), "lang/" + lang);

        if (messages.exists()) {
            Bukkit.getLogger().warning("NULL LANGUAGE IS SET SERVER | У ВАС НЕ УСТАНОВЛЕН ЯЗЫК!");
            Bukkit.getLogger().warning("config -> mainLanguage: ru_ru.yml");
            Bukkit.getLogger().warning("config -> mainLanguage: en_en.yml");
        } else {
            saveResource("lang/" + lang, false);
            Bukkit.getLogger().info("Конфиг установлен: " + lang);
            Bukkit.getLogger().info("Config has set: " + lang);
        }

        messageConfig = YamlConfiguration.loadConfiguration(messages);
    }

    public FileConfiguration getMessageConfig() {
        return messageConfig;
    }
}
