package tvgirl.elmodev.e1m0Admin.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.state.admin.AdminSessionManager;

public class QuitListener implements Listener {

    private final FileConfiguration cfg;
    private final AdminSystemService systemService;
    private final AdminSessionManager sessionManager;

    public QuitListener(FileConfiguration cfg, AdminSystemService systemService, AdminSessionManager sessionManager) {
        this.cfg = cfg;
        this.systemService = systemService;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAdminJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission(cfg.getString("Permissions.admin"))) return;

        sessionManager.quit(p.getUniqueId());
    }
}
