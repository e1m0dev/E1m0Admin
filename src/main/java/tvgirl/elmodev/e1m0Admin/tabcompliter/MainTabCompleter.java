package tvgirl.elmodev.e1m0Admin.commands.tabcompliter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {

    private final FileConfiguration cfg;

    public MainTabCompleter(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> tab = new ArrayList<>();

        // TODO: Дополнить!

        if(commandSender.hasPermission(cfg.getString("Permissions.admin"))) {
            tab.add("invise");
            tab.add("rewatch");
        }

        return tab;

    }
}
