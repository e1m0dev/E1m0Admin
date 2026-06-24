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
import tvgirl.elmodev.e1m0Admin.repository.AdminGameRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.*;
import java.util.List;

public class AdminGameService implements GameServiceAPI {

    private final ReportSystemRepository reportRepository;
    private final AdminGameRepository gameRepository;

    private HashSet<UUID> invAdmins = new HashSet<>(); // Администраторы в инвизе;

    private final Map<UUID, UUID> reportPlayers; // Игроки с репортом в памяти;
    private final Map<UUID, UUID> reAdmins = new HashMap<>(); // Администраторы в рекона;
    private final Map<UUID, BukkitTask> rewatchTasks = new HashMap<>(); // Таски для переноса и защитки;

    private final FileConfiguration cfg;
    private final ReportGUI reportGUI;
    private final E1m0Sender sender;
    private final E1m0Admin plugin;

    public AdminGameService(ReportSystemRepository reportRepository, AdminGameRepository gameRepository, Map<UUID, UUID> reportPlayers, FileConfiguration cfg, ReportGUI reportGUI, E1m0Admin plugin, E1m0Sender sender) {
        this.reportRepository = reportRepository;
        this.gameRepository = gameRepository;
        this.reportPlayers = reportPlayers;
        this.reportGUI = reportGUI;
        this.plugin = plugin;
        this.sender = sender;
        this.cfg = cfg;
    }


    @Override
    public void handleInvisibility(UUID id) {
        Player p = Bukkit.getPlayer(id);
        if(!invAdmins.contains(id)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, true));
            sender.sendPath(p, "Messages.inviseOn");
            invAdmins.add(id);
        } else {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendPath(p, "Messages.inviseOff");
            invAdmins.remove(id);
        }
    }

    @Override
    public void handleRewatch(UUID adm, UUID p) {
        BukkitTask old = rewatchTasks.remove(adm);
        Player admin = Bukkit.getPlayer(adm);
        Player player = Bukkit.getPlayer(p);

        reAdmins.remove(adm);
        old.cancel();

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
        reAdmins.put(adm, p);
        rewatchTasks.put(adm, task);
    }

    @Override
    public void handleReoff(UUID adm) {
        Player admin = Bukkit.getPlayer(adm);
        admin.setGameMode(GameMode.SURVIVAL);

        World world = Bukkit.getWorld(cfg.getString("Admin.AdminZone.world"));
        double x = cfg.getDouble("Admin.AdminZone.x");
        double y = cfg.getDouble("Admin.AdminZone.y");
        double z = cfg.getDouble("Admin.AdminZone.z");

        BukkitTask task = rewatchTasks.remove(adm);
        task.cancel();

        admin.teleportAsync(new Location(world, x, y, z));
        reAdmins.remove(adm);
    }

    @Override
    public void sendReport(@NotNull Report report) {
        Player p = Bukkit.getPlayer(report.getPlayerID());
        List<String> emergencySwords = cfg.getStringList("Admin.Report.EmergencyReportSword");

        for(Player adm : Bukkit.getOnlinePlayers()) {
            if(adm.hasPermission(cfg.getString("Permissions.admin"))) {
                // Отправить репорт в базу данных через контроллер:
                gameRepository.gameReport(report.getPlayerID(), report.getAdminID(), report.getReport(), report.getResponse(), cfg.getString("Admin.Report.status_send"));

                // Если у игрока есть Донат обработать действия:
                if(cfg.getBoolean("Admin.Report.DonateReport.enable") && p.hasPermission(cfg.getString("Admin.Report.DonateReport.permission"))) {

                    sender.sendPath(adm, cfg.getString("Admin.Report.DonateReport")
                            .replace("%content", report.getReport())
                            .replace("%player", report.getPlayerNick()));
                }

                // Если в репорте есть контрольные слова – то он выделяется
                if(!cfg.getBoolean("Server.emergencyRep")) return;

                for(String s : emergencySwords) {
                    String sword = s.toLowerCase();

                    if (report.getReport().toLowerCase().contains(sword)) {
                        if (cfg.getBoolean("Server.emergencyRep")) {
                            // 🚨 | Если это сообщение срочное:
                            fastReport(adm.getUniqueId(), report);

                            adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.EmergencyReport.sound")), 1.0f, 1.0f);
                        }
                    } else if(cfg.getBoolean("Server.donateReport") && p.hasPermission(cfg.getString("Admin.Report.DonateReport.permission"))) {
                            // 🤑 | Если это сообщение донатное:
                        adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.DonateReport.sound")), 1.0f, 1.0f);

                        sender.sendPath(adm, cfg.getString("Admin.Report.DonateReport.donateMessage")
                                .replace("%content", report.getReport())
                                .replace("%player", report.getPlayerNick()));
                    } else {
                            // ⌚ | Если это сообщение обычное:
                        if (cfg.getBoolean("Server.newReport")) {
                            adm.playSound(p, Sound.valueOf(cfg.getString("Admin.Report.NewReport.sound")), 1.0f, 1.0f);

                            sender.sendPath(adm, cfg.getString("Admin.Report.NewReport.reportMessage")
                                    .replace("%content", report.getReport())
                                    .replace("%player", report.getPlayerNick()));
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
                            // TODO не забыть точку входа, а не то все взорвется.
                            "/admrep accept " + report.getUuid()
                    )
            );

            adm.sendMessage(component);
        }

        // Авто-репорт
        report.answer(adminID, adm.getName(), cfg.getString("Admin.Report.EmergencyReport.emergencyMessage"), cfg.getString("Admin.Report.status_answered"));
        reportRepository.updateReport(report);

        reportPlayers.remove(report.getPlayerID());
    }

    // /arep Администратор E1m0 спешит к Вам на помощь! | Или другая какая-либо форма.
    @Override
    public void openReportGUI(UUID adminID, String response) {
        reportGUI.openReportGUI(adminID, response);
    }

    @Override
    public void handleAccess(UUID id) {
        // Get admin weight
        // Get slot cfg
        // Get commands
        // Give commands



    }
}
