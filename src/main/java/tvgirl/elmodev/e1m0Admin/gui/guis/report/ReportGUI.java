package tvgirl.elmodev.e1m0Admin.gui.guis.report;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tvgirl.elmodev.e1m0Admin.E1m0Admin;
import tvgirl.elmodev.e1m0Admin.api.gui.ReportGuiAPI;
import tvgirl.elmodev.e1m0Admin.gui.holder.report.ReportHolder;
import tvgirl.elmodev.e1m0Admin.service.gui.ReportSystemService;
import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ReportGUI implements ReportGuiAPI {

    private final ReportSystemService reportService;
    private final FileConfiguration cfg;
    private final E1m0Admin plugin;

    private final NamespacedKey reportIdKey;
    private final NamespacedKey actionKey;

    public ReportGUI(NamespacedKey reportIdKey, NamespacedKey actionKey, ReportSystemService reportService, FileConfiguration cfg, E1m0Admin plugin) {
        this.reportService = reportService;
        this.reportIdKey = reportIdKey;
        this.actionKey = actionKey;
        this.plugin = plugin;
        this.cfg = cfg;
    }

    @Override
    public void openReportGUI(UUID adminID, String response) {
        Player adm = Bukkit.getPlayer(adminID);
        ReportHolder holder = new ReportHolder("report_holder", adm, response);

        ConfigurationSection reportGui = cfg.getConfigurationSection("Admin.Report.GUI.ReportGui.items");
        Inventory inv = Bukkit.createInventory(holder, cfg.getInt("Admin.Report.GUI.ReportGUI.SIZE"), cfg.getString("Admin.Report.GUI.ReportGUI.NAME", "&cМеню репортов"));

        // ⌚ | Репорты
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<Report> reportList = reportService.getReports();

            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Report report : reportList) {
                    int slot = 0;

                    // Сам репорт
                    List<String> preLore = cfg.getStringList("Admin.Report.GUI.ReportGUI.ReportItem.FORM"); // Stream
                    List<String> lore = preLore.stream()
                            .map(line -> line
                                    .replace("%content", report.getReport())
                                    .replace("%status", report.getStatus())
                                    .replace("%player", report.getPlayerNick())
                                    .replace("%date", formatter(report)))
                            .toList();

                    inv.setItem(slot, createReportItem(report, lore));
                    slot++;
                }
            });
        });

        // ▶️ | Кнопки
        for(String s : reportGui.getKeys(false)) {
            ItemStack item = new ItemStack(Material.valueOf(cfg.getString("Admin.Report.GUI.ReportGui.items." + s + ".item")));
            int slot = cfg.getInt("Admin.Report.GUI.ReportGui.items." + s + ".slot", 0);
            inv.setItem(slot, item);
        }

        adm.openInventory(inv);
    }

    // Допы

    private ItemStack createReportItem(Report report, List<String> lore) {
        ItemStack item = new ItemStack(
                Material.valueOf(cfg.getString("Admin.Report.GUI.ReportGUI.ReportItem.ITEM")),
                1);

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(
                reportIdKey,
                PersistentDataType.STRING,
                report.getUuid().toString());

        meta.getPersistentDataContainer().set(
                actionKey,
                PersistentDataType.STRING,
                "open_report");

        meta.setDisplayName(cfg.getString("Admin.Report.GUI.ReportGUI.NAME"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private String formatter(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        return formatter.format(Instant.ofEpochMilli(report.getCreatedAt()));
    }
}
