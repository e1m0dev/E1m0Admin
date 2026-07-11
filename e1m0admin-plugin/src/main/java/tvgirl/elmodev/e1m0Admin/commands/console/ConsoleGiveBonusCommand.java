package tvgirl.elmodev.e1m0Admin.commands.console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.Arrays;
import java.util.UUID;

public class ConsoleGiveBonusCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final ConsoleService consoleService;

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    public ConsoleGiveBonusCommand(E1m0Sender sender, FileConfiguration cfg, ConsoleService consoleService) {
        this.cfg = cfg;
        this.sender = sender;
        this.consoleService = consoleService;
    }

    // $cbonusall E1m0 1 Удачи!
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (strings.length < 3) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        String[] messageArray = Arrays.copyOfRange(strings, 2, strings.length);
        String message = String.join(" ", messageArray);
        Player admin = Bukkit.getPlayer(strings[0]);
        int sum = Integer.parseInt(strings[1]);

        if (admin == null) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.nullPlayer"));
            return false;
        }

        if (message.isEmpty()) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        if (sum < 0) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        if (command.getName().toLowerCase().equalsIgnoreCase("cbonus")) {
            consoleService.giveBonusConsole(consoleID, admin.getUniqueId(), sum, message);
        }

        return true;
    }
}
