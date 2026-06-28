package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.ConsoleServiceAPI;
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

        sender.sendPath(admin, "Messages.changeCodeAdmin"
                .replace("%staff", "CONSOLE"
                        .replace("%code", String.valueOf(code)))
        );

        Bukkit.getLogger().info("AdminChangeSecretCode | COMMAND-SERVICE: /csetsecret. Регистрирование репо + закрепление факта");
    }

    @Override
    public void setAdminConsole(UUID adminID, UUID consoleID, int weight) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player admin = Bukkit.getPlayer(adminID);

        // ConfigSection | Как обычно перебираю конфиг секции.
        for (String key : ranksSection.getKeys(false)) {
            // ConfigSection | Нахожу по weight ранг - и работаю с его ключом.
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight) {

                // Определяю зарплату и префикс
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                // Отправляю в репо.
                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());
                Bukkit.getLogger().info("ConsoleSetAdminCommand | COMMAND-SERVICE: /csetadmin. Администратор поставлен, + sendRepo"); // ❗ | Вот тут Важно! Тк.к консоль != Player, это не является тестером а прямым сообщением в консоль с успехом!
                return;
            } else {
                Bukkit.getLogger().info("ConsoleSetAdminCommand | COMMAND-SERVICE: /csetadmin. Администратор НЕ поставлен, ошибка weight"); // ❗ | Вот тут Важно! Тк.к консоль != Player, это не является тестером а прямым сообщением в консоль с ошибкой!
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

        String rankKey = null;
        int rankWeight = 0;

        Bukkit.getLogger().info("ConsoleUpAdminCommand | COMMAND-SERVICE: /cup. Точка выхода 0 - Пагинация."); // ТЕСТЕР
        if (admin == null) return;

        for (String key : ranksSection.getKeys(false)) {
            if (admin.hasPermission(cfg.getString("Admin.AdminRanks." + key + ".permission"))) {
                rankKey = key;
                rankWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (rankKey == null) return;
        String currentKey = null;

        int targetWeight = rankWeight + 1;
        int currentWeight = 0;

        Bukkit.getLogger().info("ConsoleUpAdminCommand | COMMAND-SERVICE: /cup. Точка выхода 1 - Баня с конфигом."); // ТЕСТЕР

        for (String key : ranksSection.getKeys(false)) {
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            admin.sendMessage(cfg.getString("Messages.Errors.upAdminLevelError"));
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        Bukkit.getLogger().info("ConsoleUpAdminCommand | COMMAND-SERVICE: /cup. Точка выхода 2 - Конечный результат + sendRepo: Ник: %admin. Префикс в базе: %prefix, зарплата: %salary"
                .replace("%admin", admin.getName())
                .replace("%prefix", newPrefix)
                .replace("%salary", String.valueOf(newSalary))); // ТЕСТЕР

        staffRepository.upAdminStatus(adminID);
    }

    @Override
    public void downAdminConsole(UUID adminID, UUID consoleID) {
        Player admin = Bukkit.getPlayer(adminID);
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        String weightBase = systemRepository.getAdminPrefix(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        String rankKey = null;
        int rankWeight = 0;

        Bukkit.getLogger().info("ConsoleDownAdminCommand | COMMAND-SERVICE: /cdown. Точка выхода 0 - Пагинация."); // ТЕСТЕР

        if (admin == null) return;

        for (String key : ranksSection.getKeys(false)) {
            if (admin.hasPermission(cfg.getString("Admin.AdminRanks." + key + ".permission"))) {
                rankKey = key;
                rankWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (rankKey == null) return;
        String currentKey = null;

        int targetWeight = rankWeight - 1;
        int currentWeight = 0;

        Bukkit.getLogger().info("ConsoleDownAdminCommand | COMMAND-SERVICE: /cdown. Точка выхода 1 - Проверки и баня с конфигом."); // ТЕСТЕР
        for (String key : ranksSection.getKeys(false)) {
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            sender.sendPath(admin, "Messages.Errors.downAdminLevelError");
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        staffRepository.downAdminStatus(adminID);
        Bukkit.getLogger().info("ConsoleDownAdminCommand | COMMAND-SERVICE: /cdown. Точка выхода 2 - Конечный результат + sendRepo: Ник: %admin. Префикс в базе: %prefix, зарплата: %salary"
                .replace("%admin", admin.getName())
                .replace("%prefix", newPrefix)
                .replace("%salary", String.valueOf(newSalary))); // ТЕСТЕР
    }

    @Override
    public void delAdminConsole(UUID adminID, UUID consoleID, String reason) {
        Player admin = Bukkit.getPlayer(adminID);

        staffRepository.deleteAdminStatus(adminID);
        staffRepository.deleteAdminStatusLog(consoleID, adminID, reason);
        Bukkit.getLogger().info("ConsoleDelAdminCommand | COMMAND-SERVICE: /cdel. Команда прошла sendRepo.");
    }
}
