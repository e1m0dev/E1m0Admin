package tvgirl.elmodev.e1m0Admin.commands.console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.AdminsStaffService;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

import java.util.UUID;

public class ConsoleSetAdminCommand implements CommandExecutor {

    private final FileConfiguration cfg;
    private final ConsoleService consoleService;

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    public ConsoleSetAdminCommand(FileConfiguration cfg, ConsoleService consoleService) {
        this.cfg = cfg;
        this.consoleService = consoleService;
    }

    // $csetadmin E1m0 1
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length != 2) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        Player admin = Bukkit.getPlayer(strings[0]);
        int weight = Integer.parseInt(strings[1]);

        if (weight <= 0) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.setAdminWeightIsNull"));
            return false;
        }

        if (admin == null) {
            commandSender.sendMessage(cfg.getString("Messages.Errors.nullPlayer"));
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("csetadmin")) {
            consoleService.setAdminConsole(admin.getUniqueId(), consoleID, weight);
        }

        return true;
    }
}
