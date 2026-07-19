package tvgirl.elmodev.e1m0Admin.gui.guis.secretcode;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.configuration.file.FileConfiguration;
import org.checkerframework.checker.units.qual.C;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.gui.SecretCodeGuiAPI;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;
import tvgirl.elmodev.e1m0Admin.gui.holder.secretcode.SecretCodeHolder;

import java.util.List;
import java.util.UUID;

public class SecretCodeGui implements SecretCodeGuiAPI {

    private final SecretCodeService codeService;
    private final NamespacedKey actionKey;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;
    private final E1m0Color color;

    public SecretCodeGui(SecretCodeService codeService, NamespacedKey actionKey, FileConfiguration cfg, E1m0Sender sender, E1m0Color color) {
        this.codeService = codeService;
        this.actionKey = actionKey;
        this.sender = sender;
        this.color = color;
        this.cfg = cfg;
    }

    @Override
    public void openPINGui(UUID id) {
        int size = cfg.getInt("Admin.GUI.SecretGUI.SIZE");

        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_pin"), size, "PIN: ****");
        Player p = Bukkit.getPlayer(id);

        createItemsInInventory(i, id);
        p.openInventory(i);
    }

    public void openTwoStepGUI(UUID id) {
        int size = cfg.getInt("Admin.GUI.SecretGUI.SIZE");
        String inputCode = codeService.getInputCode(id);

        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_two"), size, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createItemsInInventory(i, id);
        p.openInventory(i);
    }

    public void openThreeStepGUI(UUID id) {
        int size = cfg.getInt("Admin.GUI.SecretGUI.SIZE");

        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_three"), size, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createItemsInInventory(i, id);
        p.openInventory(i);
    }

    public void openFoursStepGUI(UUID id) {
        int size = cfg.getInt("Admin.GUI.SecretGUI.SIZE");

        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_fours"), size, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createItemsInInventory(i, id);
        p.openInventory(i);
    }

    private void createItemsInInventory(Inventory i, UUID id) {
        ConfigurationSection configGUI = cfg.getConfigurationSection("Admin.GUI.SecretGUI.items");

        for (String s : configGUI.getKeys(false)) {
            // Достаю все нужное из конфига по кастом ключам.
            String action = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".action");
            String name = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".name");
            int slot = cfg.getInt("Admin.GUI.SecretGUI.items." + s + ".slot");

            List<String> lore = cfg.getStringList("Admin.GUI.SecretGUI.items." + s + ".lore");
            String materialString = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".item");

            // Объявленные.
            Material mat = Material.valueOf(materialString);
            ItemStack item = new ItemStack(mat);

            ItemMeta itemMeta = item.getItemMeta();

            // Достаю все нужное из конфига по кастом ключам.
            if (materialString.equalsIgnoreCase("PLAYER_HEAD")) {
                String method = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".method");

                if (method.equalsIgnoreCase("base64")) {

                    // | Если это base64
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    String configBase64 = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".base64");

                    // Если есть BASE64 но нет base64:
                    if (configBase64 == null || configBase64.isEmpty()) {
                        sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.configSkullGuiError");
                    }

                    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

                    profile.setProperty(new ProfileProperty("textures", configBase64));
                    skullMeta.setPlayerProfile(profile);
                    item.setItemMeta(skullMeta);

                    i.setItem(slot, creteSkullItem(name, action, item, lore));
                } else if (method.equalsIgnoreCase("owner")) {
                    // | Если это голова.
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

                    String configSkullOwner = cfg.getString("Admin.GUI.SecretGUI.items." + s + ".skull_owner");

                    // Если есть PLAYER_HEAD но нет skull_owner
                    if (configSkullOwner == null || configSkullOwner.isEmpty()) {
                        sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.configSkullGuiError");
                    }

                    OfflinePlayer skullOwner = Bukkit.getOfflinePlayer(configSkullOwner);

                    skullMeta.setOwningPlayer(skullOwner);
                    item.setItemMeta(skullMeta);

                    // Создаем предмет.
                    i.setItem(slot, creteSkullItem(name, action, item, lore));
                } else {
                    sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.invalidSkullMethod");
                }
            } else {
                i.setItem(slot, createButtonItem(name, action, mat, 1, lore));
            }
        }
    }

    private ItemStack creteSkullItem(String name, String action, ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(
                actionKey,
                PersistentDataType.STRING,
                action);

        meta.displayName(color.parse(name));
        meta.lore(color.parse(lore));

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createButtonItem(String name, String action, Material mat, int i, List<String> lore) {
        ItemStack item = new ItemStack(mat, i);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(
                actionKey,
                PersistentDataType.STRING,
                action);

        meta.displayName(color.parse(name));
        meta.lore(color.parse(lore));

        item.setItemMeta(meta);
        return item;
    }
}
