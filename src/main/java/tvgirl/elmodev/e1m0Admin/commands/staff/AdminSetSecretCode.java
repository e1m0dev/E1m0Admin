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

public class AdminSetSecretCode implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;
    private final E1m0Permission permissionManager;

    public AdminSetSecretCode(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
        this.permissionManager = permissionManager;
    }

    // /asecret E1m0 7777
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player staff)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if (!(permissionManager.checkSecretCodeAccess(staff.getUniqueId()))) {
            sender.sendPath(staff, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("asecret")) {
            Player admin = Bukkit.getPlayer(strings[0]);
            String strCode = strings[1];

            if (strCode.length() < 4) {
                sender.sendPath(staff, "Messages.Errors.setAdminCodeWrong");
                return false;
            }

            if (strCode.length() > 4) {
                sender.sendPath(staff, "Messages.Errors.setAdminCodeWrong");
                return false;
            }

            if(admin == null) {
                sender.sendPath(staff, "Messages.Errors.nullPlayer");
                return false;
            }

            int code = Integer.parseInt(strCode);
            if(staff.hasPermission(cfg.getString("Permissions.acodechange"))) {
                staffService.changeSecretPassword(admin.getUniqueId(), staff.getUniqueId(), code);

                Bukkit.getLogger().info("AdminChangeSecretCode | COMMAND: /acodechange. Команда прошла успешно, переменные: Staff: %staff, Admin: %admin, Code: %code"
                        .replace("%staff", staff.getName())
                        .replace("%admin", admin.getName())
                        .replace("%code", String.valueOf(code))); // ТЕСТЕР
            }
        }

        return true;
    }
}
