package tvgirl.elmodev.e1m0Admin.utils.permissions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.utils.PermissionsManagerAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class E1m0Permission implements PermissionsManagerAPI {
    private final AdminSystemRepository systemRepository;
    private final SecretCodeManager codeManager;
    private final FileConfiguration cfg;

    public E1m0Permission(AdminSystemRepository systemRepository, SecretCodeManager codeManager, FileConfiguration cfg) {
        this.systemRepository = systemRepository;
        this.codeManager = codeManager;
        this.cfg = cfg;
    }

    @Override
    public boolean checkSecretCodeAccess(UUID id) {
        Bukkit.getLogger().info("checkSecretCodeAccess | Сейчас тут"); // ТЕСТЕР
        SecretCodeState state = codeManager.getAdminByID(id);

        if (state == null) {
            return false;
        }

        Bukkit.getLogger().warning("AdminUUID: " + state.getAdminID());
        return true;
    }
}