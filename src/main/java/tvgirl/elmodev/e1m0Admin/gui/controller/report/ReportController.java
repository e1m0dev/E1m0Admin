package tvgirl.elmodev.e1m0Admin.gui.controller.report;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tvgirl.elmodev.e1m0Admin.gui.holder.report.ReportHolder;
import tvgirl.elmodev.e1m0Admin.service.gui.ReportSystemService;

import java.util.UUID;

public class ReportController implements Listener {

    private final NamespacedKey reportIdKey;
    private final NamespacedKey actionKey;

    private final ReportSystemService reportService;

    public ReportController(NamespacedKey reportIdKey, NamespacedKey actionKey, ReportSystemService reportService) {
        this.actionKey = actionKey;
        this.reportIdKey = reportIdKey;
        this.reportService = reportService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClickToReport(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof ReportHolder holder)) {
            return;
        }

        e.setCancelled(true);
        if (e.getClickedInventory() == null) {
            return;
        }

        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) {
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        String action = meta.getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);

        if (!"open_report".equals(action)) {
            return;
        }

        String rawUuid = meta.getPersistentDataContainer().get(reportIdKey, PersistentDataType.STRING);
        if (rawUuid == null) {
            return;
        }

        Bukkit.getLogger().info("SecretCodeController | Точка входа GUI-CONTROLLER: Администратор взял репорт"); // ТЕСТЕР
        UUID reportUuid = UUID.fromString(rawUuid);
        reportService.clickToReport(e.getWhoClicked().getUniqueId(), reportUuid, holder.getAnswer());
    }
}
