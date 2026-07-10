package tvgirl.elmodev.e1m0Admin.commands.console;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.service.ConsoleService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class ConsoleSetSecretCommand implements CommandExecutor {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final ConsoleService consoleService;

    private UUID consoleID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    public ConsoleSetSecretCommand(E1m0Sender sender, FileConfiguration cfg, ConsoleService consoleService) {
        this.cfg = cfg;
        this.sender = sender;
        this.consoleService = consoleService;
    }

    // $csetsecret E1m0 6969
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (strings.length != 2) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.lengthError"));
            return false;
        }

        Player admin = Bukkit.getPlayer(strings[0]);

        if (admin == null) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.nullPlayer"));
            return false;
        }

        String strCode = strings[1];

        if (strCode.length() < 4) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.setAdminCodeWrong"));
            return false;
        }

        if (strCode.length() > 4) {
            sender.sendConsole(commandSender, cfg.getString("Messages.Errors.setAdminCodeWrong"));
            return false;
        }

        Integer code = Integer.parseInt(strCode);

        if (command.getName().toLowerCase().equalsIgnoreCase("csetsecret")) {
            consoleService.setSecretConsole(admin.getUniqueId(), consoleID, code);
        }

        return true;
    }
}
