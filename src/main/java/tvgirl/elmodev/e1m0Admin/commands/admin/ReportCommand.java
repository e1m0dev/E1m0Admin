package tvgirl.elmodev.e1m0Admin.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminGameService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class ReportCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminGameService service;

    public ReportCommand(E1m0Sender sender, FileConfiguration cfg, AdminGameService service) {
        this.service = service;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player adm)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        // /arep Администратор E1m0 спешит к Вам на помощь! | Или другая какая-либо форма.
        if(command.getName().toLowerCase().equalsIgnoreCase("arep")) {
            String response = String.join(" ", strings[0]);

            if(strings.length > 2) {
                sender.sendPath(adm, "Messages.Errors.lengthError");
                return false;
            }

            if(adm.hasPermission(cfg.getString("Permissions.rewatch"))) {
                service.openReportGUI(adm.getUniqueId(), response);
            }
        }

        return true;
    }
}
