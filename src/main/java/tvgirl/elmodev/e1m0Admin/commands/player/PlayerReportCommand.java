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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

        List<Report> reportList = new ArrayList<>();
        Bukkit.getLogger().info("Точка 1"); // ТЕСТЕР

        if(strings.length < 2) {
            sender.sendPath(player, "Messages.Errors.lengthError");
            return false;
        }

        Bukkit.getLogger().info("Точка 2"); // ТЕСТЕР

        if (playerReportCache.containsKey(player.getUniqueId())) {
            sender.sendPath(player, "Messages.Errors.playerHaveReport");
            return false;
        }

        for (Map.Entry<UUID, Report> report : playerReportCache.entrySet()) reportList.add(report.getValue());

        if (reportList != null) {
            if (reportList.size() >= cfg.getInt("Settings.reportMaxSize")) {
                sender.sendPath(player, "Messages.Errors.reportSizeIsMax");
                return false;
            }
        }

        Bukkit.getLogger().info("Точка 3"); // ТЕСТЕР

        //TODO: Сделать проверку на 50 репортов, потому что пока что - одна страница.

        if(command.getName().toLowerCase().equalsIgnoreCase("report")) {
            int minLength = cfg.getInt("Admin.Report.minReportLength");
            String reportMessage = String.join(" ", strings);

            Bukkit.getLogger().info("RepMessage: " + reportMessage);
            Bukkit.getLogger().info("Минималка: " + minLength);

            Bukkit.getLogger().info("Точка 4"); // ТЕСТЕР

            if(reportMessage.length() < minLength) {
                sender.sendPath(player, cfg.getString("Messages.Errors.reportLengthError!"),
                        "%len", String.valueOf(minLength));

                return false;
            }

            Bukkit.getLogger().info("Точка 5"); // ТЕСТЕР

            UUID randomID = UUID.randomUUID();
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

            service.sendReport(report);
            playerReportCache.put(player.getUniqueId(), report);
        }

        return true;
    }
}
