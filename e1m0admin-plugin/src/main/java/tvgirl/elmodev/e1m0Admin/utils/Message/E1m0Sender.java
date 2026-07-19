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

    private final FileConfiguration mainCfg;
    private final FileConfiguration messageCfg;
    private final E1m0Color color = new E1m0Color();

    public E1m0Sender(FileConfiguration mainCfg, FileConfiguration messageCfg) {
        this.mainCfg = mainCfg;
        this.messageCfg = messageCfg;
    }

    public void sendStringList(@NotNull Player player, @NotNull List<String> messageList, @Nullable String... replacements) {
        for (String message : messageList) {
            sendMessage(player, message, replacements);
        }
    }

    public void sendPath(@NotNull Player player, @NotNull String path, @Nullable String... replacements) {
        String message = messageCfg.getString(path);
        List<String> messages = messageCfg.getStringList(path);

        if (messageCfg.isString(path)) {
            sendMessage(player, message, replacements);
            return;
        }

        if (messageCfg.isList(path)) {
            for (String string : messages) {
                sendMessage(player, string, replacements);
            }
            return;
        }

        sendMessage(player, messageCfg.getString("Messages.Errors.sendInternalError"),
                "%path", path);
    }

    public void sendPathCfg(@NotNull Player player, @NotNull String path, @Nullable String... replacements) {
        if (mainCfg.isString(path)) {
            sendMessage(player, mainCfg.getString(path), replacements);
            return;
        }

        if (mainCfg.isList(path)) {
            for (String message : mainCfg.getStringList(path)) {
                sendMessage(player, message, replacements);
            }
        }
    }

    public void sendConsole(@NotNull CommandSender sender, @NotNull String path, @Nullable String... replacements) {
        if (messageCfg.isString(path)) {
            sendConsoleMessage(sender, messageCfg.getString(path), replacements);
            return;
        }

        if (messageCfg.isList(path)) {
            for (String message : messageCfg.getStringList(path)) {
                sendConsoleMessage(sender, message, replacements);
            }
        }
    }

    private void sendMessage(@NotNull Player player, @Nullable String message, @Nullable String... replacements) {

        if (message == null) {
            return;
        }

        if (replacements != null) {
            for (int i = 0; i < replacements.length - 1; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        message = PlaceholderAPI.setPlaceholders(player, message);

        if (mainCfg.getBoolean("Settings.prefixEnable")) {
            String prefix = mainCfg.getString("Settings.prefix", "");
            message = prefix + " " + message;
        }

        player.sendMessage(color.parse(message));
    }

    private void sendConsoleMessage(@NotNull CommandSender sender, @Nullable String message, @Nullable String... replacements) {

        if (message == null) {
            return;
        }

        if (replacements != null) {
            for (int i = 0; i < replacements.length - 1; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        sender.sendMessage(color.parse(message));
    }
}