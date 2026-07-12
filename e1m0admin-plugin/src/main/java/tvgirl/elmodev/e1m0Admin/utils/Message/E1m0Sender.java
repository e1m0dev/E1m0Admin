package tvgirl.elmodev.e1m0Admin.utils.Message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;

import java.util.List;

public class E1m0Sender {

    private final FileConfiguration cfg;
    private E1m0Color color = new E1m0Color();

    public E1m0Sender(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    public void sendStringList(@NotNull Player sendedPlayer, @NotNull List<String> messageList, @Nullable String... replacements) {
        for (String message : messageList) {
            String text = PlaceholderAPI.setPlaceholders(sendedPlayer, message);

            if (replacements != null) {
                for (int i = 0; i < replacements.length; i += 2) {
                    message = message.replace(replacements[i], replacements[i + 1]);
                }
            }

            String prefix = cfg.getString("Settings.prefixEnable");

            if (cfg.getBoolean("Settings.prefixEnable")) {
                if (sendedPlayer == null) return;
                sendedPlayer.sendMessage(color.parse(prefix + " " + text));
            } else {
                if (sendedPlayer == null) return;
                sendedPlayer.sendMessage(color.parse(text));
            }
        }
    }

    public void sendPath(@NotNull Player sendedPlayer, @NotNull String path, @Nullable String... replacements) {
        List<String> cfgMessage = cfg.getStringList(path);

        // E1m0:
        // Если не будет принимать обновление строковое добавитть Arrays, типо по одной строке.
        // Если будет больше двух строк - обычный перебор, если одна - Arrays
        for (String message : cfgMessage) {

            String text = PlaceholderAPI.setPlaceholders(sendedPlayer, message);

            if (replacements != null) {
                for (int i = 0; i < replacements.length; i += 2) {
                    message = message.replace(replacements[i], replacements[i + 1]);
                }
            }

            String prefix = cfg.getString("Settings.prefixEnable");

            if (cfg.getBoolean("Settings.prefixEnable")) {
                if (sendedPlayer == null) return;
                sendedPlayer.sendMessage(color.parse(prefix + " " + cfgMessage));
            } else {
                if (sendedPlayer == null) return;
                sendedPlayer.sendMessage(color.parse(message));
            }
        }
    }

    public void sendConsole(CommandSender sender, @NotNull String path, @Nullable String... replacements) {
        List<String> cfgMessage = cfg.getStringList(path);

        for (String message : cfgMessage) {
            if (replacements != null) {
                for (int i = 0; i < replacements.length; i += 2) {
                    message = message.replace(replacements[i], replacements[i + 1]);
                }
            }

            // TODO: Делать ли систему отправки в API?

            sender.sendMessage(message);
        }
    }
}
