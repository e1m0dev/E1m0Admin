package tvgirl.elmodev.e1m0Admin.utils.permissions;

import org.bukkit.configuration.file.FileConfiguration;
import tvgirl.elmodev.e1m0Admin.api.utils.PermissionsManagerAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;

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

    // TODO: Система - полная херь, переработать на CommandEnum у администраторов
    //  Причина простая, я хочу полную адаптивность, а эта хуня из под коня не
    //   заточена под нее, короче сделать Enum + проверки так проще будет

    // TODO: Сделать просто HashMap pinCode или State manager Secret который хранит кэш и управлять через permissions

    @Override
    public boolean checkSecretCodeAccess(UUID id) {
        return codeManager.getAdminByID(id) != null;
    }
}