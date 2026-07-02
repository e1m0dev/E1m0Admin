package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class AccessCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final SecretCodeGui secretCodeGui;
    private final E1m0Permission permissionManager;

    public AccessCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service, SecretCodeGui secretCodeGui, E1m0Permission permissionManager) {
        this.sender = sender;
        this.cfg = cfg;
        this.service = service;
        this.secretCodeGui = secretCodeGui;
        this.permissionManager = permissionManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        String permission = cfg.getString("Permissions.admin");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("aaccess")) {
            Bukkit.getLogger().info("AccessCommand | Точка входа COMMAND: /aacess прошла регистрацию."); // ТЕСТЕР
            Bukkit.getLogger().info("AccessCommand | Точка входа COMMAND: /aacess была введена и пропущена."); // ТЕСТЕР
            secretCodeGui.openPINGui(admin.getUniqueId());
        }
        return true;
    }

}
