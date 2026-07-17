package tvgirl.elmodev.e1m0Admin.service;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tvgirl.elmodev.e1m0Admin.E1m0Admin;
import tvgirl.elmodev.e1m0admin.api.service.SystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSession;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.state.report.Report;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdminSystemService implements SystemServiceAPI {

    private final ReportSystemRepository reportRepository;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final AdminSessionManager sessionManager;

    private final Map<UUID, Report> playerReportCache;

    private final FileConfiguration cfg;
    private final E1m0Sender sender;
    private final E1m0Admin plugin;

    private E1m0Color c = new E1m0Color();

    public AdminSystemService(ReportSystemRepository reportRepository, AdminSessionManager sessionManager, AdminSystemRepository systemRepository, AdminStaffRepository staffRepository, Map<UUID, Report> playerReportCache, FileConfiguration cfg, E1m0Sender sender, E1m0Admin plugin) {
        this.playerReportCache = playerReportCache;
        this.reportRepository = reportRepository;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.sessionManager = sessionManager;
        this.sender = sender;
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @Override
    public void adminPay() {
        // Получаю секции из конфига.
        ConfigurationSection sectionSalary = cfg.getConfigurationSection("Admin.AdminRanks");
        ConfigurationSection sectionBonus = cfg.getConfigurationSection("Admin.Salary.SalaryActions");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.getBoolean("Server.adminPay")) return;

                for (AdminSession aSession : sessionManager.getSessions()) {
                    Player adm = Bukkit.getPlayer(aSession.getUuid());

                    // Проверяю: A) Время нажитое на сервере. B) Какой это час?
                    long onlineTime = System.currentTimeMillis() - aSession.getJoinTime();
                    long minutes = onlineTime / 60000;

                    if (minutes <= aSession.getWorkedMinutes()) {
                        continue;
                    }

                    // Если час превышает тот что был в State = значит он новый.
                    if (minutes > aSession.getWorkedMinutes()) {
                        aSession.plusWorkedMinutes();

                        // Игрок все еще жив и на сервере?
                        if (adm == null) {
                            continue;
                        }

                        for (String key : sectionSalary.getKeys(false)) {
                            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                            int adminWeight = aSession.getAdminWeight();

                            // Если не подошел по prefix = скип.
                            if (cfgWeight != adminWeight)
                                continue;

                            // Получаю зарплату
                            int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                            String salaryString = String.valueOf(salary);


                            // Вывожу сообщение + выдаю ему честно нажитое.
                            sender.sendPath(adm, "Messages.adminPay",
                                    "%salary", String.valueOf(salary));

                            // Достаю его зарплату из конфига по патчу
                            String str = cfg.getString("Admin.AdminRanks." + key + ".scom")
                                    .replace("%player", adm.getName())
                                    .replace("%salary", salaryString);

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
                        }

                        // Если админ бонус на сервере выключен - скип.
                        if (!cfg.getBoolean("Server.adminBonus")) continue;
                        for (String key : sectionBonus.getKeys(false)) {

                            // Получаю часы и зарплату игрока из его таблицы + state session.
                            int hour = cfg.getInt("Admin.Salary.SalaryActions." + key + ".minutes");
                            int salary = aSession.getAdminSalary();

                            if (hour == aSession.getWorkedMinutes()) {
                                // Если часы совпадают - делаем определенные действия
                                for (String s : cfg.getStringList("Admin.Salary.SalaryActions." + key + ".Actions")) {
                                    String str = s
                                            .replace("%player", adm.getName())
                                            .replace("%salary", String.valueOf(salary));

                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20 * 60 * cfg.getLong("Settings.Dev.salaryCheck"), 20 * 60 * cfg.getLong("Settings.Dev.salaryCheck"));
    }

    @Override
    public void autoLeakActions(UUID adminID, UUID staffID) {
        // 1 - | Проверки.
        boolean leakActionsIsEnable = cfg.getBoolean("Admin.Leak.enable");
        if (!leakActionsIsEnable) return;

        // 2 - | Инициализация.
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        // 3 - | Проверки и подготовка.
        String cfgSound = cfg.getString("Admin.Leak.LeakSound");
        Sound sound = Sound.valueOf(cfgSound);

        if (sound == null) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.leakSoundIsNotFound");
            return;
        }

        List<String> leakActions = cfg.getStringList("Admin.Leak.LeakActions");

        // ❗ - | Исполнение.
        Bukkit.getOnlinePlayers().stream()
                .filter(adm -> adm.hasPermission(cfg.getString("Permissions.admin")))
                .forEach(adm -> adm.playSound(adm.getLocation(), sound, 1.0f, 1.0f));

        leakActions.stream()
                .map(str -> str
                        .replace("%leak", staff.getName()
                                .replace("%admin", admin.getName())))

                .forEach(perm -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), perm));
    }

    @Override
    public void autoComplimentActions(UUID adminID) {
        boolean isWork = cfg.getBoolean("Admin.Compliment.enable");
        if (!isWork) return;


        Player admin = Bukkit.getPlayer(adminID);
        List<String> runActions = cfg.getStringList("Admin.Compliment.Actions");

        runActions
                .stream()
                .map(string -> string.replace("%admin", admin.getName()))
                .map(placeholder -> PlaceholderAPI.setPlaceholders(admin, placeholder))
                .forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
    }

    @Override
    public void autoSetAdmin(UUID adminID, UUID staffID, int weight) {
        // 1 - | Проверки.
        boolean permissionIsEnable = cfg.getBoolean("Admin.AutoSetAdmin.autoSetPermissions.enable");
        if (!permissionIsEnable) return;

        boolean skinsIsEnable = cfg.getBoolean("Admin.AutoSetAdmin.autoSetSkins.enable");
        if (!skinsIsEnable) return;

        // 2 - | Инициализация.
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        ConfigurationSection skinsSections = cfg.getConfigurationSection("Admin.AutoSetAdmin.autoSetSkins.skinsInWeight");
        ConfigurationSection permissionSections = cfg.getConfigurationSection("Admin.AutoSetAdmin.autoSetPermissions.permissionInWeight");

        /* ❗ | PERMISSIONS */
        for (String s : permissionSections.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AutoSetAdmin.autoSetPermissions.permissionInWeight." + s);
            if (weight != cfgWeight) continue;

            // 3 - | Проверки и подготовка.
            List<String> permissions = cfg.getStringList("Admin.AutoSetAdmin.autoSetPermissions.permissionInWeight." + s + ".permissions");

            // ❗ - | Исполнение.
            permissions.stream()
                    .map(str -> str.replace("%admin", admin.getName()))
                    .map(placeholder -> PlaceholderAPI.setPlaceholders(admin, placeholder))
                    .forEach(perm -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), perm));
        }

        /* ❗ | SKINS */
        for (String s : permissionSections.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AutoSetAdmin.autoSetSkins.skinsInWeight." + s);
            if (weight != cfgWeight) continue;

            // 3 - | Проверки и подготовка.
            List<String> skins = cfg.getStringList("Admin.AutoSetAdmin.autoSetSkins.skinsInWeight." + s + ".skins");

            // ❗ - | Исполнение.
            skins.stream()
                    .map(str -> str.replace("%admin", admin.getName()))
                    .map(placeholder -> PlaceholderAPI.setPlaceholders(admin, placeholder))
                    .forEach(skin -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), skin));
        }
    }

    @Override
    public void autoDelAdmin(UUID adminID, UUID staffID) {
        // 1 - | Проверки.
        boolean permissionIsEnable = cfg.getBoolean("Admin.AutoDelAdmin.autoDelPermissions.enable");
        if (!permissionIsEnable) return;

        boolean skinsIsEnable = cfg.getBoolean("Admin.AutoDelAdmin.autoDelSkins.enable");
        if (!skinsIsEnable) return;

        // 2 - | Инициализация.
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        ConfigurationSection skinsSections = cfg.getConfigurationSection("Admin.AutoDelAdmin.autoDelSkins.skinsInWeight");
        ConfigurationSection permissionSections = cfg.getConfigurationSection("Admin.AutoDelAdmin.autoDelPermissions.permissionInWeight");

        /* ❗ | PERMISSIONS */
        for (String s : permissionSections.getKeys(false)) {
            // 3 - | Подготовка.
            List<String> permissions = cfg.getStringList("Admin.AutoDelAdmin.autoDelPermissions.permissionInWeight." + s + ".permissions");

            // ❗ - | Исполнение.
            permissions.stream()
                    .map(str -> str.replace("%admin", admin.getName()))
                    .map(placeholder -> PlaceholderAPI.setPlaceholders(admin, placeholder))
                    .forEach(perm -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), perm));
        }

        /* ❗ | SKINS */
        for (String s : permissionSections.getKeys(false)) {
            // 3 - | Подготовка.
            List<String> skins = cfg.getStringList("Admin.AutoDelAdmin.autoDelSkins.skinsInWeight." + s + ".skins");

            // ❗ - | Исполнение.
            skins.stream()
                    .map(str -> str.replace("%admin", admin.getName()))
                    .map(placeholder -> PlaceholderAPI.setPlaceholders(admin, placeholder))
                    .forEach(skin -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), skin));
        }
    }

    @Override
    public void handleReportAccept(UUID adminID, UUID reportID) {

        for (Map.Entry<UUID, Report> reportKey : playerReportCache.entrySet()) {

            if (reportKey.getValue().getUuid() == reportID) {
                Report report = reportKey.getValue();

                Player admin = Bukkit.getPlayer(adminID);
                Player player = Bukkit.getPlayer(report.getPlayerID());
                String response = cfg.getString("Admin.Report.fastTakeReportMessage");

                Report newReport = new Report(
                        report.getUuid(),
                        adminID,
                        report.getPlayerID(),
                        admin.getName(),
                        player.getName(),
                        report.getReport(),
                        response,
                        report.getStatus(),
                        report.getCreatedAt()
                );

                sender.sendPath(admin, "Messages.reportTake",
                        "%content", report.getReport(),
                        "%player", report.getPlayerNick());

                sender.sendPath(player, cfg.getString("Messages.reportMessagePlayerFast"),
                        "%content", report.getReport(),
                        "%admin", admin.getName());

                reportRepository.gameReportSend(newReport);
                playerReportCache.remove(report.getPlayerID());
            } else {
                Bukkit.getLogger().warning("Репорт: " + reportID + " НЕ ДЕЙСТВИТЕЛЕН, ОПАСНАЯ НЕ ОПРЕДЕЛЕННОСТЬ!"); // ЛОГ.

                for (Map.Entry<UUID, Report> reportKeyOff : playerReportCache.entrySet()) {
                    if (reportKey.getValue().getUuid() == reportID) {
                        Report report = reportKey.getValue();
                        playerReportCache.remove(report.getPlayerID());
                    }
                }
            }
        }
    }
}