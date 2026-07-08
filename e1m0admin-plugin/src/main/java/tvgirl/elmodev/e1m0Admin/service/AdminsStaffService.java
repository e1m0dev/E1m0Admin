package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0admin.api.service.StaffServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class AdminsStaffService implements StaffServiceAPI {

    private final SecretCodeRepository secretCodeRepository;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;


    public AdminsStaffService(SecretCodeRepository secretCodeRepository, AdminStaffRepository staffRepository, AdminSystemRepository systemRepository, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void upStatus(UUID adminID, UUID staffId) {
        Player staff = Bukkit.getPlayer(staffId);
        Player admin = Bukkit.getPlayer(adminID);

        if (staffId == adminID) {
            sender.sendPath(staff, "Messages.Errors.upAdminSelfError");
            return;
        }

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        int weightBaseStaff = systemRepository.getAdminWeight(staffId);

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        // Сделать ли триггер на слив через снятие?
        // TODO: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff <= weightBase) {
            sender.sendPath(staff, "Messages.Errors.upAdminWeightError");
            return;
        }

        int newLevel = weightBase + 1;
        if (newLevel > weightBaseStaff) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
        }

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendPath(staff, "Messages.Errors.nullPlayer");
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

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");

            if (cfgWeight == targetWeight) {
                currentKey = key;
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(staff, "Messages.Errors.upAdminLevelError");
            return;
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newWeight = cfg.getInt("Admin.AdminRanks." + currentKey + ".weight");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        if (newPrefix == null || newWeight == 0 || newSalary == 0) {
            sender.sendPath(admin, "Messages.Errors.upAdminLevelError");
            return;
        }

        //TODO: Сделать мож ивент какой что ли, а то скучновато по сообщениям как то..
        sender.sendPath(staff, "Messages.successfulUpStaff", "%admin", admin.getName());
        sender.sendPath(admin, "Messages.successfulUpAdmin", "%staff", staff.getName());

        staffRepository.upAdminStatus(adminID, newPrefix, newWeight, newSalary);
    }

    @Override
    public void downStatus(UUID adminID, UUID staffId) {
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffId);

        if (staffId == adminID) {
            sender.sendPath(admin, "Messages.Errors.upAdminSelfError");
            return;
        }

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        int weightBaseStaff = systemRepository.getAdminWeight(staffId);

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        // Сделать ли триггер на слив через снятие?
        // TODO: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff <= weightBase) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
            return;
        }

        int newLevel = weightBase + 1;
        if (newLevel > weightBaseStaff) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
        }

        if (admin == null) return;
        String currentKey = null;

        int targetWeight = weightBase - 1;

        Bukkit.getLogger().info("Target Weight" + targetWeight);
        Bukkit.getLogger().info("Base Weight" + weightBase);

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
            Bukkit.getLogger().info("CFG Weight" + cfgWeight);
            if (targetWeight == cfgWeight) {
                currentKey = key;
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(admin, "Messages.Errors.downAdminLevelError");
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newWeight = cfg.getInt("Admin.AdminRanks." + currentKey + ".weight");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        //TODO: Сделать мож ивент какой что ли, а то скучновато по сообщениям как то..
        sender.sendPath(staff, "Messages.successfulDownStaff", "%admin", admin.getName());
        sender.sendPath(admin, "Messages.successfulDownAdmin", "%staff", staff.getName());
        staffRepository.downAdminStatus(adminID, newPrefix, newWeight, newSalary);
    }

    @Override
    public void setAdmin(UUID adminID, UUID staffID, int weight) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        if (systemRepository.checkAdminInBase(adminID)) {
            sender.sendPath(staff, "Messages.Errors.isAdminContainsData");
            Bukkit.getLogger().warning("Человек уже есть в базе данных!");
            return;
        }

        for (String key : ranksSection.getKeys(false)) {
            if(cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight)  {
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());

                sender.sendPath(admin, "Messages.successfulSetAdmin",
                        "%level", String.valueOf(weight),
                        "%staff", staff.getName()
                );


                sender.sendPath(admin, "Messages.successfulSetStaff",
                        "%level", String.valueOf(weight),
                        "%admin", admin.getName()
                );

                return;
            } else {
                sender.sendPath(staff, "Messages.Errors.setAdminWeightNotFound");
            }
        }
    }

    @Override
    public void deleteAdmin(UUID adminID, UUID staffID, String reason) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        int weightBaseStaff = systemRepository.getAdminWeight(staffID);
        int weightBaseAdmin = systemRepository.getAdminWeight(adminID);

        // Сделать ли триггер на слив через снятие?
        // E1m0: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff < weightBaseAdmin) {
            sender.sendPath(staff, "Messages.Errors.delAdminWeightError");
            return;
        }

        sender.sendPath(staff, "Messages.successfulDeleteAdmin",
                "%admin", admin.getName());

        sender.sendPath(admin, "Messages.deleteAdminByStaff",
                "%staff", staff.getName());


        // TODO: Лучше - Вывести в Event.
        staffRepository.deleteAdminStatus(adminID);
        secretCodeRepository.systemDeleteAdmin(adminID);
        staffRepository.deleteAdminStatusLog(staffID, adminID, reason);

        Bukkit.getLogger().info("AdminSetCommand | COMMAND-SERVICE: /adel. Команда прошла sendRepo.");
    }

    @Override
    public void adminBonusGive(UUID adminID, UUID staffID, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        if (!admin.hasPermission(cfg.getString("Permissions.admin"))) {
            return;
        }

        String action = cfg.getString("Admin.Bonus.giveBonus")
                .replace("%admin", admin.getName())
                .replace("%bonus", String.valueOf(sum));

        sender.sendPath(admin, "Messages.adminBonusAll",
                "%message", message,
                "%staff", staff.getName(),
                "%bonus", String.valueOf(sum));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        staffRepository.giveBonusLog(adminID, staffID, sum, message);

    }

    @Override
    public void adminBonusAll(UUID staffID, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);

        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (!admin.hasPermission(cfg.getString("Permissions.admin"))) continue;

            String action = cfg.getString("Admin.Bonus.giveBonus")
                    .replace("%admin", admin.getName())
                    .replace("%bonus", String.valueOf(sum));

            sender.sendPath(admin, "Messages.adminBonus",
                    "%message", message,
                    "%staff", staff.getName(),
                    "%bonus", String.valueOf(sum));

            staffRepository.giveBonusLog(admin.getUniqueId(), staffID, sum, message);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        }
    }

    @Override
    public void setSecretPassword(UUID adminID, UUID staffID, int code) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        sender.sendPath(admin, "Messages.changeCodeAdmin",
                "%staff", staff.getName(),
                "%code", String.valueOf(code));

        sender.sendPath(staff, "Messages.changeCodeStaff",
                "%admin", admin.getName(),
                "%code", String.valueOf(code));

        secretCodeRepository.staffSetSecretCode(adminID, staffID, code);
        // TODO: Event?
    }
}
