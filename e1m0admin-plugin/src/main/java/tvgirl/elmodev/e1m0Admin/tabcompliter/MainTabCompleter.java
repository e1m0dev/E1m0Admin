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


        if (sender.hasPermission(cfg.getString("Permissions.admin"))) {
            switch (command.getName().toLowerCase()) {

                case "admins":
                case "ahelp":
                    if (strings.length == 1) {
                        tab.add("Больше ничего не надо?");
                        tab.add("Просто /admins");
                        tab.add("Просто /ahelp");
                    }

                    break;

                case "rep":
                case "reps":
                case "arep":
                    if (strings.length == 1) {
                        tab.add("Answer?");
                    }

                    break;

                case "thanks":
                    if (strings.length == 1) {
                        getOnlinePlayers(tab);
                    }

                case "cunban":
                case "aunban":
                case "aban":
                case "cban":
                    if (strings.length == 1) {
                        getOnlineAdmins(tab);
                    }

                    break;

                case "report":
                    if (strings.length == 1) {
                        getOnlinePlayers(tab);
                    } else if (strings.length == 2) {
                        tab.add("Reason?");
                    }

                    break;

                case "cbonus":
                case "abonus":
                    if (strings.length == 1) {
                        getOnlineAdmins(tab);
                    } else if (strings.length == 2) {
                        tab.add("10?");
                        tab.add("100?");
                        tab.add("1000?");
                    } else if (strings.length == 3) {
                        tab.add("Message?");
                    }

                    break;

                case "cbonusall":
                case "abonusall":
                    if (strings.length == 1) {
                        tab.add("10?");
                        tab.add("100?");
                        tab.add("1000?");
                    } else if (strings.length == 2) {
                        tab.add("Message?");
                    }

                    break;

                case "adel":
                case "cdel":
                    if (strings.length == 1) {
                        getOnlineAdmins(tab);
                    } else if (strings.length == 2) {
                        tab.add("Reason del?");
                    }

                    break;

                case "aset":
                case "csetadmin":
                    if (strings.length == 1) {
                        getOnlinePlayers(tab);
                    } else if (strings.length == 2) {
                        tab.add("Weight?");
                    }

                    break;


                case "csetsecret":
                case "setsecret":
                case "asecret":
                    if (strings.length == 1) {
                        getOnlineAdmins(tab);
                    } else if (strings.length == 2) {
                        tab.add("Code?");
                    }

                    break;

                case "cup":
                case "cdown":

                case "aup":
                case "adown":
                    if (strings.length == 1) {
                        getOnlineAdmins(tab);
                    }

                    break;

                case "re":
                case "arec":
                case "recon":
                    if (strings.length == 1) {
                        getOnlinePlayers(tab);
                    }

                    break;
            }
        }
        return tab;
    }

    private void getOnlinePlayers(List<String> tab) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            tab.add(player.getName());
        }
    }

    private void getOnlineAdmins(List<String> tab) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(cfg.getString("Permissions.admin"))) {
                tab.add(player.getName());
            }
        }
    }
}