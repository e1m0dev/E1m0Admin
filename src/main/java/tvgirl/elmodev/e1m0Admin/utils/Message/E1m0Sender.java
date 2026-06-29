package tvgirl.elmodev.e1m0Admin.utils.Message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tvgirl.elmodev.e1m0Admin.api.utils.SenderAPI;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;

public class E1m0Sender implements SenderAPI {

    private final FileConfiguration cfg;
    private E1m0Color color = new E1m0Color();

    public E1m0Sender(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @Override
    public void sendString(@NotNull Player sendedPlayer, @NotNull String message, @Nullable String... replacements) {
        String text = PlaceholderAPI.setPlaceholders(sendedPlayer, message);

        if (replacements != null) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        sendedPlayer.sendMessage(color.parse(cfg.getString("Settings.prefix") + " " + text));
    }

    @Override
    public void sendPath(@NotNull Player sendedPlayer, @NotNull String path, @Nullable String... replacements) {
        String text = PlaceholderAPI.setPlaceholders(sendedPlayer, path);

        if (replacements != null) {
            for (int i = 0; i < replacements.length; i += 2) {
                path = path.replace(replacements[i], replacements[i + 1]);
            }
        }

        sendedPlayer.sendMessage(color.parse(cfg.getString("Settings.prefix") + " " + cfg.getString(path)));
    }
}
