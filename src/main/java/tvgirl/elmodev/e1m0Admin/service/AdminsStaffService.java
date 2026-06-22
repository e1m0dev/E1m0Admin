package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.StaffServiceAPI;
import tvgirl.elmodev.e1m0Admin.color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;

import java.util.UUID;

public class AdminStaffService implements StaffServiceAPI {

    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final FileConfiguration cfg;

    private E1m0Color color = new E1m0Color();

    public AdminStaffService(AdminStaffRepository staffRepository, AdminSystemRepository systemRepository, FileConfiguration cfg) {
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.cfg = cfg;
    }

    @Override
    public void upStatus(UUID staffId, UUID id) {
        Player p = Bukkit.getPlayer(id);
        Player staff = Bukkit.getPlayer(staffId);

        // TODO: Перенести в команду
        if (staffId == id) {
            staff.sendMessage(cfg.getString("Messages.Errors.upAdminSelfError"));
            return;
        }
        //

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        String prefixBase = systemRepository.getAdminPrefix(id);

        int weightBase = systemRepository.getAdminWeight(id);
        int salaryBase = systemRepository.getAdminSalary(id);

        String rankKey = null;
        int rankWeight = 0;

        if (p == null) return;

        for (String key : ranksSection.getKeys(false)) {
            if (p.hasPermission(cfg.getString("Admin.AdminRanks." + key + ".permission"))) {
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
            p.sendMessage(cfg.getString("Messages.Errors.upAdminLevelError"));
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        staffRepository.upAdminStatus(id, currentWeight, newSalary, newPrefix);
    }

    @Override
    public void downStatus(UUID staffId, UUID id) {
        Player p = Bukkit.getPlayer(id);
        Player staff = Bukkit.getPlayer(staffId);

        // TODO: Перенести в команду
        if (staffId == id) {
            staff.sendMessage(cfg.getString("Messages.Errors.upAdminSelfError"));
            return;
        }
        //

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        String prefixBase = systemRepository.getAdminPrefix(id);
        String weightBase = systemRepository.getAdminPrefix(id);
        int salaryBase = systemRepository.getAdminSalary(id);

        String rankKey = null;
        int rankWeight = 0;

        if (p == null) return;

        for (String key : ranksSection.getKeys(false)) {
            if (p.hasPermission(cfg.getString("Admin.AdminRanks." + key + ".permission"))) {
                rankKey = key;
                rankWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (rankKey == null) return;
        String currentKey = null;

        int targetWeight = rankWeight - 1;
        int currentWeight = 0;

        for (String key : ranksSection.getKeys(false)) {
            if (cfg.getInt("Admin.AdminRanks." + key + ".weight") == targetWeight) {
                currentKey = key;
                currentWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                break;
            }
        }

        if (currentKey == null) {
            p.sendMessage(cfg.getString("Messages.Errors.downAdminLevelError"));
        }

        String newPrefix = cfg.getString("Admin.AdminRanks." + currentKey + ".prefix");
        int newSalary = cfg.getInt("Admin.AdminRanks." + currentKey + ".salary");

        staffRepository.downAdminStatus(id, currentWeight, newSalary, newPrefix);
    }

    @Override
    public void setAdmin(UUID staff, UUID id) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player p = Bukkit.getPlayer(id);

        for (String key : ranksSection.getKeys(false)) {
                int weight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                staffRepository.setAdminStatus(id, p.getName(), weight, salary, prefix);
                return;
            }
        }
    }

    @Override
    public void adminBonusGive(UUID staffID, UUID id, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);
        Player p = Bukkit.getPlayer(id);
        if (!p.hasPermission("e1admin.adm")) return;

        String action = cfg.getString("Admin.Bonus.giveBonus")
                .replace("%admin", p.getName())
                .replace("%bonus", String.valueOf(sum));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
        p.sendMessage(color.parse(cfg.getString("Messages.adminBonusAll",
            "&7Администратор %staff выдал Вам бонус: %bonus $!")
                .replace("%staff", staff.getName())
                .replace("%bonus", String.valueOf(sum))));

    }

    @Override
    public void adminBonusAll(UUID staffID, int sum, String message) {
        Player staff = Bukkit.getPlayer(staffID);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("e1admin.adm")) continue;

            String action = cfg.getString("Admin.Bonus.giveBonus")
                    .replace("%admin", p.getName())
                    .replace("%bonus", String.valueOf(sum));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
            p.sendMessage(color.parse(cfg.getString("Messages.adminBonus",
                            "&7Администратор %staff выдал Вам бонус: %bonus $!")
                    .replace("%staff", staff.getName())
                    .replace("%bonus", String.valueOf(sum))));
        }
    }
}
