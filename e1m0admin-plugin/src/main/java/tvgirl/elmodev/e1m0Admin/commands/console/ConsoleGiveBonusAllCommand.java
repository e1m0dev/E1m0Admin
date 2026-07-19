package tvgirl.elmodev.e1m0Admin.commands.console;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.Arrays;
import java.util.UUID;

public class ConsoleGiveBonusAllCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final ConsoleService consoleService;

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    public ConsoleGiveBonusAllCommand(E1m0Sender sender, FileConfiguration cfg, ConsoleService consoleService) {
        this.cfg = cfg;
        this.sender = sender;
        this.consoleService = consoleService;
    }

    // $cbonusall 1 Удачи!
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof ConsoleCommandSender)) {
            sender.sendConsole(commandSender, "Messages.Errors.playerConsoleError");
            return false;
        }

        if (strings.length < 1) {
            sender.sendConsole(commandSender, "Messages.Errors.lengthError");
            return false;
        }

        String[] messageArray = Arrays.copyOfRange(strings, 1, strings.length);
        String message = String.join(" ", messageArray);
        int sum = Integer.parseInt(strings[0]);

        if (message.isEmpty()) {
            sender.sendConsole(commandSender, "Messages.Errors.lengthError");
            return false;
        }

        if (sum < 0) {
            sender.sendConsole(commandSender, "Messages.Errors.lengthError");
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("cbonusall")) {
            consoleService.giveBonusAllConsole(consoleID, sum, message);
        }

        return true;
    }
}
