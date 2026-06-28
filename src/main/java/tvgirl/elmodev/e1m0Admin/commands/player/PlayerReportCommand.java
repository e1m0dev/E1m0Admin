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

import java.util.Map;
import java.util.UUID;

public class PlayerReportCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final Map<UUID, UUID> reportPlayers;

    public PlayerReportCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service, Map<UUID, UUID> reportPlayers) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.reportPlayers = reportPlayers;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if(strings.length < 2) {
            sender.sendPath(player, "Messages.Errors.lengthError");
            return false;
        }

        if (reportPlayers.containsKey(player.getUniqueId())) {
            sender.sendPath(player, "Messages.Errors.playerHaveReport");
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("report")) {
            int minLength = cfg.getInt("Admin.Report.minReportLength");
            String reportMessage = String.join(" ", strings);

            if(reportMessage.length() < minLength) {
                sender.sendPath(player, cfg.getString("Messages.Errors.reportLengthError!")
                        .replace("%len", String.valueOf(minLength)));

                return false;
            }

            UUID randomID = UUID.randomUUID();

            Report report = new Report(
                    randomID,
                    null,
                    player.getUniqueId(),
                    null,
                    player.getName(),
                    reportMessage,
                    "Null..",
                    cfg.getString("Admin.Report.status_send"),
                    System.currentTimeMillis()
            );

            Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND: /report была введена и пропущена. PlayerID: " + player.getUniqueId() + "PlayerName: " + player.getName() + "Сообщение: " + reportMessage); // ТЕСТЕР

            service.sendReport(report);
            reportPlayers.put(player.getUniqueId(), randomID);
        }

        return true;
    }
}
