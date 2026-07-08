package tvgirl.elmodev.e1m0Admin.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.service.AdminsStaffService;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class AdminSetCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;
    private final E1m0Permission permissionManager;

    public AdminSetCommand(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
        this.permissionManager = permissionManager;
    }

    // /aset E1m0 1
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player staff)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        boolean checkPermission = permissionManager.checkSecretCodeAccess(staff.getUniqueId());
        if (!checkPermission) {
            sender.sendPath(staff, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        String permission = cfg.getString("Permissions.setadm");
        if (!staff.hasPermission(permission)) {
            sender.sendPath(staff, "Messages.Errors.permissionError");
            return false;
        }

        if (strings.length != 2) {
            sender.sendPath(staff, "Messages.Errors.lengthError");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("aset")) {
            Player admin = Bukkit.getPlayer(strings[0]);
            int weight = Integer.parseInt(strings[1]);

            if(weight <= 0) {
                sender.sendPath(staff, "Messages.Errors.setAdminWeightIsNull");
                return false;
            }

            if(admin == null) {
                sender.sendPath(staff, "Messages.Errors.nullPlayer");
                return false;
            }

            staffService.setAdmin(admin.getUniqueId(), staff.getUniqueId(), weight);
        }

        return true;
    }
}
