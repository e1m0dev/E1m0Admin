package tvgirl.elmodev.e1m0Admin.listeners.e1m0;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tvgirl.elmodev.e1m0Admin.event.AdminDelEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminDelListener implements Listener {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminSystemService systemService;

    public AdminDelListener(E1m0Sender sender, FileConfiguration cfg, AdminSystemService systemService) {
        this.systemService = systemService;
        this.sender = sender;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAdminRegistered(AdminDelEvent e) {
        systemService.autoDelAdmin(e.getAdminID(), e.getStaffID());
    }
}