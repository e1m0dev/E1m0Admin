package tvgirl.elmodev.e1m0Admin.listeners.e1m0;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tvgirl.elmodev.e1m0Admin.event.AdminAccessEvent;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminAccessListener implements Listener {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;

    public AdminAccessListener(E1m0Sender sender, FileConfiguration cfg) {
        this.sender = sender;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdminRegistered(AdminAccessEvent e) {
        Player admin = e.getAdmin();

        /*
         TODO 2.0:
           ❗Actions
           ❗Skins
           ❗ Смотри логирование TODO.md, я просто не решил че это будет..
        */

        sender.sendPath(admin, "Messages.secretCodeAccess");
    }
}