package tvgirl.elmodev.e1m0Admin.gui.controller.secretcode;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import tvgirl.elmodev.e1m0Admin.gui.guis.secretcode.SecretCodeGui;
import tvgirl.elmodev.e1m0Admin.gui.holder.secretcode.SecretCodeHolder;
import tvgirl.elmodev.e1m0Admin.service.gui.SecretCodeService;

import java.util.UUID;

public class SecretCodeController implements Listener {

    private final SecretCodeGui codeGUI;
    private final NamespacedKey actionKey;
    private final SecretCodeService codeService;

    public SecretCodeController(SecretCodeGui codeGUI, NamespacedKey actionKey, SecretCodeService codeService) {
        this.codeGUI = codeGUI;
        this.actionKey = actionKey;
        this.codeService = codeService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickToPin(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv == null) return;

        if (!(inv.getHolder() instanceof SecretCodeHolder holder)) return;
        e.setCancelled(true);

        Bukkit.getLogger().warning(holder.getName() + " Это holder]");

        if (!(e.getWhoClicked() instanceof Player p)) return;

        if (inv == null) {
            return;
        }

        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        Bukkit.getLogger().warning(item + " Это предмет");

        String action = item.getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);

        Bukkit.getLogger().warning(action + " Это действие");

        UUID playerID = e.getWhoClicked().getUniqueId();
        int num = handlerButton(action);

        if(num == 0) {
            p.closeInventory();
            return;
        }

        Bukkit.getLogger().info("SecretCodeController | Точка входа COMMAND-GUI-CONTROLLER: Администратор нажал: " + num + " Этап: " + num); // ТЕСТЕР

        switch (holder.getName().toLowerCase()) {
            case "step_pin" -> {
                Bukkit.getLogger().warning("STEP_PIN");
                codeService.oneStepHandler(playerID, num);
                codeGUI.openTwoStepGUI(playerID);
            }
            case "step_two" -> {
                Bukkit.getLogger().warning("step_two");
                codeService.twoStepHandler(playerID, num);
                codeGUI.openThreeStepGUI(playerID);
            }
            case "step_three" -> {
                Bukkit.getLogger().warning("step_three");
                codeService.threeStepHandler(playerID, num);
                codeGUI.openFoursStepGUI(playerID);
            }
            case "step_fours" -> {
                Bukkit.getLogger().warning("step_fours");
                codeService.foursStepHandler(playerID, num);
            }
        }
    }

    // Если ошибка любая - переписать byte - int
    private int handlerButton(String action) {
        int i = 0;

        switch (action) {
            case "BUTTON_ONE":
                return i = 1;
            case "BUTTON_TWO":
                return i = 2;
            case "BUTTON_THREE":
                return i = 3;
            case "BUTTON_FOUR":
                return i = 4;
            case "BUTTON_FIVE":
                return i = 5;
            case "BUTTON_SIX":
                return i = 6;
            case "BUTTON_SEVEN":
                return i = 7;
            case "BUTTON_EIGHT":
                return i = 8;
            case "BUTTON_NINE":
                return i = 9;
            case "CLOSE":
                return i = 0;
        }

        return i;
    }
}
