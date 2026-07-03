package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.gui.guis.report.ReportGUI;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class ReportCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final ReportGUI reportGUI;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final E1m0Permission permissionManager;

    public ReportCommand(E1m0Sender sender, ReportGUI reportGUI, FileConfiguration cfg, AdminGameService service, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.reportGUI = reportGUI;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if (strings.length != 1) {
            Bukkit.getLogger().info("AccessCommand | ТОЧКА ЗАШЛА В ЧЕКЕР STR!"); // ТЕСТЕР
            sender.sendPath(admin, "Messages.Errors.lengthError");
            return false;
        }

        boolean checkPermission = permissionManager.checkSecretCodeAccess(admin.getUniqueId());
        if (!checkPermission) {
            sender.sendPath(admin, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        String permission = cfg.getString("Permissions.arep");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        Bukkit.getLogger().info("AccessCommand | ТОЧКА ПРОШЛА ВСЕ ПРОВЕРКИ!"); // ТЕСТЕР

        // /arep Администратор E1m0 спешит к Вам на помощь! | Или другая какая-либо форма.
        String response = String.join(" ", strings);

        Bukkit.getLogger().info("ReportCommand | Точка входа COMMAND: /arep была введена и пропущена. Сообщение от администратора: " + response); // ТЕСТЕР
        reportGUI.openReportGUI(admin.getUniqueId(), response);

        return true;
    }
}
