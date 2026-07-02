package tvgirl.elmodev.e1m0Admin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import tvgirl.elmodev.e1m0Admin.commands.admin.AccessCommand;
import tvgirl.elmodev.e1m0Admin.commands.admin.InvisibilityCommand;
import tvgirl.elmodev.e1m0Admin.commands.admin.ReportCommand;
import tvgirl.elmodev.e1m0Admin.commands.admin.RewatchCommand;
import tvgirl.elmodev.e1m0Admin.commands.console.*;
import tvgirl.elmodev.e1m0Admin.commands.player.PlayerReportCommand;
import tvgirl.elmodev.e1m0Admin.commands.staff.*;
import tvgirl.elmodev.e1m0Admin.gui.controller.report.ReportController;
import tvgirl.elmodev.e1m0Admin.gui.controller.secretcode.SecretCodeController;
import tvgirl.elmodev.e1m0Admin.gui.guis.report.ReportGUI;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.listeners.bukkit.QuitListener;
import tvgirl.elmodev.e1m0Admin.listeners.e1m0.AdminAccessListener;
import tvgirl.elmodev.e1m0Admin.repository.AdminGameRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.service.gui.ReportSystemService;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;
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
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public final class E1m0Admin extends JavaPlugin {

    // | ❗ PLUGIN MEMORY:

    // 🚨 | REPORT MEMORY
    private HashMap<UUID, Report> playerReportCache = new HashMap<>(); // Игроки с их UUID репорта.

    // 🧑‍💻 | SESSION MEMORY
    private HashMap<UUID, AdminSession> sessionsCache = new HashMap<>(); // Сессии кэша админов

    // 🌐 | CODE MEMORY
    private HashMap<UUID, SecretCodeState> codeCache = new HashMap<>(); // Кэширование игроков с кодом для utils

    // 🪼 | Invise Memory
    private HashSet<UUID> inviseCache = new HashSet<>(); // Администраторы в инвизе;

    // 🧑‍💻 |  RECON MEMORY
    private HashMap<UUID, UUID> reconCache = new HashMap<>(); // Администраторы в рекона;
    private HashMap<UUID, BukkitTask> rewatchTasksCache = new HashMap<>(); // Таски для переноса и защитки;

    // 🌐 | NAMESPACED KEY
    private NamespacedKey reportActions = new NamespacedKey(this, "reportActions");
    private NamespacedKey reportKey = new NamespacedKey(this, "reportKey");

    private NamespacedKey secretKey = new NamespacedKey(this, "secretActions");

    // BOOT:

    /* SENDER */
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

    @Override
    public void onEnable() {
        bootstrap();
    }

    @Override
    public void onDisable() {
        databaseSource.shutdown();
    }

    private void bootstrap() {

        // 📋 | Config
        saveDefaultConfig();
        getConfig().options().copyDefaults();

        // 💬 | Sender
        sender = new E1m0Sender(getConfig());

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
        secretCodeManager = new SecretCodeManager(codeCache);
        sessionManager = new AdminSessionManager(systemRepository, sessionsCache);

        // 🧑‍🔬 | Service
        consoleService = new ConsoleService(secretCodeRepository, systemRepository, staffRepository, getConfig(), sender);

        reportService = new ReportSystemService(sender, getConfig(), playerReportCache, reportSystemRepository);
        secretCodeService = new SecretCodeService(codeCache, secretCodeRepository, permissionManager, secretCodeManager, getConfig(), sender);

        systemService = new AdminSystemService(reportSystemRepository, sessionManager, systemRepository, staffRepository, playerReportCache, getConfig(), sender, this);
        staffService = new AdminsStaffService(secretCodeRepository, staffRepository, systemRepository, getConfig(), sender);
        gameService = new AdminGameService(reportSystemRepository, gameRepository, secretCodeGui, inviseCache, rewatchTasksCache, playerReportCache, reconCache, getConfig(), reportGui, sender, this);

        // ⌚ | Permissions
        permissionManager = new E1m0Permission(systemRepository, secretCodeManager, getConfig());

        // - | E1m0
        lManager.registerEvents(new AdminAccessListener(sender, getConfig()), this);

        // 🌐 | GUI
        secretCodeGui = new SecretCodeGui(secretCodeService, secretKey, getConfig());
        reportGui = new ReportGUI(playerReportCache, reportKey, reportActions, reportService, getConfig(), this);

        // 🗣️ | Listeners
        lManager.registerEvents(new JoinListener(getConfig(), sessionManager), this);
        lManager.registerEvents(new QuitListener(getConfig(), systemService, sessionManager, secretCodeManager, playerReportCache), this);

        // Controllers
        secretCodeController = new SecretCodeController(secretCodeGui, secretKey, secretCodeService);
        lManager.registerEvents(secretCodeController, this);

        reportController = new ReportController(reportKey, reportActions, reportService);
        lManager.registerEvents(reportController, this);

        // 🤖 | Commands
        // - | Admin
        getCommand("rep").setExecutor(new ReportCommand(sender, reportGui, getConfig(), gameService, permissionManager));
        getCommand("aaccess").setExecutor(new AccessCommand(sender, getConfig(), gameService, secretCodeGui, permissionManager));
        getCommand("ainv").setExecutor(new InvisibilityCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("reoff").setExecutor(new RewatchCommand(sender, getConfig(), gameService, permissionManager));
        getCommand("re").setExecutor(new RewatchCommand(sender, getConfig(), gameService, permissionManager));

        // - | Player
        getCommand("report").setExecutor(new PlayerReportCommand(sender, getConfig(), gameService, playerReportCache));

        // - | Staff
        getCommand("adown").setExecutor(new AdminDownCommand(sender, getConfig(), staffService, permissionManager, systemRepository));
        getCommand("abonusall").setExecutor(new AdminBonusAllCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("asecret").setExecutor(new AdminSetSecretCode(sender, getConfig(), staffService, permissionManager));
        getCommand("abonus").setExecutor(new AdminBonusCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("adel").setExecutor(new AdminDeleteCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("aset").setExecutor(new AdminSetCommand(sender, getConfig(), staffService, permissionManager));
        getCommand("aup").setExecutor(new AdminUpCommand(sender, getConfig(), staffService, permissionManager));

        // - | Console
        getCommand("cup").setExecutor(new ConsoleUpAdminCommand(getConfig(), consoleService));
        getCommand("cdel").setExecutor(new ConsoleDelAdminCommand(getConfig(), consoleService));
        getCommand("cdown").setExecutor(new ConsoleDownAdminCommand(getConfig(), consoleService));

        getCommand("csetadmin").setExecutor(new ConsoleSetAdminCommand(getConfig(), consoleService));
        getCommand("csetsecret").setExecutor(new ConsoleSetSecretCommand(getConfig(), consoleService));

        // ❓ | Tab-Completer.
        getCommand("re").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("rec").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("rep").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("ainv").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("recon").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("reoff").setTabCompleter(new MainTabCompleter(getConfig()));

        getCommand("report").setTabCompleter(new MainTabCompleter(getConfig()));

        getCommand("abonusall").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("setsecret").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("asecret").setTabCompleter(new MainTabCompleter(getConfig()));
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
}
