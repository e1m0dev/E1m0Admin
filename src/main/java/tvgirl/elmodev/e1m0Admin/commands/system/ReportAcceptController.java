package tvgirl.elmodev.e1m0Admin.commands.system;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class ReportAcceptController implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminSystemService service;

    public ReportAcceptController(E1m0Sender sender, FileConfiguration cfg, AdminSystemService service) {
        this.sender = sender;
        this.cfg = cfg;
        this.service = service;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        UUID id = UUID.fromString(strings[0]);

        if (id == null) {
            return false;
        }

        if (!(commandSender instanceof Player admin)) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.consoleError", "Консоли нельзя выполнять такую команду!"));
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("arepaccept")) {
            service.handleReportAccept(admin.getUniqueId(), id);
        }

        return true;
    }
}
