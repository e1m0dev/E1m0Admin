package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;

public class InvisibilityCommand implements CommandExecutor {

    private final FileConfiguration cfg;
    private final AdminGameService service;

    public InvisibilityCommand(FileConfiguration cfg, AdminGameService service) {
        this.cfg = cfg;
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player p)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if(command.getName().toLowerCase().equalsIgnoreCase("invise")) {
            if(p.hasPermission(cfg.getString("Permissions.invisibility"))) {
                service.handleInvisibility(p.getUniqueId());
            }
        }

        return true;
    }
}
