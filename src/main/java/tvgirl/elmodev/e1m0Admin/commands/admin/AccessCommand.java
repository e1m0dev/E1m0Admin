package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class AccessCommand implements CommandExecutor {

    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final E1m0Permission permissionManager;

    public AccessCommand(FileConfiguration cfg, AdminGameService service, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.service = service;
        this.permissionManager = permissionManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("aaccess")) {
            Bukkit.getLogger().info("AccessCommand | Точка входа COMMAND: /aacess прошла регистрацию."); // ТЕСТЕР
            String permission = cfg.getString("Permissions.admin");

            Bukkit.getLogger().info("Permission: " + permission);

            if (admin.hasPermission(permission)) {
                Bukkit.getLogger().info("AccessCommand | Точка входа COMMAND: /aacess была введена и пропущена."); // ТЕСТЕР
                service.handleAccess(admin.getUniqueId());
            }
        }
        return true;
    }

}
