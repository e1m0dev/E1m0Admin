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
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        Bukkit.getLogger().warning("0.1"); // ТЕСТЕР

        boolean isAllowed = cfg.getBoolean("Server.report");
        if (!isAllowed) {
            return false;
        }

        Bukkit.getLogger().warning("0.2"); // ТЕСТЕР

        if (strings.length < 1) {
            sender.sendPath(admin, "Messages.Errors.lengthError");
            return false;
        }

        Bukkit.getLogger().warning("0.3"); // ТЕСТЕР

        if (!permissionManager.checkSystem(admin.getUniqueId())) return false;

        Bukkit.getLogger().warning("0.4"); // ТЕСТЕР

        boolean checkPermission = permissionManager.checkSecretCodeAccess(admin.getUniqueId());
        if (!checkPermission) {
            sender.sendPath(admin, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        Bukkit.getLogger().warning("0.5"); // ТЕСТЕР

        String permission = cfg.getString("Permissions.arep");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        Bukkit.getLogger().warning("0.6"); // ТЕСТЕР

        String response = String.join(" ", strings);

        // /arep Администратор E1m0 спешит к Вам на помощь! | Или другая какая-либо форма.
        if (command.getName().toLowerCase().equalsIgnoreCase("arep")) {
            Bukkit.getLogger().warning("1"); // ТЕСТЕР

            reportGUI.openReportGUI(admin.getUniqueId(), response);

            // CLS | Console Log
            boolean isActive = cfg.getBoolean("Settings.consoleLogActive");
            if (isActive) {
                sender.sendConsole(Bukkit.getConsoleSender(), "Messages.ConsoleLogs.reportLog",
                        "%admin", admin.getName(),
                        "%response", response
                );
            }
        }


        return true;
    }
}
