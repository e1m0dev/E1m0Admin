package tvgirl.elmodev.e1m0Admin.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class ThanksCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;

    public ThanksCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service) {
        this.service = service;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        boolean isAllowed = cfg.getBoolean("Server.compliment");
        if (!isAllowed) {
            return false;
        }

        String permission = cfg.getString("Permissions.compliment");
        if (!player.hasPermission(permission)) {
            sender.sendPath(player, "Messages.Errors.permissionError");
            return false;
        }

        if (strings.length != 1) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        Player admin = Bukkit.getPlayer(strings[0]);
        String adminPermission = cfg.getString("Permissions.admin");

        if (!admin.hasPermission(permission)) {
            sender.sendPath(player, "Messages.Errors.notAdmin");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("thanks")) {
            service.addCompliment(admin.getUniqueId(), player.getUniqueId());
        }

        return true;
    }

}
