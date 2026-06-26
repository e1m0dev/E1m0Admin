package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

public class RewatchCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;
    private final E1m0Permission permissionManager;

    public RewatchCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service, E1m0Permission permissionManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.service = service;
        this.permissionManager = permissionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if (!(permissionManager.checkSecretCodeAccess(admin.getUniqueId()))) {
            sender.sendPath(admin, "Messages.Errors.secretCodeNotInput");
            return false;
        }

        if(strings.length < 2) {
            sender.sendPath(admin, "Messages.Errors.lengthError");
            return false;
        }

        // /arec E1m0 || /rewatch E1m0
        if (command.getName().toLowerCase().equalsIgnoreCase("arec")) {
            String user = strings[1];
            Player player = Bukkit.getPlayer(user);

            if (admin.hasPermission(cfg.getString("Permissions.rewatch"))) {
                service.handleRewatch(admin.getUniqueId(), player.getUniqueId());
                Bukkit.getLogger().info("RewatchCommand | Точка входа COMMAND: /rewatch была введена и пропущена. Вызов обработчика: handleRewatch | Следящий режим"); // ТЕСТЕР

            }
        } else if (command.getName().toLowerCase().equalsIgnoreCase("areoff")) {
            if (admin.hasPermission(cfg.getString("Permissions.rewatch"))) {
                service.handleReoff(admin.getUniqueId());
                Bukkit.getLogger().info("RewatchCommand | Точка входа COMMAND: /rewatch была введена и пропущена. Вызов обработчика: handleReoff | Админ-режим"); // ТЕСТЕР
            }
        }

        return true;
    }
}
