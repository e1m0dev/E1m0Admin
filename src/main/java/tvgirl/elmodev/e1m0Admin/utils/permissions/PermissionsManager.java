package tvgirl.elmodev.e1m0Admin.utils.permissions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.utils.PermissionsManagerAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.state.access.AdminCommandManager;
import tvgirl.elmodev.e1m0Admin.state.access.AdminCommandState;

import java.util.List;
import java.util.UUID;

public class PermissionsManager implements PermissionsManagerAPI {
    private final AdminSystemRepository systemRepository;
    private final AdminCommandManager commandManager;
    private final FileConfiguration cfg;

    public PermissionsManager(AdminSystemRepository systemRepository, AdminCommandManager commandManager, FileConfiguration cfg) {
        this.systemRepository = systemRepository;
        this.commandManager = commandManager;
        this.cfg = cfg;
    }

    // TODO: Система - полная херь, переработать на CommandEnum у администраторов
    //  Причина простая, я хочу полную адаптивность, а эта хуня из под коня не
    //   заточена под нее, короче сделать Enum + проверки так проще будет

    // TODO: Сделать просто HashMap pinCode или State manager Secret который хранит кэш и управлять через permissions

    @Override
    public void getAccess(UUID id) {
        ConfigurationSection adminSection = cfg.getConfigurationSection("Admin.AdminRanks");
        int systemWeight = systemRepository.getAdminWeight(id);

        for(String section : adminSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + section + ".weight");
            if(!(systemWeight == cfgWeight)) continue;

            List<String> commands = cfg.getStringList("Admin.AdminRanks." + section + ".commands");
            AdminCommandState commandState = new AdminCommandState(id, commands);

            commandManager.addAdminAccess(commandState);
        }
    }

    @Override
    public boolean checkAccessCommand(UUID id, String command) {
        Player p = Bukkit.getPlayer(id);
        commandManager.getAdminByID(id);

        return commandManager.getCommandAccess(id, command);
    }
}