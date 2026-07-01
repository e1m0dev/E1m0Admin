package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.StaffServiceAPI;
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
        // E1m0: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff > weightBase) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
            return;
        }

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendPath(staff, "Messages.Errors.nullPlayer");
            return;
        }

        Bukkit.getLogger().info("AdminUpCommand | COMMAND-SERVICE: /aup . Точка выхода -1 - Сбор информации. Нынешне: Префикс %prefix, Вес: %weight, Зарплата: %salary"
                .replace("%prefix", prefixBase)
                .replace("%weight", String.valueOf(weightBase))
                .replace("%salary", String.valueOf(salaryBase))
        ); // ТЕСТЕР

        String rankKey = null;
        int rankWeight = 0;

        Bukkit.getLogger().info("AdminUpCommand | COMMAND-SERVICE: /aup. Точка выхода 0 - Пагинация."); // ТЕСТЕР
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

        Bukkit.getLogger().info("AdminUpCommand | COMMAND-SERVICE: /aup. Точка выхода 1 - Баня с конфигом."); // ТЕСТЕР

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");

            if (cfgWeight == targetWeight) {
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

        if (weightBase == -1 || salaryBase == -1 || prefixBase.equalsIgnoreCase("NULL")) {
            sender.sendPath(staff, "Messages.Errors.nullPlayer");
            return;
        }


        Bukkit.getLogger().info("AdminUpCommand | COMMAND-SERVICE: /aup. Точка выхода 2 - Конечный результат + sendRepo: Ник: %admin. Префикс в базе: %prefix, зарплата: %salary"
                .replace("%admin", admin.getName())
                .replace("%prefix", newPrefix)
                .replace("%salary", String.valueOf(newSalary))); // ТЕСТЕР

        staffRepository.upAdminStatus(adminID, newPrefix, newWeight, newSalary);
    }

    @Override
    public void downStatus(UUID staffId, UUID adminID) {
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
        // E1m0: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff > weightBase) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
            return;
        }

        Bukkit.getLogger().info("AdminDownCommand | COMMAND-SERVICE: /cdown. Точка выхода -1 - Сбор информации. Нынешне: Префикс %prefix, Вес: %weight, Зарплата: %salary"
                .replace("%prefix", prefixBase)
                .replace("%weight", String.valueOf(weightBase))
                .replace("%salary", String.valueOf(salaryBase))
        ); // ТЕСТЕР

        String rankKey = null;
        int rankWeight = 0;

        Bukkit.getLogger().info("AdminDownCommand | COMMAND-SERVICE: /adown. Точка выхода 0 - Пагинация."); // ТЕСТЕР

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

        Bukkit.getLogger().info("AdminDownCommand | COMMAND-SERVICE: /adown. Точка выхода 1 - Проверки и баня с конфигом."); // ТЕСТЕР
        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
            if (cfgWeight == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(admin, "Messages.Errors.downAdminLevelError");
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newWeight = cfg.getInt("Admin.AdminRanks." + currentKey + ".weight");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        staffRepository.downAdminStatus(adminID, newPrefix, newWeight, newSalary);
        Bukkit.getLogger().info("AdminDownCommand | COMMAND-SERVICE: /adown. Точка выхода 2 - Конечный результат + sendRepo: Ник: %admin. Префикс в базе: %prefix, зарплата: %salary"
                .replace("%admin", admin.getName())
                .replace("%prefix", newPrefix)
                .replace("%salary", String.valueOf(newSalary))); // ТЕСТЕР
    }

    @Override
    public void setAdmin(UUID staffID, UUID adminID, int weight) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        if (systemRepository.checkAdminInBase(adminID)) {
            Bukkit.getLogger().warning("Человек уже есть в базе данных!");
            return;
        }

        for (String key : ranksSection.getKeys(false)) {
            if(cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight)  {
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());
                Bukkit.getLogger().info("AdminSetCommand | COMMAND-SERVICE: /aset. Администратор поставлен, + sendRepo"); // ТЕСТЕР
                return;
            } else {
                sender.sendPath(staff, "Messages.Errors.setAdminWeightNotFound");
                Bukkit.getLogger().info("AdminSetCommand | COMMAND-SERVICE: /aset. Администратор НЕ поставлен, ошибка weight"); // ТЕСТЕР
            }
        }
    }

    @Override
    public void deleteAdmin(UUID staffID, UUID adminID, String reason) {
        Player staff = Bukkit.getPlayer(staffID);

        int weightBaseStaff = systemRepository.getAdminWeight(staffID);
        int weightBaseAdmin = systemRepository.getAdminWeight(adminID);

        // Сделать ли триггер на слив через снятие?
        // E1m0: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff > weightBaseAdmin) {
            sender.sendPath(staff, "Messages.Errors.delAdminWeightError");
            return;
        }

        staffRepository.deleteAdminStatus(adminID);
        staffRepository.deleteAdminStatusLog(staffID, adminID, reason);
        Bukkit.getLogger().info("AdminSetCommand | COMMAND-SERVICE: /adel. Команда прошла sendRepo.");
    }

    @Override
    public void adminBonusGive(UUID staffID, UUID adminID, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);
        Bukkit.getLogger().info("AdminBonusCommand | COMMAND-SERVICE: /abonus. Точка выхода 1 - Делегация ответственности + Проверки."); // ТЕСТЕР

        if (!admin.hasPermission("e1admin.adm")) return;

        String action = cfg.getString("Admin.Bonus.giveBonus")
                .replace("%admin", admin.getName())
                .replace("%bonus", String.valueOf(sum));

        sender.sendPath(admin, "Messages.adminBonusAll",
                "%message", message,
                "%staff", staff.getName(),
                "%bonus", String.valueOf(sum));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        staffRepository.giveBonusLog(staffID, adminID, sum, message);

        Bukkit.getLogger().info("AdminBonusCommand | COMMAND-SERVICE: /abonus. Точка выхода 2 - Исполнение."); // ТЕСТЕР
    }

    @Override
    public void adminBonusAll(UUID staffID, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);
        Bukkit.getLogger().info("AdminBonusAllCommand | COMMAND-SERVICE: /abonusall. Точка выхода 1 - Делегация ответственности + Проверки."); // ТЕСТЕР

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("e1admin.adm")) continue;

            String action = cfg.getString("Admin.Bonus.giveBonus")
                    .replace("%admin", p.getName())
                    .replace("%bonus", String.valueOf(sum));

            sender.sendPath(p, "Messages.adminBonus",
                    "%message", message,
                    "%staff", staff.getName(),
                    "%bonus", String.valueOf(sum));

            Bukkit.getLogger().info("AdminBonusAllCommand | COMMAND-SERVICE: /abonusall. Точка выхода 2 - Исполнение"); // ТЕСТЕР
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
        Bukkit.getLogger().info("AdminChangeSecretCode | COMMAND-SERVICE: /asecret. Регистрирование репо + закрепление факта"); // ТЕСТЕР
        // TODO: Event?
    }
}
