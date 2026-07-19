package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class AccessCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final SecretCodeService service;
    private final SecretCodeGui secretCodeGui;
    private final E1m0Permission permissionManager;

    public AccessCommand(E1m0Sender sender, FileConfiguration cfg, SecretCodeService service, SecretCodeGui secretCodeGui, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.secretCodeGui = secretCodeGui;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        if (!permissionManager.checkSystem(admin.getUniqueId())) return false;

        boolean checkPermission = permissionManager.checkSecretCodeAccess(admin.getUniqueId());
        if (checkPermission) {
            sender.sendPath(admin, "Messages.Errors.secretCodeHasInputted");
            return false;
        }

        String permission = cfg.getString("Permissions.admin");
        if (!admin.hasPermission(permission)) {
            sender.sendPath(admin, "Messages.Errors.permissionError");
            return false;
        }

        if (!service.checkSecret(admin.getUniqueId())) {
            sender.sendPath(admin, "Messages.Errors.notCode");
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("aaccess")) {
            secretCodeGui.openPINGui(admin.getUniqueId());
        }
        return true;
    }

}
