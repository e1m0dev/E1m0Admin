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

        String permission = cfg.getString("Permissions.rewatch");
        if (admin.hasPermission(permission)) {
            Bukkit.getLogger().info("RewatchCommand | Точка входа COMMAND: /rewatch была введена и пропущена. Вызов обработчика: handleRewatch | Следящий режим"); // ТЕСТЕР
        } else {
            Bukkit.getLogger().info("RewatchCommand | У мужлан нет прав"); // ТЕСТЕР
        }

        // /reoff
        if (command.getName().toLowerCase().equalsIgnoreCase("reoff")) {
            Bukkit.getLogger().info("RewatchCommand | Точка входа COMMAND: /rewatch была введена и пропущена. Вызов обработчика: handleReoff | Админ-режим"); // ТЕСТЕР
            service.handleReoff(admin.getUniqueId());
            return true;
        }

        String user = strings[0];

        Player player = Bukkit.getPlayer(user);
        if (player == null) {
            sender.sendPath(admin, "Messages.Errors.nullPlayer");
            return false;
        }

        if (strings.length < 1) {
            sender.sendPath(admin, "Messages.Errors.lengthError");
            return false;
        }

        // /re E1m0 || /rewatch E1m0
        if (command.getName().toLowerCase().equalsIgnoreCase("re") || command.getName().toLowerCase().equalsIgnoreCase("rewatch")) {
            service.handleRewatch(admin.getUniqueId(), player.getUniqueId());
        }

        return true;
    }
}
