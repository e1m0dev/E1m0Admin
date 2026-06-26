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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClickToPin(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof SecretCodeHolder holder)) return;
        Inventory inv = e.getClickedInventory();

        if (!(e.getWhoClicked() instanceof Player p)) return;

        e.setCancelled(true);
        if (inv == null) {
            return;
        }

        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) {
            return;
        }

        ItemStack item = e.getCurrentItem();
        String action = item.getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);

        UUID playerID = e.getWhoClicked().getUniqueId();
        byte num = handlerButton(action);

        if(num == 0) {
            p.closeInventory();
            return;
        }

        Bukkit.getLogger().info("SecretCodeController | Точка входа GUI-CONTROLLER: Администратор нажал: " + num + " Этап: " + holder.getName()); // ТЕСТЕР

        switch (holder.getName()) {
            case "step_one" -> {
                codeService.oneStepHandler(playerID, num);
                codeGUI.openTwoStepGUI(playerID, num);
            }
            case "step_two" -> {
                codeService.twoStepHandler(playerID, num);
                codeGUI.openThreeStepGUI(playerID, num);
            }
            case "step_three" -> {
                codeService.threeStepHandler(playerID, num);
                codeGUI.openFoursStepGUI(playerID, num);
            }
            case "step_fours" -> codeService.foursStepHandler(playerID, num);
        }
    }

    private byte handlerButton(String action) {
        byte i = 0;

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
