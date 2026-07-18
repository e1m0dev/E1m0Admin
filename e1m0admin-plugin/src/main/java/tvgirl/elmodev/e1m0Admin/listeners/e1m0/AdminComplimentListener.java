package tvgirl.elmodev.e1m0Admin.listeners.e1m0;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tvgirl.elmodev.e1m0Admin.event.AdminComplimentEvent;
import tvgirl.elmodev.e1m0Admin.service.AdminSystemService;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

public class AdminComplimentListener implements Listener {

    private final E1m0Sender sender;
    private final FileConfiguration cfg;
    private final AdminSystemService systemService;

    public AdminComplimentListener(E1m0Sender sender, FileConfiguration cfg, AdminSystemService systemService) {
        this.systemService = systemService;
        this.sender = sender;
        this.cfg = cfg;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSendCompliment(AdminComplimentEvent e) {
        Player admin = Bukkit.getPlayer(e.getAdminID());
        Player player = Bukkit.getPlayer(e.getPlayerID());
        String compliments = String.valueOf(e.getCompliments());

        sender.sendPath(admin, "Messages.newCompliment",
                "%player", player.getName(),
                "%compliments", compliments);

        sender.sendPath(player, "Messages.successfulSendCompliment",
                "%admin", admin.getName());

        systemService.autoComplimentActions(e.getAdminID());
    }
}