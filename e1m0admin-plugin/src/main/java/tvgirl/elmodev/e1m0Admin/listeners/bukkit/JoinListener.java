package tvgirl.elmodev.e1m0Admin.listeners.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class JoinListener implements Listener {

    private final E1m0Sender send;
    private final FileConfiguration cfg;
    private final AdminSessionManager sessionManager;

    public JoinListener(E1m0Sender send, FileConfiguration cfg, AdminSessionManager sessionManager) {
        this.send = send;
        this.cfg = cfg;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAdminJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission(cfg.getString("Permissions.admin"))) return;

        if (cfg.getBoolean("Server.adminPay")) {
            sessionManager.join(p.getUniqueId());
        }
    }
}
