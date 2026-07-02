package tvgirl.elmodev.e1m0Admin.listeners.bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.state.report.Report;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;

import java.util.Map;
import java.util.UUID;

public class QuitListener implements Listener {

    private final FileConfiguration cfg;
    private final AdminSystemService systemService;
    private final AdminSessionManager sessionManager;
    private final SecretCodeManager secretCodeManager;

    private final Map<UUID, Report> playerReportCache;

    public QuitListener(FileConfiguration cfg, AdminSystemService systemService, AdminSessionManager sessionManager, SecretCodeManager secretCodeManager, Map<UUID, Report> playerReportCache) {
        this.cfg = cfg;
        this.systemService = systemService;
        this.sessionManager = sessionManager;
        this.secretCodeManager = secretCodeManager;
        this.playerReportCache = playerReportCache;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onUserJoin(PlayerQuitEvent e) {
        Player user = e.getPlayer();

        if (playerReportCache.containsKey(user.getUniqueId())) {
            playerReportCache.remove(user.getUniqueId());
        }

        if (!user.hasPermission(cfg.getString("Permissions.admin"))) return;
        Player admin = user; // Это больше для понимания если listener будет расти.

        sessionManager.quit(admin.getUniqueId());
        secretCodeManager.takeAdminAccess(admin.getUniqueId());
    }
}
