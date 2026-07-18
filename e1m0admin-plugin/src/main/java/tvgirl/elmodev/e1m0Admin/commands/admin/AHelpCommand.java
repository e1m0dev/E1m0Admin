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

public class AHelpCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;

    public AHelpCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        String permission = cfg.getString("Permissions.admin");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        Bukkit.getLogger().warning("ahelp / 1!"); // ТЕСТЕР

        if (command.getName().toLowerCase().equalsIgnoreCase("ahelp")) {
            service.adminHelp(admin.getUniqueId());

            // CLS | Console Log
            boolean isActive = cfg.getBoolean("Settings.consoleLogActive");
            if (isActive) {
                sender.sendConsole(Bukkit.getConsoleSender(), "Messages.ConsoleLogs.aHelpLog",
                        "%admin", admin.getName());
            }
        }

        return true;
    }
}
