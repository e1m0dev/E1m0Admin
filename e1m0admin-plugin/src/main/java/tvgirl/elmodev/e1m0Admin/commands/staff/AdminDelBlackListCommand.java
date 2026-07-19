package tvgirl.elmodev.e1m0Admin.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminsStaffService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

import java.util.Arrays;

public class AdminDelBlackListCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;
    private final E1m0Permission permissionManager;

    public AdminDelBlackListCommand(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
        this.permissionManager = permissionManager;
    }

    // /abdlist E1m0 Изменил своему пиратскому братству
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player staff)) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.ablacklist");
        if (!isAllowed) {
            return false;
        }

        if (strings.length < 2) {
            sender.sendPath(staff, "Messages.Errors.lengthError");
            return false;
        }

        String permission = cfg.getString("Permissions.ablocklist");
        if (!staff.hasPermission(permission)) {
            sender.sendPath(staff, "Messages.Errors.permissionError");
            return false;
        }

        if (!(permissionManager.checkSecretCodeAccess(staff.getUniqueId()))) {
            sender.sendPath(staff, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        String[] message = Arrays.copyOfRange(strings, 1, strings.length);
        String reason = String.join(" ", message);
        Player admin = Bukkit.getPlayer(strings[0]);

        String adminPermission = cfg.getString("Permissions.admin");
        if (!admin.hasPermission(adminPermission)) {
            sender.sendPath(staff, "Messages.Errors.notAdmin");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("abdlist")) {
            staffService.adminDelBlackList(admin.getUniqueId(), staff.getUniqueId(), reason);
        }

        return true;
    }
}
