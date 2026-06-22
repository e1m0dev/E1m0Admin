package tvgirl.elmodev.e1m0Admin.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.state.AdminSession;
import tvgirl.elmodev.e1m0Admin.state.AdminSessionManager;

public class JoinListener implements Listener {

    private final FileConfiguration cfg;
    private final AdminSystemService systemService;
    private final AdminSessionManager sessionManager;

    public JoinListener(FileConfiguration cfg, AdminSystemService systemService, AdminSessionManager sessionManager) {
        this.cfg = cfg;
        this.systemService = systemService;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAdminJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission(cfg.getString("Permissions.admin"))) return;

        // Админ зарплата.
        if (cfg.getBoolean("Server.adminPay")) {
            sessionManager.join(p.getUniqueId());
            systemService.adminPay(p.getUniqueId());
        }
    }
}
