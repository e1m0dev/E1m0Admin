package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0admin.api.service.ConsoleServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class ConsoleService implements ConsoleServiceAPI {

    private final SecretCodeRepository secretCodeRepository;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    public ConsoleService(SecretCodeRepository secretCodeRepository, AdminSystemRepository systemRepository, AdminStaffRepository staffRepository, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.cfg = cfg;
        this.sender = sender;
    }

    @Override
    public void setSecretConsole(UUID adminID, UUID consoleID, int code) {
        Player admin = Bukkit.getPlayer(adminID);

        secretCodeRepository.systemSetSecretCode(adminID, code);

        sender.sendPath(admin, "Messages.changeCodeAdmin",
                "%staff", "CONSOLE",
                "%code", String.valueOf(code));
    }

    @Override
    public void setAdminConsole(UUID adminID, UUID consoleID, int weight) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player admin = Bukkit.getPlayer(adminID);

        if (systemRepository.checkAdminInBase(adminID)) {
            sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.setAdminIsAdminError"));
            return;
        }

        // ConfigSection | Как обычно перебираю конфиг секции.
        for (String key : ranksSection.getKeys(false)) {
            // ConfigSection | Нахожу по weight ранг - и работаю с его ключом.
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight) {

                // Определяю зарплату и префикс
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                // Отправляю в репо.
                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());
                sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.successfulAdminSet"));
                return;
            } else {
                sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.setAdminWeightError"));
                return;
            }
        }
    }

    @Override
    public void upAdminConsole(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.upAdminNotAdminError"));
            return;
        }

        String rankKey = null;
        int rankWeight = 0;

        if (admin == null) return;

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");

            if (weightBase == cfgWeight) {
                rankKey = key;
                rankWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (rankKey == null) return;
        String currentKey = null;

        int targetWeight = rankWeight + 1;
        int currentWeight = 0;


        for (String key : ranksSection.getKeys(false)) {
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(admin, "Messages.Errors.upAdminLevelError");
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newWeight = cfg.getInt("Admin.AdminRanks." + currentKey + ".weight");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        staffRepository.upAdminStatus(adminID, newPrefix, newWeight, newSalary);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.successfulAdminUp"));
    }

    @Override
    public void downAdminConsole(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.upAdminNotAdminError"));
            return;
        }

        String rankKey = null;
        int rankWeight = 0;

        if (admin == null) return;

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
            if (weightBase == cfgWeight) {
                rankKey = key;
                rankWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (rankKey == null) return;
        String currentKey = null;

        int targetWeight = rankWeight - 1;
        int currentWeight = 0;

        if (targetWeight < 1) {
            sender.sendPath(admin, "Messages.Errors.downAdminLevelError", "", "");
            return;
        }

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
            Bukkit.getLogger().info("Config Weight: " + cfgWeight);

            if (cfgWeight == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(admin, "Messages.Errors.downAdminLevelError", "", "");
        }

        int newWeight = cfg.getInt("Admin.AdminRanks." + currentKey + ".weight");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");
        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");

        staffRepository.downAdminStatus(adminID, newPrefix, newWeight, newSalary);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.successfulAdminDown"));
    }

    @Override
    public void delAdminConsole(UUID adminID, UUID consoleID, String reason) {
        Player admin = Bukkit.getPlayer(adminID);

        staffRepository.deleteAdminStatus(adminID);
        secretCodeRepository.systemDeleteAdmin(adminID);
        staffRepository.systemDeleteAdminStatusLog(adminID, consoleID, reason);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.successfulAdminDelete"));
    }
}
