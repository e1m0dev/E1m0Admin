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

public class AdminChangeSecretCode implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;

    public AdminChangeSecretCode(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService) {
        this.staffService = staffService;
        this.sender = sender;
        this.cfg = cfg;
    }

    // /asecret E1m0 7777
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player staff)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if(strings.length < 2) {
            sender.sendPath(staff, "Messages.Errors.lengthError");
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("acodechange")) {
            Player admin = Bukkit.getPlayer(strings[0]);

            if(admin == null) {
                sender.sendPath(staff, "Messages.Errors.nullPlayer");
                return false;
            }

            if(staff.hasPermission(cfg.getString("Permissions.invisibility"))) {
                staffService.setAdmin(staff.getUniqueId(), admin.getUniqueId(), weight);
            }
        }

        return true;
    }
}
