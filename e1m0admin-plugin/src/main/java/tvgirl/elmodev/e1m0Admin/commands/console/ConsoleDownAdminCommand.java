package tvgirl.elmodev.e1m0Admin.commands.console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class ConsoleDownAdminCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final ConsoleService consoleService;

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    public ConsoleDownAdminCommand(E1m0Sender sender, ConsoleService consoleService) {
        this.sender = sender;
        this.consoleService = consoleService;
    }

    // $cdel E1m0 1
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            sender.sendConsole(commandSender, "Messages.Errors.playerConsoleError");
            return false;
        }

        if (strings.length != 1) {
            sender.sendConsole(commandSender, "Messages.Errors.lengthError");
            return false;
        }

        Player admin = Bukkit.getPlayer(strings[0]);

        if (admin == null) {
            sender.sendConsole(commandSender, "Messages.Errors.nullPlayer");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("cdown")) {
            consoleService.downAdminConsole(admin.getUniqueId(), consoleID);
        }

        return true;
    }
}
