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
import tvgirl.elmodev.e1m0Admin.event.AdminComplimentEvent;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;
import tvgirl.elmodev.e1m0admin.api.service.GameServiceAPI;
import tvgirl.elmodev.e1m0Admin.gui.guis.report.ReportGUI;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.repository.AdminGameRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.ReportSystemRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.state.report.Report;

import java.util.*;
import java.util.List;

public class AdminGameService implements GameServiceAPI {

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    private final ReportSystemRepository reportRepository;
    private final AdminStaffRepository staffRepository;
    private final AdminGameRepository gameRepository;
    private final HashSet<UUID> thanksPlayers;
    private final SecretCodeGui secretCodeGui;
    private final E1m0Permission permission;
    private final E1m0Color color;

    private final HashSet<UUID> inviseCache; // Администраторы в инвизе;

    private final HashMap<UUID, BukkitTask> rewatchTasksCache; // Таски для переноса и защитки;
    private final HashMap<UUID, Report> playerReportCache; // Игроки с репортом в памяти;
    private final HashMap<UUID, UUID> reconCache; // Администраторы в рекона;


    private final FileConfiguration cfg;
    private final ReportGUI reportGUI;
    private final E1m0Sender sender;
    private final E1m0Admin plugin;

    public AdminGameService(ReportSystemRepository reportRepository, AdminStaffRepository staffRepository, AdminGameRepository gameRepository, HashSet<UUID> thanksPlayers, SecretCodeGui secretCodeGui, E1m0Color color, HashSet<UUID> inviseCache, HashMap<UUID, BukkitTask> rewatchTasksCache, HashMap<UUID, Report> playerReportCache, HashMap<UUID, UUID> reconCache, FileConfiguration cfg, E1m0Permission permission, ReportGUI reportGUI, E1m0Sender sender, E1m0Admin plugin) {
        this.playerReportCache = playerReportCache;
        this.rewatchTasksCache = rewatchTasksCache;
        this.reportRepository = reportRepository;
        this.staffRepository = staffRepository;
        this.gameRepository = gameRepository;
        this.thanksPlayers = thanksPlayers;
        this.secretCodeGui = secretCodeGui;
        this.inviseCache = inviseCache;
        this.reconCache = reconCache;
        this.permission = permission;
        this.reportGUI = reportGUI;
        this.sender = sender;
        this.plugin = plugin;
        this.color = color;
        this.cfg = cfg;
    }

    @Override
    public void handleInvisibility(UUID adminID) {
        Player admin = Bukkit.getPlayer(adminID);

        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (isBlockedAdmin) {
            sender.sendPath(admin, "Messages.Errors.youAdminAccessIsBlocked");
            return;
        }

        if (!(inviseCache.contains(adminID))) {
            admin.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, true));
            sender.sendPath(admin, "Messages.inviseOn");
            inviseCache.add(adminID);

        } else {
            admin.removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendPath(admin, "Messages.inviseOff");
            inviseCache.remove(adminID);
        }
    }

    @Override
    public void handleRewatch(UUID adminID, UUID playerID) {
        if (rewatchTasksCache.containsKey(adminID)) {
            BukkitTask old = rewatchTasksCache.remove(adminID);

            reconCache.remove(adminID);
            old.cancel();
        }

        Player admin = Bukkit.getPlayer(adminID);
        Player player = Bukkit.getPlayer(playerID);

        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (isBlockedAdmin) {
            sender.sendPath(admin, "Messages.Errors.youAdminAccessIsBlocked");
            return;
        }

        admin.setGameMode(GameMode.SPECTATOR);

        double rewatchVectorX = cfg.getDouble("Settings.Dev.rewatchVectorX");
        double rewatchVectorY = cfg.getDouble("Settings.Dev.rewatchVectorY");
        double rewatchVectorZ = cfg.getDouble("Settings.Dev.rewatchVectorZ");

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Vector v = new Vector(rewatchVectorX, rewatchVectorY, rewatchVectorZ);
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

        if (rewatchTasksCache.containsKey(adminID)) {
            BukkitTask task = rewatchTasksCache.remove(adminID);
            task.cancel();
        } else {
            sender.sendPath(admin, "Messages.Errors.reoffNullError");
        }

        admin.teleportAsync(new Location(world, x, y, z));
        sender.sendPath(admin, "Messages.teleportToAZ");
        reconCache.remove(adminID);
    }

    @Override
    public void sendReport(@NotNull Report report) {
        Player p = Bukkit.getPlayer(report.getPlayerID());
        List<String> emergencySwords = cfg.getStringList("Admin.Report.EmergencyReportSword");

        for(Player adm : Bukkit.getOnlinePlayers()) {
            if(adm.hasPermission(cfg.getString("Permissions.admin"))) {
                // Если в репорте есть контрольные слова – то он выделяется

                // EMERGENCY
                if (!cfg.getBoolean("Server.emergencyRep")) return;
                for (String s : emergencySwords) {
                    String sword = s.toLowerCase();
                    if (report.getReport().toLowerCase().contains(sword)) {
                        if (cfg.getBoolean("Server.emergencyRep")) {
                            // 🚨 | Если это сообщение срочное:
                            fastReport(adm.getUniqueId(), report);
                            adm.playSound(adm, Sound.valueOf(cfg.getString("Admin.Report.EmergencyReport.sound")), 1.0f, 1.0f);
                            return;
                        }
                    }
                }

                // DONATE
                if (cfg.getBoolean("Server.donateReport") && p.hasPermission(cfg.getString("Admin.Report.DonateReport.permission"))) {
                    // 🤑 | Если это сообщение донатное:
                    adm.playSound(adm, Sound.valueOf(cfg.getString("Admin.Report.DonateReport.sound")), 1.0f, 1.0f);


                    String argContent = report.getReport();
                    String argPlayer = report.getPlayerNick();
                    sender.sendPathCfg(adm, "Admin.Report.DonateReport.donateMessage",
                            "%content", argContent,
                            "%player", argPlayer);

                } else {
                    // ⌚ | Если это сообщение обычное:
                    if (cfg.getBoolean("Server.newReport")) {
                        adm.playSound(adm, Sound.valueOf(cfg.getString("Admin.Report.NewReport.sound")), 1.0f, 1.0f);

                        String argContent = report.getReport();
                        String argPlayer = report.getPlayerNick();

                        sender.sendPathCfg(adm, "Admin.Report.NewReport.reportMessage",
                                "%player", argPlayer,
                                "%content", argContent);
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
                .replace("%player", report.getPlayerNick())
                .replace("%date", String.valueOf(report.getCreatedAt()));

        // Text
        for (String message : emergencyList) {
            Component component = color.parse(
                    message
                            .replace("%content", report.getReport())
                            .replace("%status", report.getStatus())
                            .replace("%player", report.getPlayerNick())
                            .replace("%date", String.valueOf(report.getCreatedAt()))
            ).hoverEvent(
                    HoverEvent.showText(
                            color.parse(hoverText)
                    )
            ).clickEvent(
                    ClickEvent.runCommand("/arepaccept " + report.getUuid())
            );

            adm.sendMessage(component);
        }

        Player player = Bukkit.getPlayer(report.getPlayerID());
        Player admin = Bukkit.getPlayer(adminID);

        String response = cfg.getString("Admin.Report.EmergencyReport.emergencyMessage");
        String responseFinal = response
                .replace("%player", player.getName()
                        .replace("%content", report.getReport()));

        // Авто-репорт
        Report newReport = new Report(
                report.getUuid(),
                adminID,
                report.getPlayerID(),
                admin.getName(),
                player.getName(),
                report.getReport(),
                responseFinal,
                report.getStatus(),
                report.getCreatedAt()
        );

        playerReportCache.remove(report.getPlayerID());
    }

    @Override
    public void adminBlockAccess(UUID suspectID, UUID adminID) {
        Player suspect = Bukkit.getPlayer(suspectID);
        Player admin = Bukkit.getPlayer(adminID);

        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);
        boolean isBlockedTarget = staffRepository.checkAdminABan(suspectID);

        if (isBlockedAdmin) {
            sender.sendPath(admin, "Messages.Errors.youAdminAccessIsBlocked");
            return;
        }

        if (isBlockedAdmin || isBlockedTarget) {
            sender.sendPath(admin, "Messages.Errors.adminBlockInternalError");
            return;
        }

        staffRepository.setAdminABan(adminID, suspectID); // Таргету - блокирует доступ админ, потому что он использовал /aban, а вот самому админу, блокирует доступ СИСТЕМА, потому что это ТОЖЕ элемент защиты.
        staffRepository.setAdminABanConsole(adminID, consoleID);

        sender.sendPath(admin, "Messages.successfulBlocked",
                "%target", suspect.getName());
        sender.sendPath(suspect, "Messages.Errors.youAdminAccessIsBlocked",
                "%admin", admin.getName());
    }

    @Override
    public void adminHelp(UUID adminID) {
        Player admin = Bukkit.getPlayer(adminID);

        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (isBlockedAdmin) {
            sender.sendPath(admin, "Messages.Errors.youAdminAccessIsBlocked");
            return;
        }

        if (admin.hasPermission(cfg.getString("Permissions.admin"))) {
            sender.sendPath(admin, "Admin.AHelp.admin");
        }

        if (admin.hasPermission(cfg.getString("Permissions.staff"))) {
            sender.sendPath(admin, "Admin.AHelp.staff");
        }
    }

    @Override
    public void getAdminList(UUID playerID) {
        // Администраторы на посту (checkPermission)
        // Администраторы в игре (hasPermission)

        Player player = Bukkit.getPlayer(playerID);

        List<String> adminListJob = new ArrayList<>();
        List<String> adminListGame = new ArrayList<>();

        List<String> finalMessage = new ArrayList<>();
        List<String> cfgAdminList = cfg.getStringList("Admin.AdminList");

        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (!admin.hasPermission(cfg.getString("Permissions.admin"))) continue;

            // | Условие: Если администратор регистрировался в системе - значит он на посту.
            if (permission.checkSecretCodeAccess(admin.getUniqueId())) {
                // | И мы - его добавляем.
                adminListJob.add(admin.getName());
            } else {

                // Но! Если он НЕ регистрировался НО имеет печать администратора - добавляем его в Game список.
                // То есть - в игре но не на посту.
                if (admin.hasPermission(cfg.getString("Permissions.admin"))) {
                    adminListGame.add(admin.getName());
                }
            }
        }

        for (String configString : cfgAdminList) {
            if (configString.contains("%adminInJob")) {

                if (adminListJob.isEmpty()) {
                    finalMessage.add(configString.replace("%adminInJob", "Никого"));
                } else {
                    for (String jobNick : adminListJob) {
                        finalMessage.add(configString.replace("%adminInJob", jobNick));
                    }
                }

                continue;
            }

            if (configString.contains("%adminInGame")) {

                if (adminListGame.isEmpty()) {
                    finalMessage.add(configString.replace("%adminInGame", "Никого"));
                } else {
                    for (String gameNick : adminListGame) {
                        finalMessage.add(configString.replace("%adminInGame", gameNick));
                    }
                }

                continue;
            }

            finalMessage.add(configString);
        }

        sender.sendStringList(player, finalMessage);

        adminListJob.clear();
        adminListGame.clear();
    }

    @Override
    public void addCompliment(UUID adminID, UUID playerID) {
        Player player = Bukkit.getPlayer(playerID);
        Player admin = Bukkit.getPlayer(adminID);

        if (thanksPlayers.contains(playerID)) {
            sender.sendPath(player, "Messages.Errors.cooldownErrorCompliment");
            return;
        }

        thanksPlayers.add(playerID);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (thanksPlayers.contains(playerID)) {
                thanksPlayers.remove(playerID);
            }
        }, 20 * 60 * cfg.getLong("Settings.complimentCooldown"));

        gameRepository.addCompliment(adminID);

        int compliments = gameRepository.getCompliments(adminID);
        Bukkit.getPluginManager().callEvent(new AdminComplimentEvent(compliments, playerID, adminID));
    }
}
