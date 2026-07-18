package tvgirl.elmodev.e1m0Admin.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.state.report.Report;

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
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
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

        boolean containsInMemory = playerReportCache.containsKey(player.getUniqueId());
        if (containsInMemory) {
            sender.sendPath(player, "Messages.Errors.playerHaveReport");
            return false;
        }

        for (Map.Entry<UUID, Report> report : playerReportCache.entrySet()) reportList.add(report.getValue());

        if (reportList != null) {
            if (reportList.size() >= maxSize) {
                Bukkit.getLogger().info(String.valueOf(reportList.size() >= maxSize));

                sender.sendPath(player, "Messages.Errors.reportSizeIsMax");
                return false;
            }
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("report")) {
            if(reportMessage.length() < minLength) {
                sender.sendPath(player, "Messages.Errors.reportLengthError",
                        "%len", String.valueOf(minLength));

                return false;
            }

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

            sender.sendPath(player, "Messages.reportSended",
                    "%report", reportMessage);

            service.sendReport(report);
            playerReportCache.put(player.getUniqueId(), report);
        }

        return true;
    }
}
