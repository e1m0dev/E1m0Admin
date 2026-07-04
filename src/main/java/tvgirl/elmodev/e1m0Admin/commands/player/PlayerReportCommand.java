package tvgirl.elmodev.e1m0Admin.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.*;

public class PlayerReportCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final Map<UUID, Report> playerReportCache;

    public PlayerReportCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service, Map<UUID, Report> playerReportCache) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.playerReportCache = playerReportCache;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.report");
        if (!isAllowed) {
            return false;
        }

        UUID randomID = UUID.randomUUID();

        int minLength = cfg.getInt("Admin.Report.minReportLength");
        int maxSize = cfg.getInt("Admin.Report.reportMaxSize");


        if (strings.length < 2) {
            sender.sendPath(player, "Messages.Errors.lengthError");
            return false;
        }

        String[] messageArray = Arrays.copyOfRange(strings, 1, strings.length);
        String reportMessage = String.join(" ", messageArray);
        List<Report> reportList = new ArrayList<>();


        String permission = cfg.getString("Permissions.admin");
        if (player.hasPermission(permission)) {
            sender.sendPath(player, "Messages.Errors.adminSendReportToAdmins");
            return false;
        }

        Bukkit.getLogger().info("Точка 2"); // ТЕСТЕР

        boolean containsInMemory = playerReportCache.containsKey(player.getUniqueId());
        if (containsInMemory) {
            sender.sendPath(player, "Messages.Errors.playerHaveReport");
            return false;
        }

        Bukkit.getLogger().info("Size: " + reportList.size());

        for (Map.Entry<UUID, Report> report : playerReportCache.entrySet()) reportList.add(report.getValue());

        if (reportList != null) {
            if (reportList.size() >= maxSize) {
                Bukkit.getLogger().info(String.valueOf(reportList.size() >= maxSize));

                Bukkit.getLogger().info("Size: " + reportList.size()); // ТЕСТЕР
                sender.sendPath(player, "Messages.Errors.reportSizeIsMax");
                return false;
            }
        }

        Bukkit.getLogger().info("Точка 3"); // ТЕСТЕР

        if(command.getName().toLowerCase().equalsIgnoreCase("report")) {
            Bukkit.getLogger().info("RepMessage: " + reportMessage);
            Bukkit.getLogger().info("Минималка: " + minLength);

            Bukkit.getLogger().info("Точка len: " + reportMessage.length()); // ТЕСТЕР
            Bukkit.getLogger().info("Точка minLength: " + minLength); // ТЕСТЕР

            if(reportMessage.length() < minLength) {
                sender.sendPath(player, "Messages.Errors.reportLengthError",
                        "%len", String.valueOf(minLength));

                return false;
            }

            Bukkit.getLogger().info("Точка 5"); // ТЕСТЕР

            Report report = new Report(
                    randomID,
                    null,
                    player.getUniqueId(),
                    null,
                    player.getName(),
                    reportMessage,
                    null,
                    cfg.getString("Admin.Report.status_send"),
                    System.currentTimeMillis()
            );

            Bukkit.getLogger().info("Точка 6"); // ТЕСТЕР

            Bukkit.getLogger().info("RepMessage: " + reportMessage);
            Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND: /report была введена и пропущена. PlayerID: " + player.getUniqueId() + "PlayerName: " + player.getName() + "Сообщение: " + reportMessage); // ТЕСТЕР

            sender.sendPath(player, "Messages.reportSended",
                    "%report", reportMessage);

            service.sendReport(report);
            playerReportCache.put(player.getUniqueId(), report);
        }

        return true;
    }
}
