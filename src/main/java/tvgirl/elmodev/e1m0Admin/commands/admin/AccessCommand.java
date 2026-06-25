package tvgirl.elmodev.e1m0Admin.commands.admin;

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
            if (admin.hasPermission(cfg.getString("Permissions.admin"))) {
                service.handleAccess(admin.getUniqueId());
            }
        }

        return true;
    }

}
