package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.event.AdminDelEvent;
import tvgirl.elmodev.e1m0Admin.event.AdminSetEvent;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0admin.api.service.ConsoleServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class ConsoleService implements ConsoleServiceAPI {

    private final SecretCodeRepository secretCodeRepository;
    private final AdminSessionManager adminSessionManager;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final SecretCodeManager secretCodeManager;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    public ConsoleService(SecretCodeRepository secretCodeRepository, AdminSessionManager adminSessionManager, AdminSystemRepository systemRepository, AdminStaffRepository staffRepository, SecretCodeManager secretCodeManager, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.adminSessionManager = adminSessionManager;
        this.secretCodeManager = secretCodeManager;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void setSecretConsole(UUID adminID, UUID consoleID, int code) {
        Player admin = Bukkit.getPlayer(adminID);

        secretCodeRepository.systemSetSecretCode(adminID, code);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.successfulAdminSetSecret"));

        sender.sendPath(admin, "Messages.changeCodeAdmin",
                "%staff", cfg.getString("Settings.consolePrefix"),
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

        boolean inBlackList = staffRepository.checkAdminBlockList(adminID);
        if (inBlackList) {
            sender.sendConsole(Bukkit.getServer().getConsoleSender(), "Message.Errors.setAdminInBlackList");
            return;
        }

        // ConfigSection | Как обычно перебираю конфиг секции.
        for (String key : ranksSection.getKeys(false)) {
            // ConfigSection | Нахожу по weight ранг - и работаю с его ключом.
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight) {

                // Определяю зарплату и префикс
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                // Отправляю в репо + закрепляю в кэше + ивент.
                adminSessionManager.update(adminID, prefix, weight, salary);
                Bukkit.getPluginManager().callEvent(new AdminSetEvent(adminID, consoleID, weight));
                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());
                sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.successfulAdminSet"));
            } else {
                sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.Errors.setAdminWeightError"));
            }
            return;
        }
    }

    @Override
    public void consoleBanAdminAccess(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);
        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (!isBlockedAdmin) {
            staffRepository.setAdminABan(adminID, consoleID);
        } else {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.adminBanned");
        }

        sender.sendPath(admin, "Messages.bannedSystem",
                "%staff", cfg.getString("Settings.consolePrefix"));

        sender.sendConsole(Bukkit.getConsoleSender(), "Messages.successfulBannedSystem",
                "%admin", admin.getName());
    }

    @Override
    public void consoleUnBanAdminAccess(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);

        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);

        if (isBlockedAdmin) {
            staffRepository.delAdminABan(adminID);
        } else {
            sender.sendConsole(Bukkit.getConsoleSender(), "Messages.Errors.adminNotBanned");
        }

        sender.sendPath(admin, "Messages.unbannedSystem",
                "%staff", cfg.getString("Settings.consolePrefix"));

        sender.sendConsole(Bukkit.getConsoleSender(), "Messages.successfulUnbannedSystem",
                "%admin", admin.getName());
    }

    @Override
    public void upAdminConsole(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.upAdminNotAdminError"));
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

        adminSessionManager.update(adminID, newPrefix, newWeight, newSalary);
        staffRepository.upAdminStatus(adminID, newPrefix, newWeight, newSalary);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.successfulAdminUp"));
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

        adminSessionManager.update(adminID, newPrefix, newWeight, newSalary);
        staffRepository.downAdminStatus(adminID, newPrefix, newWeight, newSalary);
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.successfulAdminDown"));
    }

    @Override
    public void giveBonusAllConsole(UUID consoleID, int sum, String message) {
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (!admin.hasPermission(cfg.getString("Permissions.admin"))) continue;

            String action = cfg.getString("Admin.Bonus.giveBonus")
                    .replace("%admin", admin.getName())
                    .replace("%bonus", String.valueOf(sum));

            sender.sendPath(admin, "Messages.adminBonus",
                    "%message", message,
                    "%staff", cfg.getString("Settings.consolePrefix"),
                    "%bonus", String.valueOf(sum));

            staffRepository.giveBonusLog(admin.getUniqueId(), consoleID, sum, message);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        }
    }

    @Override
    public void giveBonusConsole(UUID consoleID, UUID adminID, int sum, String message) {
        Player admin = Bukkit.getPlayer(adminID);

        if (!admin.hasPermission(cfg.getString("Permissions.admin"))) {
            return;
        }

        String action = cfg.getString("Admin.Bonus.giveBonus")
                .replace("%admin", admin.getName())
                .replace("%bonus", String.valueOf(sum));

        sender.sendPath(admin, "Messages.adminBonusAll",
                "%message", message,
                "%staff", cfg.getString("Settings.consolePrefix"),
                "%bonus", String.valueOf(sum));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        staffRepository.giveBonusLog(adminID, consoleID, sum, message);
    }

    @Override
    public void delAdminConsole(UUID adminID, UUID consoleID, String reason) {
        Player admin = Bukkit.getPlayer(adminID);

        adminSessionManager.quit(adminID);
        staffRepository.deleteAdminStatus(adminID);
        secretCodeRepository.systemDeleteAdmin(adminID);
        staffRepository.systemDeleteAdminStatusLog(adminID, consoleID, reason);
        Bukkit.getPluginManager().callEvent(new AdminDelEvent(adminID, consoleID));
        sender.sendConsole(Bukkit.getConsoleSender(), cfg.getString("Messages.successfulAdminDelete"));
    }
}
