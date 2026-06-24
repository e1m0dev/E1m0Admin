package tvgirl.elmodev.e1m0Admin.api.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SenderAPI {

    void sendString(@NotNull Player p, @NotNull String str);
    void sendPath(@NotNull Player p,  @NotNull String path);

}
