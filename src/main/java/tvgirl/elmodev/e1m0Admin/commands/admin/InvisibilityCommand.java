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
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if (!(permissionManager.checkSecretCodeAccess(admin.getUniqueId()))) {
            Bukkit.getLogger().info("AccessCommand | ТОЧКА ЗАШЛА В ЧЕКЕР!"); // ТЕСТЕР
            sender.sendPath(admin, "Messages.Errors.secretCodeNotInput", "", "");
            return false;
        }

        String permission = cfg.getString("Permissions.invisibility");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }


        Bukkit.getLogger().info("InvisibilityCommand | ПРОШЛА РЕГИСТРАЦИЮ"); // ТЕСТЕР

        if (command.getName().toLowerCase().equalsIgnoreCase("ainv")) {
            Bukkit.getLogger().info("InvisibilityCommand | Точка входа COMMAND: /ainvise была введена и пропущена."); // ТЕСТЕР
            service.handleInvisibility(admin.getUniqueId());
        }

        return true;
    }
}
