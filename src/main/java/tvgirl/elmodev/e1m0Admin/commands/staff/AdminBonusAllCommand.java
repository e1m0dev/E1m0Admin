    package tvgirl.elmodev.e1m0Admin.commands.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminsStaffService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminBonusAllCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;

    public AdminBonusAllCommand(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
    }

    // /abonusall E1m0 Работаем негры
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player staff)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("adminbonusall")) {
            if(strings.length < 2) {
                sender.sendPath(staff, "Messages.Errors.lengthError");
                return false;
            }

            int sum = Integer.parseInt(strings[1]);
            String message = String.join(" ", strings[2]);

            if(staff.hasPermission(cfg.getString("Permissions.invisibility"))) {
                staffService.adminBonusAll(staff.getUniqueId(), sum, message);
            }
        }

        return true;
    }
}
