package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class InvisibilityCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final E1m0Permission permissionManager;

    public InvisibilityCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.invisibility");
        if (!isAllowed) {
            return false;
        }

        boolean checkPermission = permissionManager.checkSecretCodeAccess(admin.getUniqueId());
        if (!checkPermission) {
            sender.sendPath(admin, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        String permission = cfg.getString("Permissions.invisibility");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }


        if (command.getName().toLowerCase().equalsIgnoreCase("ainv")) {
            service.handleInvisibility(admin.getUniqueId());

            // CLS | Console Log
            boolean isActive = cfg.getBoolean("Settings.consoleLogActive");
            if (isActive) {
                sender.sendConsole(Bukkit.getConsoleSender(), "Messages.ConsoleLogs.inviseLog",
                        "%admin", admin.getName());
            }
        }

        return true;
    }
}
