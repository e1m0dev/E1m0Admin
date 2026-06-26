package tvgirl.elmodev.e1m0Admin.tabcompliter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        List<String> tab = new ArrayList<>();

        if (!sender.hasPermission(cfg.getString("Permissions.admin"))) {

            if (strings.length == 1) {
                tab.add("report");
            } else if(strings.length == 2) {
                tab.add("Message");
            }

            return tab;
        }

        String sub = strings.length > 0 ? strings[0].toLowerCase() : "";

        if (strings.length == 1) {
            // 🧑‍🔬 | Admin
            tab.add("ainv");
            tab.add("arec");
            tab.add("arep");
            tab.add("areoff");

            if (sender.hasPermission(cfg.getString("Permissions.staff"))) {
                // 🧑‍🔬 | STAFF
                tab.add("aup");
                tab.add("aset");
                tab.add("adown");
                tab.add("abonus");
                tab.add("abonusall");
            }
        } else if (strings.length == 2) {

            switch (sub) {
                case "arep": tab.add("Form");

                case "aup":
                case "adel":
                case "aset":
                case "adown":
                case "abonus":
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission(cfg.getString("Permissions.admin"))) {
                            tab.add(player.getName());
                        }
                    }

                break;
            }
        } else if (strings.length == 3) {

            switch (sub) {

                case "aset":
                    tab.add("Уровень/Weight?");
                    break;

                case "abonus":
                case "abonusall":
                    tab.add("10");
                    tab.add("100");
                    tab.add("777?");
                    break;
            }
        } else if (strings.length == 4) {

            switch (sub) {

                case "abonus":
                case "abonusall":
                    tab.add("Message?");
                    break;
            }
        }

        String current = strings[strings.length - 1].toLowerCase();

        return tab.stream()
                .filter(s -> s.toLowerCase().startsWith(current))
                .toList();
    }
}