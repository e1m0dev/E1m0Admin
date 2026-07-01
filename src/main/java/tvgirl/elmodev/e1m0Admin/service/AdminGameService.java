package tvgirl.elmodev.e1m0Admin.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.E1m0Admin;
import tvgirl.elmodev.e1m0Admin.api.service.GameServiceAPI;
import tvgirl.elmodev.e1m0Admin.gui.guis.report.ReportGUI;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.repository.AdminGameRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.*;
import java.util.List;

public class AdminGameService implements GameServiceAPI {

    private final ReportSystemRepository reportRepository;
    private final AdminGameRepository gameRepository;
    private final SecretCodeGui secretCodeGui;

    // Перенести в дамп E1m0
    private final HashSet<UUID> inviseCache; // Администраторы в инвизе;

    private final HashMap<UUID, BukkitTask> rewatchTasksCache; // Таски для переноса и защитки;
    private final HashMap<UUID, UUID> playerReportCache; // Игроки с репортом в памяти;
    private final HashMap<UUID, UUID> reconCache; // Администраторы в рекона;

    private final FileConfiguration cfg;
    private final ReportGUI reportGUI;
    private final E1m0Sender sender;
    private final E1m0Admin plugin;

    public AdminGameService(ReportSystemRepository reportRepository, AdminGameRepository gameRepository, SecretCodeGui secretCodeGui, HashSet<UUID> inviseCache, HashMap<UUID, BukkitTask> rewatchTasksCache, HashMap<UUID, UUID> playerReportCache, HashMap<UUID, UUID> reconCache, FileConfiguration cfg, ReportGUI reportGUI, E1m0Sender sender, E1m0Admin plugin) {
        this.rewatchTasksCache = rewatchTasksCache;
        this.playerReportCache = playerReportCache;
        this.reportRepository = reportRepository;
        this.gameRepository = gameRepository;
        this.secretCodeGui = secretCodeGui;
        this.inviseCache = inviseCache;
        this.reconCache = reconCache;
        this.reportGUI = reportGUI;
        this.sender = sender;
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @Override
    public void handleInvisibility(UUID adminID) {
        Player p = Bukkit.getPlayer(adminID);
        Bukkit.getLogger().info("InvisibilityCommand | Точка входа COMMAND-SERVICE: /ainvise | Успешно прошел в команду."); // ТЕСТЕР

        if (!(inviseCache.contains(adminID))) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, true));
            sender.sendPath(p, "Messages.inviseOn");
            inviseCache.add(adminID);

            Bukkit.getLogger().info("InvisibilityCommand | Точка входа COMMAND-SERVICE: /ainvise | Был успешно включен. Статус бакета HashMap: Не найден."); // ТЕСТЕР
        } else {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendPath(p, "Messages.inviseOff");
            inviseCache.remove(adminID);

            Bukkit.getLogger().info("InvisibilityCommand | Точка входа COMMAND-SERVICE: /ainvise | Был успешно выключен. Статус бакета HashMap: Найден"); // ТЕСТЕР
        }
    }

    @Override
    public void handleRewatch(UUID adminID, UUID playerID) {
        Bukkit.getLogger().info("InvisibilityCommand | Команда в обработчике!"); // ТЕСТЕР

        if (rewatchTasksCache.containsKey(adminID)) {
            BukkitTask old = rewatchTasksCache.remove(adminID);

            reconCache.remove(adminID);
            old.cancel();
        }

        Player admin = Bukkit.getPlayer(adminID);
        Player player = Bukkit.getPlayer(playerID);

        admin.setGameMode(GameMode.SPECTATOR);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Vector v = new Vector(0, cfg.getDouble("Settings.Dev.rewatchVector"), 0);
                Location watchVector = player.getLocation().add(v);

                admin.teleport(watchVector);
            }
        };

        BukkitTask task = runnable.runTaskTimer(plugin, cfg.getLong("Settings.Dev.rewatchTick"), cfg.getLong("Settings.Dev.rewatchTick"));
        reconCache.put(adminID, playerID);
        rewatchTasksCache.put(adminID, task);
    }

    @Override
    public void handleReoff(UUID adminID) {
        Player admin = Bukkit.getPlayer(adminID);
        admin.setGameMode(GameMode.SURVIVAL);

        World world = Bukkit.getWorld(cfg.getString("Admin.AdminZone.world"));
        double x = cfg.getDouble("Admin.AdminZone.x");
        double y = cfg.getDouble("Admin.AdminZone.y");
        double z = cfg.getDouble("Admin.AdminZone.z");

        BukkitTask task = rewatchTasksCache.remove(adminID);
        task.cancel();

        admin.teleportAsync(new Location(world, x, y, z));
        reconCache.remove(adminID);
    }

    @Override
    public void sendReport(@NotNull Report report) {
        Player p = Bukkit.getPlayer(report.getPlayerID());
        List<String> emergencySwords = cfg.getStringList("Admin.Report.EmergencyReportSword");
        Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 1. Обьявление."); // ТЕСТЕР


        for(Player adm : Bukkit.getOnlinePlayers()) {
            if(adm.hasPermission(cfg.getString("Permissions.admin"))) {
                // Отправить репорт в базу данных через контроллер:
                gameRepository.gameReport(report.getPlayerID(), report.getAdminID(), report.getReport(), report.getResponse(), cfg.getString("Admin.Report.status_send"));
                Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 2. Отсеивание не адм + sendRepo."); // ТЕСТЕР

                // Если в репорте есть контрольные слова – то он выделяется
                if(!cfg.getBoolean("Server.emergencyRep")) return;

                for(String s : emergencySwords) {
                    String sword = s.toLowerCase();
                    Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 3. Выборка"); // ТЕСТЕР

                    // TODO: Сделать ли кастом hover для всех?
                    if (report.getReport().toLowerCase().contains(sword)) {
                        if (cfg.getBoolean("Server.emergencyRep")) {
                            // 🚨 | Если это сообщение срочное:
                            fastReport(adm.getUniqueId(), report);
                            adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.EmergencyReport.sound")), 1.0f, 1.0f);
                            Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 4. Выборка: Срочное."); // ТЕСТЕР
                        }
                    } else if(cfg.getBoolean("Server.donateReport") && p.hasPermission(cfg.getString("Admin.Report.DonateReport.permission"))) {
                            // 🤑 | Если это сообщение донатное:
                        adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.DonateReport.sound")), 1.0f, 1.0f);

                        sender.sendPath(adm, cfg.getString("Admin.Report.DonateReport.donateMessage")
                                .replace("%content", report.getReport())
                                .replace("%player", report.getPlayerNick()));

                        Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 5. Выборка: Донатное."); // ТЕСТЕР
                    } else {
                            // ⌚ | Если это сообщение обычное:
                        if (cfg.getBoolean("Server.newReport")) {
                            adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.NewReport.sound")), 1.0f, 1.0f);

                            sender.sendPath(adm, cfg.getString("Admin.Report.NewReport.reportMessage")
                                    .replace("%content", report.getReport())
                                    .replace("%player", report.getPlayerNick()));

                            Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND-SERVICE: /report | Точка выхода: 6. Выборка: Обычное."); // ТЕСТЕР
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fastReport(UUID adminID, Report report) {
        List<String> emergencyList = cfg.getStringList("Admin.Report.EmergencyReport.emergencyMessage");
        List<String> hover = cfg.getStringList("Admin.Report.HoverReport");
        Player adm = Bukkit.getPlayer(adminID);
        UUID id = report.getUuid();

        String hoverText = String.join("\n", hover)
                .replace("%content", report.getReport())
                .replace("%status", report.getStatus())
                .replace("%admin", report.getAdminNick())
                .replace("%player", report.getPlayerNick())
                .replace("%date", String.valueOf(report.getCreatedAt()));

        // Text
        for (String message : emergencyList) {
            Component component = MiniMessage.miniMessage().deserialize(
                    message
                            .replace("%content", report.getReport())
                            .replace("%status", report.getStatus())
                            .replace("%admin", report.getAdminNick())
                            .replace("%player", report.getPlayerNick())
                            .replace("%date", String.valueOf(report.getCreatedAt()))
            ).hoverEvent(
                    HoverEvent.showText(
                            MiniMessage.miniMessage().deserialize(hoverText)
                    )
            ).clickEvent(
                    ClickEvent.runCommand(
                            "/arepaccept " + report.getUuid()
                    )
            );

            adm.sendMessage(component);
        }

        // Авто-репорт
        report.answer(adminID, adm.getName(), cfg.getString("Admin.Report.EmergencyReport.emergencyMessage"), cfg.getString("Admin.Report.status_answered"));
        reportRepository.updateReport(report);

        playerReportCache.remove(report.getPlayerID());
    }
}
