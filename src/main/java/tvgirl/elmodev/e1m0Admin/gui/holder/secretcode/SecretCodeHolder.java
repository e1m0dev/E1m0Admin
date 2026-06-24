package tvgirl.elmodev.e1m0Admin.gui.holder.secretcode;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SecretCodeHolder implements InventoryHolder {

    private final String name;

    public SecretCodeHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
