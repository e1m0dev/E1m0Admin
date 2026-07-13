package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class ABlockCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;

    public ABlockCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.aban");
        if (!isAllowed) {
            return false;
        }

        String permission = cfg.getString("Permissions.admin");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        if (strings.length != 1) {
            sender.sendPath(admin, "Messages.Errors.lengthError");
            return false;
        }

        Player targetAdmin = Bukkit.getPlayer(strings[0]);

        if (targetAdmin == null) {
            sender.sendPath(admin, "Messages.Errors.nullPlayer");
        }

        String permissionAdmin = cfg.getString("Permissions.admin");
        if (!(targetAdmin.hasPermission(permissionAdmin))) {
            sender.sendPath(admin, "Messages.Errors.notAdmin");
        }

        Bukkit.getLogger().warning("!adminBlockAccess! / 1 "); // Тестер

        if (command.getName().toLowerCase().equalsIgnoreCase("ablock")) {
            service.adminBlockAccess(targetAdmin.getUniqueId(), admin.getUniqueId());
            Bukkit.getLogger().warning("!adminBlockAccess! / 2 "); // Тестер

            // CLS | Console Log
            boolean isActive = cfg.getBoolean("Settings.consoleLogActive");
            if (isActive) {
                sender.sendConsole(Bukkit.getConsoleSender(), "Messages.ConsoleLogs.ABlockLog",
                        "%admin", admin.getName(),
                        "%target", targetAdmin.getName()
                );
            }
        }

        return true;
    }
}
