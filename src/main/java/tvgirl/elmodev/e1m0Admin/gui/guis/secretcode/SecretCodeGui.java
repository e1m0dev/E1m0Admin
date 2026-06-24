package tvgirl.elmodev.e1m0Admin.gui.guis.secretcode;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.api.gui.SecretCodeGuiAPI;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;
import tvgirl.elmodev.e1m0Admin.gui.holder.secretcode.SecretCodeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecretCodeGui implements SecretCodeGuiAPI {

    private final SecretCodeService codeService;
    private final NamespacedKey actionKey;
    private final FileConfiguration cfg;

    public SecretCodeGui(SecretCodeService codeService, NamespacedKey actionKey, FileConfiguration cfg) {
        this.codeService = codeService;
        this.actionKey = actionKey;
        this.cfg = cfg;
    }

    @Override
    public void openPINGui(UUID id) {
        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_one"), 54, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createInventory(i);
        p.openInventory(i);
    }

    public void openOneStepGui(UUID id, byte lastCode) {
        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_one"), 54, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createInventory(i);
        p.openInventory(i);
    }

    public void openTwoStepGUI(UUID id, byte lastCode) {
        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_one"), 54, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createInventory(i);
        p.openInventory(i);
    }

    public void openThreeStepGUI(UUID id, byte lastCode) {
        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_one"), 54, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createInventory(i);
        p.openInventory(i);
    }

    public void openFoursStepGUI(UUID id, byte lastCode) {
        Inventory i = Bukkit.createInventory(new SecretCodeHolder("step_one"), 54, "PIN: " + codeService.getInputCode(id));
        Player p = Bukkit.getPlayer(id);

        createInventory(i);
        p.openInventory(i);
    }

    private void createInventory(Inventory i) {

        // TODO
        //  Головы - Добавить поддержку форматтера?

        List<String> loreNumber = new ArrayList<>();
        loreNumber.add("Просто нажмите для введения цифры.");

        i.setItem(2, createButtonItem("Цифра: 1", "BUTTON_ONE", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(4, createButtonItem("Цифра: 2", "BUTTON_TWO", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(6, createButtonItem("Цифра: 3", "BUTTON_THREE", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(20, createButtonItem("Цифра: 4", "BUTTON_FOUR", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(22, createButtonItem("Цифра: 5", "BUTTON_FIVE", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(24, createButtonItem("Цифра: 6", "BUTTON_SIX", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(38, createButtonItem("Цифра: 7", "BUTTON_SEVEN", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(40, createButtonItem("Цифра: 8", "BUTTON_EIGHT", Material.PLAYER_HEAD, 1, loreNumber));
        i.setItem(42, createButtonItem("Цифра: 9", "BUTTON_NINE", Material.PLAYER_HEAD, 1, loreNumber));

        List<String> loreClose = new ArrayList<>();
        loreNumber.add("Просто нажмите для введения цифры.");
        createButtonItem("Закрыть", "CLOSE", Material.BARRIER,  49, loreClose);
    }

    private ItemStack createButtonItem(String name, String action, Material mat, int i, List<String> lore) {
        ItemStack item = new ItemStack(mat, i);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(
                actionKey,
                PersistentDataType.STRING,
                action);

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
