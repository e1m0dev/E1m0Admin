package tvgirl.elmodev.e1m0Admin.utils.Message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tvgirl.elmodev.e1m0Admin.api.utils.SenderAPI;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;

public class E1m0Sender implements SenderAPI {

    private final FileConfiguration cfg;
    private E1m0Color color = new E1m0Color();

    public E1m0Sender(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @Override
    public void sendString(@NotNull Player p, @NotNull String str) {
        String text = PlaceholderAPI.setPlaceholders(p, str);
        p.sendMessage(color.parse(cfg.getString("Settings.prefix") + " " + text));
    }

    @Override
    public void sendPath(@NotNull Player p,  @NotNull String path) {
        String text = PlaceholderAPI.setPlaceholders(p, path);
        p.sendMessage(color.parse(cfg.getString("Settings.prefix") + " " + cfg.getString(path)));
    }

}
