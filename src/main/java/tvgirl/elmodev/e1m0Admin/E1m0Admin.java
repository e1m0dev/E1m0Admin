package tvgirl.elmodev.e1m0Admin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tvgirl.elmodev.e1m0Admin.commands.admin.InvisibilityCommand;
import tvgirl.elmodev.e1m0Admin.commands.admin.RewatchCommand;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class E1m0Admin extends JavaPlugin {

    // PLUGIN MEMORY

    // 🚨 | REPORT MEMORY
    private Map<UUID, UUID> reportPlayer = new HashMap<>();

    // 🔐 | CODE MEMORY
    private HashMap<UUID, Byte> stepOne = new HashMap<>();
    private HashMap<UUID, Byte> stepTwo = new HashMap<>();
    private HashMap<UUID, Byte> stepThree = new HashMap<>();
    private HashMap<UUID, Byte> stepFours = new HashMap<>();

    /* DATABASE */
    private DatabaseSource databaseSource;
    private DatabaseManager databaseManager;

    /* State */
    private AdminSessionManager sessionManager;

    /* Repository */
    private AdminSystemRepository systemRepository;
    private AdminStaffRepository staffRepository;

    /* Service */
    private AdminSystemService systemService;
    private AdminsStaffService staffService;
    private AdminGameService gameService;

    /* Manager */
    private final PluginManager lManager = Bukkit.getPluginManager();

    @Override
    public void onEnable() {
        bootstrap();
    }

    @Override
    public void onDisable() {
        // TODO: Закрыть остальные репорты ❗

        databaseSource.shutdown();
    }

    private void bootstrap() {
        // 📋 | Config
        saveDefaultConfig();
        getConfig().options().copyDefaults();

        // ⚙️ | Database
        databaseSource = new DatabaseSource(getConfig());
        databaseManager = new DatabaseManager(databaseSource.getSource(), this);

        // ♾️ | State
        sessionManager = new AdminSessionManager(systemRepository);

        // 📊 | Repository
        systemRepository = new AdminSystemRepository(databaseManager.getJdbi());
        staffRepository = new AdminStaffRepository(databaseManager.getJdbi());

        // 🧑‍🔬 | Service
        systemService = new AdminSystemService(sessionManager, systemRepository, staffRepository, getConfig(), this);
        staffService = new AdminsStaffService(staffRepository, systemRepository, getConfig());
        gameService = new AdminGameService(getConfig(), this);

        // 🗣️ | Listeners
        lManager.registerEvents(new JoinListener(getConfig(), sessionManager), this);

        // 🤖 | Commands
        getCommand("einv").setExecutor(new InvisibilityCommand(getConfig(), gameService));
        getCommand("rewatch").setExecutor(new RewatchCommand(getConfig(), gameService));
        getCommand("reoff").setExecutor(new RewatchCommand(getConfig(), gameService));

        // ❓ | Tab-Comp.
        getCommand("einv").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("rewatch").setTabCompleter(new MainTabCompleter(getConfig()));
        getCommand("reoff").setTabCompleter(new MainTabCompleter(getConfig()));

        // ⌛ | Database INIT:
        databaseManager.initDatabase(databaseSource.getSource());

        // 💳 | AdminPay
        systemService.adminPay();
    }
}
