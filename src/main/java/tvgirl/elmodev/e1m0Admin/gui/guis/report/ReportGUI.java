package tvgirl.elmodev.e1m0Admin.gui.guis.report;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportGUI implements ReportGuiAPI {

    private final Map<UUID, Report> playerReportCache;

    private final ReportSystemService reportService;
    private final FileConfiguration cfg;
    private final E1m0Admin plugin;

    private final NamespacedKey reportIdKey;
    private final NamespacedKey actionKey;

    private final E1m0Sender sender;

    private E1m0Color color = new E1m0Color();

    public ReportGUI(Map<UUID, Report> playerReportCache, NamespacedKey reportIdKey, NamespacedKey actionKey, ReportSystemService reportService, FileConfiguration cfg, E1m0Admin plugin, E1m0Sender sender) {
        this.playerReportCache = playerReportCache;
        this.reportService = reportService;
        this.reportIdKey = reportIdKey;
        this.actionKey = actionKey;
        this.plugin = plugin;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void openReportGUI(UUID adminID, String response) {
        Player adm = Bukkit.getPlayer(adminID);
        List<Report> reportList = new ArrayList<>();
        ReportHolder holder = new ReportHolder("report_holder", adm, response);

        ConfigurationSection reportGui = cfg.getConfigurationSection("Admin.Report.GUI.ReportGUI.items");

        int size = cfg.getInt("Admin.Report.GUI.ReportGUI.SIZE");
        String nameMenu = cfg.getString("Admin.Report.GUI.ReportGUI.NAME");
        String finalNameMenu = PlainTextComponentSerializer.plainText().serialize(color.parse(nameMenu));

        Inventory inv = Bukkit.createInventory(holder, size, finalNameMenu);

        // ⌚ | Репорты
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Map.Entry<UUID, Report> report : playerReportCache.entrySet()) reportList.add(report.getValue());

            if (reportList.isEmpty()) {
                sender.sendPath(adm, "Messages.Errors.noReportsInMemory");
                adm.closeInventory();
                return;
            }

            for (Report reps : reportList) {
                Bukkit.getLogger().warning(reps.getReport());
            }

            int slot = 0;

            int maxSize = cfg.getInt("Admin.Report.reportMaxSize");
            if (slot >= maxSize) {
                return;
            }

            for (Report report : reportList) {
                // Сам репорт

                List<String> preLore = cfg.getStringList("Admin.Report.GUI.ReportItem.LORE"); // Stream
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

        // ▶️ | Кнопки
        for(String s : reportGui.getKeys(false)) {
            ItemStack item = new ItemStack(Material.valueOf(cfg.getString("Admin.Report.GUI.ReportGUI.items." + s + ".item")));
            ItemMeta meta = item.getItemMeta();

            // ❗ Ревью | Требуется горничная, 2.0
            List<String> lore = cfg.getStringList("Admin.Report.GUI.ReportGUI.items." + s + ".lore"); // Stream
            String itemName = cfg.getString("Admin.Report.GUI.ReportGUI.items." + s + ".name");
            int itemSlot = cfg.getInt("Admin.Report.GUI.ReportGUI.items." + s + ".slot");

            String finalItemName = PlainTextComponentSerializer.plainText().serialize(color.parse(itemName));

            meta.setDisplayName(finalItemName);
            meta.setLore(lore); //TODO: Не красит цвета, решить в 2.0

            item.setItemMeta(meta);
            inv.setItem(itemSlot, item);
        }

        adm.openInventory(inv);
    }

    // Допы

    private ItemStack createReportItem(Report report, List<String> lore) {

        for (String slore : lore) {
            Bukkit.getLogger().warning("SLORE2 : " + slore);
        }

        String material = cfg.getString("Admin.Report.GUI.ReportItem.ITEM");
        ItemStack item = new ItemStack(Material.valueOf(material), 1);

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(
                reportIdKey,
                PersistentDataType.STRING,
                report.getUuid().toString());

        meta.getPersistentDataContainer().set(
                actionKey,
                PersistentDataType.STRING,
                "open_report");

        String reportName = cfg.getString("Admin.Report.GUI.ReportItem.NAME");

        LegacyComponentSerializer legacy = LegacyComponentSerializer.legacySection();
        List<Component> component = color.parse(lore);

        // TODO: Мне это очень не нравится, поправить, сделать всего 1 парсер а не 200.
        List<String> coloredLore = color.parse(lore).stream()
                .map(legacy::serialize)
                .toList();

        meta.setDisplayName(reportName
                .replace("%content", report.getReport()));
        meta.setLore(coloredLore);

        item.setItemMeta(meta);
        return item;
    }

    private String formatter(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        return formatter.format(Instant.ofEpochMilli(report.getCreatedAt()));
    }
}
