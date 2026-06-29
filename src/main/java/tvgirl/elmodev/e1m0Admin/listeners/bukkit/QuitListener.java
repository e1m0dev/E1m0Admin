package tvgirl.elmodev.e1m0Admin.listeners.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;

public class QuitListener implements Listener {

    private final FileConfiguration cfg;
    private final AdminSystemService systemService;
    private final AdminSessionManager sessionManager;
    private final SecretCodeManager secretCodeManager;

    public QuitListener(FileConfiguration cfg, AdminSystemService systemService, AdminSessionManager sessionManager, SecretCodeManager secretCodeManager) {
        this.cfg = cfg;
        this.systemService = systemService;
        this.sessionManager = sessionManager;
        this.secretCodeManager = secretCodeManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAdminJoin(PlayerQuitEvent e) {
        Player admin = e.getPlayer();
        if (!admin.hasPermission(cfg.getString("Permissions.admin"))) return;

        sessionManager.quit(admin.getUniqueId());
        secretCodeManager.takeAdminAccess(admin.getUniqueId());
    }
}
