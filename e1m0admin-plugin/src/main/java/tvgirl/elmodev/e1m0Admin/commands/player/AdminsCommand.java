package tvgirl.elmodev.e1m0Admin.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminsCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;

    public AdminsCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service) {
        this.sender = sender;
        this.cfg = cfg;
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.consoleError");
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.admins");
        if (!isAllowed) {
            return false;
        }

        String permission = cfg.getString("Permissions.admins");
        if (!player.hasPermission(permission)) {
            sender.sendPath(player, "Messages.Errors.permissionError");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("admins")) {
            service.getAdminList(player.getUniqueId());
        }

        return true;
    }

}
