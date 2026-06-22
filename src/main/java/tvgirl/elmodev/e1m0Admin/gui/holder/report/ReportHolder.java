package tvgirl.elmodev.e1m0Admin.gui.holder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ReportHolder implements InventoryHolder {

    //  Не забыть: DATA - GUI | HOLDER - ACTIONS -> CONTROLLER (Listener) ❗ - SERVICE - REPO | Доменная модель; Командная.

    private final Player adm;
    private final String name;
    private final String answer;

    public ReportHolder(String name, Player adm, String answer) {
        this.name = name;
        this.adm = adm;
        this.answer = answer;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 27, "holder");
        return inv;
    }
}
