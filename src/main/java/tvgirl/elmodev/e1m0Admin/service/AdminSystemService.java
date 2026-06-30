package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tvgirl.elmodev.e1m0Admin.E1m0Admin;
import tvgirl.elmodev.e1m0Admin.api.service.SystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSession;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminSystemService implements SystemServiceAPI {

    private final ReportSystemRepository reportRepository;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final AdminSessionManager sessionManager;

    private final Map<UUID, UUID> reportPlayer;

    private final FileConfiguration cfg;
    private final E1m0Sender sender;
    private final E1m0Admin plugin;

    private E1m0Color c = new E1m0Color();

    public AdminSystemService(ReportSystemRepository reportRepository, AdminSessionManager sessionManager, AdminSystemRepository systemRepository, AdminStaffRepository staffRepository, Map<UUID, UUID> reportPlayer, FileConfiguration cfg, E1m0Sender sender, E1m0Admin plugin) {
        this.reportRepository = reportRepository;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.sessionManager = sessionManager;
        this.reportPlayer = reportPlayer;
        this.sender = sender;
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @Override
    public void adminPay() {
        // Получаю секцию часов из конфига.
        ConfigurationSection sectionSalary = cfg.getConfigurationSection("Admin.AdminRanks");
        ConfigurationSection sectionBonus = cfg.getConfigurationSection("Admin.Salary.SalaryActions");
                new BukkitRunnable() {
            @Override
            public void run() {
                for (AdminSession aSession : sessionManager.getSessions()) {
                    Player adm = Bukkit.getPlayer(aSession.getUuid());

                    if (aSession.getUuid() != adm.getUniqueId()) continue;

                    // Проверяю: A) Время нажитое на сервере. B) Какой это час?
                    long onlineTime = System.currentTimeMillis() - aSession.getJoinTime();
                    long hours = onlineTime / 3600000L;

                    // Если час превышает тот что был в State = значит он новый.
                    if (hours > aSession.getWorkedHours()) {
                        aSession.plusWorkedHours();

                        if (!cfg.getBoolean("Server.adminPay")) return;
                        // Игрок все еще жив и на сервере?
                        if (adm == null) {
                            continue;
                        }

                        for (String key : sectionSalary.getKeys(true)) {
                            String adminPrefix = aSession.getAdminPrefix();

                            // Если не подошел по prefix = скип.
                            if (!cfg.getString("Admin.AdminRanks." + key + ".prefix").equalsIgnoreCase(adminPrefix))
                                continue;

                            // Получаю зарплату
                            int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");

                            // Вывожу сообщение + выдаю ему честно нажитое.
                            sender.sendPath(adm, "Messages.adminPay",
                                    "%salary", String.valueOf(salary));

                            // Достаю его зарплату из конфига по патчу
                            String str = cfg.getString("Admin.AdminRanks" + key + ".scom")
                                    .replace("%player", adm.getName())
                                    .replace("%salary", String.valueOf(salary));

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
                        }

                        // Если админ бонус на сервере выключен - скип.
                        if (!cfg.getBoolean("Server.adminBonus")) continue;
                        for (String key : sectionBonus.getKeys(true)) {

                            // Получаю часы и зарплату игрока из его таблицы + state session.
                            int hour = cfg.getInt("Admin.Salary.SalaryActions." + key + ".hour");
                            int salary = aSession.getAdminSalary();

                            if (hour == aSession.getWorkedHours()) {
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
                }.runTaskTimer(plugin, 20 * 60 * cfg.getLong("Settings.Dev.salaryCheck"), 20 * cfg.getLong("Settings.salaryCheck"));
    }

    @Override
    public void handleReportAccept(UUID adminID, UUID reportID) {
        Bukkit.getLogger().info("ReportController | Точка входа GAME-COMMAND-SERVICE: Обработка репорта ID: " + reportID); // ТЕСТЕР
        Report report = reportRepository.getReport(reportID);

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



        reportRepository.updateReport(newReport);
        reportPlayer.remove(report.getPlayerID());
        Bukkit.getLogger().info("ReportController | Точка входа GAME-COMMAND-SERVICE: Репорт выполнен! " + reportID); // ТЕСТЕР
    }
}