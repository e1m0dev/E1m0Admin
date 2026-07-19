package tvgirl.elmodev.e1m0Admin.listeners.e1m0;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tvgirl.elmodev.e1m0Admin.event.AdminLeakEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminLeakListener implements Listener {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final FileConfiguration messageCfg;
    private final AdminSystemService systemService;
    private final SecretCodeManager secretCodeManager;

    public AdminLeakListener(E1m0Sender sender, FileConfiguration cfg, FileConfiguration messageCfg, AdminSystemService systemService, SecretCodeManager secretCodeManager) {
        this.cfg = cfg;
        this.sender = sender;
        this.messageCfg = messageCfg;
        this.systemService = systemService;
        this.secretCodeManager = secretCodeManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSecurityNotify(AdminLeakEvent e) {
        Player leak = Bukkit.getPlayer(e.getStaffID());
        Player admin = Bukkit.getPlayer(e.getAdminID());

        sender.sendConsole(Bukkit.getConsoleSender(), messageCfg.getString(e.getLeakMessage()), // | Отправляю лог в CLS
                "%leak", leak.getName(),
                "%admin", admin.getName());

        systemService.autoLeakActions(e.getAdminID(), e.getStaffID()); // | Выполняю действия из конфига к админу (ам)
        secretCodeManager.takeAdminAccess(e.getStaffID()); // | Отбираю доступ у администратора за попытку вмешательства.
        sendLeakMessageAdmins(e.getLeakMessage()); // | Активным админам - выдаю сообщение о подозрении в сливе из лога консоли.
    }

    private void sendLeakMessageAdmins(String leakMessagePath) {
        String adminPermission = cfg.getString("Permissions.admin");

        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (!admin.hasPermission(adminPermission)) continue;

            sender.sendPath(admin, leakMessagePath);
        }
    }
}