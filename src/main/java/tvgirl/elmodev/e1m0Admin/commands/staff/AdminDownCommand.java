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
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;

public class AdminDownCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;
    private final E1m0Permission permissionManager;
    private final AdminSystemRepository systemRepository;

    public AdminDownCommand(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService, E1m0Permission permissionManager, AdminSystemRepository systemRepository) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
        this.permissionManager = permissionManager;
        this.systemRepository = systemRepository;
    }

    // /aup E1m0
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

        String permission = cfg.getString("Permissions.downadm");
        if (!staff.hasPermission(permission)) {
            sender.sendPath(staff, "Messages.Errors.permissionError");
            return false;
        }

        if(strings.length < 1) {
            sender.sendPath(staff, "Messages.Errors.lengthError");
            return false;
        }

        Player admin = Bukkit.getPlayer(strings[0]);

        if (admin == null) {
            sender.sendPath(staff, "Messages.Errors.nullPlayer");
            return false;
        }

        int weight = systemRepository.getAdminWeight(admin.getUniqueId());
        if (weight == -1) {
            sender.sendPath(staff, "Messages.Errors.nullPlayer");
            return false;
        }

        // Если weight равно 1 нельзя понизить
        if (weight <= 1) {
            sender.sendPath(staff, "Messages.Errors.downAdminLevelError");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("adown")) {
            Bukkit.getLogger().info("AdminDownCommand | COMMAND: /adown. Команда прошла успешно.");
            staffService.downStatus(staff.getUniqueId(), admin.getUniqueId());
        }

        return true;
    }
}
