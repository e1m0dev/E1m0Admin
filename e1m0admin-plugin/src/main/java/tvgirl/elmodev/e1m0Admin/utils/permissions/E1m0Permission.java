package tvgirl.elmodev.e1m0Admin.utils.permissions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0admin.api.state.secretcode.SecretCodeState;
import tvgirl.elmodev.e1m0admin.api.utils.PermissionsManagerAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;

import java.util.HashSet;
import java.util.UUID;

public class E1m0Permission implements PermissionsManagerAPI {
    private final AdminSystemRepository systemRepository;
    private final SecretCodeManager codeManager;
    private final HashSet<UUID> blockedAdmins;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    public E1m0Permission(AdminSystemRepository systemRepository, SecretCodeManager codeManager, HashSet<UUID> blockedAdmins, FileConfiguration cfg, E1m0Sender sender) {
        this.systemRepository = systemRepository;
        this.blockedAdmins = blockedAdmins;
        this.codeManager = codeManager;
        this.sender = sender;
        this.cfg = cfg;
    }


    @Override
    public boolean checkSystem(UUID id) {
        if (blockedAdmins.contains(id)) {
            sender.sendPath(Bukkit.getPlayer(id), "Messages.Errors.youAdminAccessIsBlocked");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkSecretCodeAccess(UUID id) {
        SecretCodeState state = codeManager.getAdminByID(id);
        return state != null;
    }
}