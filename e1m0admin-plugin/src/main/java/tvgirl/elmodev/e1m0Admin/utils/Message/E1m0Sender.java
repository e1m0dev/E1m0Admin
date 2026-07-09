package tvgirl.elmodev.e1m0Admin.utils.Message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;

public class E1m0Sender {

    private final FileConfiguration cfg;
    private E1m0Color color = new E1m0Color();

    public E1m0Sender(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    public void sendString(@NotNull Player sendedPlayer, @NotNull String message, @Nullable String... replacements) {
        String text = PlaceholderAPI.setPlaceholders(sendedPlayer, message);

        if (replacements != null) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        String prefix = cfg.getString("Settings.prefixEnable");

        if (cfg.getBoolean("Settings.prefixEnable")) {
            sendedPlayer.sendMessage(color.parse(prefix + " " + text));
        } else {
            sendedPlayer.sendMessage(color.parse(text));
        }
    }

    public void sendPath(@NotNull Player sendedPlayer, @NotNull String path, @Nullable String... replacements) {
        String cfgMessage = cfg.getString(path);
        String text = PlaceholderAPI.setPlaceholders(sendedPlayer, cfgMessage);

        if (replacements != null) {
            for (int i = 0; i < replacements.length; i += 2) {
                cfgMessage = cfgMessage.replace(replacements[i], replacements[i + 1]);
            }
        }

        String prefix = cfg.getString("Settings.prefixEnable");

        if (cfg.getBoolean("Settings.prefixEnable")) {
            sendedPlayer.sendMessage(color.parse(prefix + " " + cfgMessage));
        } else {
            sendedPlayer.sendMessage(color.parse(cfgMessage));
        }
    }
}
