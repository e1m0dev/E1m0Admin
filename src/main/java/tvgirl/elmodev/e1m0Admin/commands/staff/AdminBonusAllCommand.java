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

    public class AdminBonusAllCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminsStaffService staffService;
        private final E1m0Permission permissionManager;

        public AdminBonusAllCommand(E1m0Sender sender, FileConfiguration cfg, AdminsStaffService staffService, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.staffService = staffService;
            this.permissionManager = permissionManager;
    }

        // /abonusall 777 Работаем негры
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

        if (command.getName().toLowerCase().equalsIgnoreCase("abonusall")) {
            if(strings.length < 2) {
                sender.sendPath(staff, "Messages.Errors.lengthError");
                return false;
            }

            int sum = Integer.parseInt(strings[1]);
            String message = String.join(" ", strings[2]);

            if (staff.hasPermission(cfg.getString("Permissions.adminbonusall"))) {
                Bukkit.getLogger().info("AdminBonusAllCommand | Точка входа COMMAND: /abonusall была введена и пропущена. Вызов обработчика: adminBonusAll"); // ТЕСТЕР
                staffService.adminBonusAll(staff.getUniqueId(), sum, message);
            }
        }

        return true;
    }
}
