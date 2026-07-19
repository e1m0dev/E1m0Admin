package tvgirl.elmodev.e1m0Admin.service;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.event.AdminDelEvent;
import tvgirl.elmodev.e1m0Admin.event.AdminLeakEvent;
import tvgirl.elmodev.e1m0Admin.event.AdminSetEvent;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.session.AdminSessionManager;
import tvgirl.elmodev.e1m0admin.api.service.StaffServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.AdminStaffRepository;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;

import java.util.UUID;

public class AdminsStaffService implements StaffServiceAPI {

    private final SecretCodeRepository secretCodeRepository;
    private final AdminSessionManager adminSessionManager;
    private final AdminSystemRepository systemRepository;
    private final AdminStaffRepository staffRepository;
    private final SecretCodeManager secretCodeManager;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    public AdminsStaffService(SecretCodeRepository secretCodeRepository, AdminSessionManager adminSessionManager, AdminStaffRepository staffRepository, AdminSystemRepository systemRepository, SecretCodeManager secretCodeManager, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.adminSessionManager = adminSessionManager;
        this.secretCodeManager = secretCodeManager;
        this.systemRepository = systemRepository;
        this.staffRepository = staffRepository;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void upStatus(UUID adminID, UUID staffID) {
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        if (staffID == adminID) {
            sender.sendPath(staff, "Messages.Errors.upAdminSelfError");
            return;
        }

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        int weightBaseStaff = systemRepository.getAdminWeight(staffID);

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        if (weightBaseStaff <= weightBase) {
            sender.sendPath(staff, "Messages.Errors.upAdminWeightError");

            String leakMessage = "Messages.ConsoleLogs.Leak.upAdminWeightError";
            Bukkit.getPluginManager().callEvent(new AdminLeakEvent(adminID, staffID, leakMessage));
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

        // TODO: Сделать мож ивент какой что ли, а то скучновато по сообщениям как то..
        sender.sendPath(staff, "Messages.successfulUpStaff", "%admin", admin.getName());
        sender.sendPath(admin, "Messages.successfulUpAdmin", "%staff", staff.getName());

        adminSessionManager.update(adminID, newPrefix, newWeight, newSalary);
        staffRepository.upAdminStatus(adminID, newPrefix, newWeight, newSalary);
    }

    @Override
    public void downStatus(UUID adminID, UUID staffID) {
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        if (staffID == adminID) {
            sender.sendPath(admin, "Messages.Errors.upAdminSelfError");
            return;
        }

        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");

        int weightBaseStaff = systemRepository.getAdminWeight(staffID);

        String prefixBase = systemRepository.getAdminPrefix(adminID);
        int weightBase = systemRepository.getAdminWeight(adminID);
        int salaryBase = systemRepository.getAdminSalary(adminID);

        if (weightBaseStaff <= weightBase) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");

            String leakMessage = "Messages.ConsoleLogs.Leak.downAdminWeightError";
            Bukkit.getPluginManager().callEvent(new AdminLeakEvent(adminID, staffID, leakMessage));
            return;
        }

        int newLevel = weightBase + 1;
        if (newLevel > weightBaseStaff) {
            sender.sendPath(staff, "Messages.Errors.downAdminWeightError");
        }

        if (admin == null) return;
        String currentKey = null;

        int targetWeight = weightBase - 1;

        for (String key : ranksSection.getKeys(false)) {
            int cfgWeight = cfg.getInt("Admin.AdminRanks." + key + ".weight");
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

        adminSessionManager.update(adminID, newPrefix, newWeight, newSalary);
        staffRepository.downAdminStatus(adminID, newPrefix, newWeight, newSalary);
    }

    @Override
    public void setAdmin(UUID adminID, UUID staffID, int weight) {
        ConfigurationSection ranksSection = cfg.getConfigurationSection("Admin.AdminRanks");
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        if (systemRepository.checkAdminInBase(adminID)) {
            sender.sendPath(staff, "Messages.Errors.isAdminContainsData");
            return;
        }

        boolean inBlackList = staffRepository.checkAdminBlackList(adminID);
        if (inBlackList) {
            sender.sendPath(staff, "Messages.Errors.adminInBlackList");
            return;
        }

        for (String key : ranksSection.getKeys(false)) {
            if(cfg.getInt("Admin.AdminRanks." + key + ".weight") == weight)  {
                int salary = cfg.getInt("Admin.AdminRanks." + key + ".salary");
                String prefix = cfg.getString("Admin.AdminRanks." + key + ".prefix");

                adminSessionManager.update(adminID, prefix, weight, salary);
                Bukkit.getPluginManager().callEvent(new AdminSetEvent(adminID, staffID, weight));
                staffRepository.setAdminStatus(adminID, admin.getName(), weight, salary, prefix, admin.getAddress().toString());

                sender.sendPath(admin, "Messages.successfulSetAdmin",
                        "%level", String.valueOf(weight),
                        "%staff", staff.getName()
                );


                sender.sendPath(staff, "Messages.successfulSetStaff",
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
        // TODO: ТРИГГЕР НА СЛИВ КФГ + ИВЕНТ
        if (weightBaseStaff < weightBaseAdmin) {
            sender.sendPath(staff, "Messages.Errors.delAdminWeightError");

            String leakMessage = "Messages.ConsoleLogs.Leak.delAdminWeightError";
            Bukkit.getPluginManager().callEvent(new AdminLeakEvent(adminID, staffID, leakMessage));
            return;
        }

        sender.sendPath(staff, "Messages.successfulDeleteAdmin",
                "%admin", admin.getName());

        sender.sendPath(admin, "Messages.deleteAdminByStaff",
                "%staff", staff.getName());

        adminSessionManager.quit(adminID); // Удаляем сессию
        staffRepository.deleteAdminStatus(adminID); // Удаляем из базы данных
        secretCodeRepository.systemDeleteAdmin(adminID); // Удаляем доступ к командам
        staffRepository.deleteAdminStatusLog(staffID, adminID, reason); // Лог об удалении
        Bukkit.getPluginManager().callEvent(new AdminDelEvent(adminID, staffID)); // Создаем ивент об увольнении
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

        int weightBaseStaff = systemRepository.getAdminWeight(staffID);
        int weightBaseAdmin = systemRepository.getAdminWeight(adminID);

        if (weightBaseStaff < weightBaseAdmin) {
            sender.sendPath(staff, "Messages.Errors.setSecretAdminWeightError");

            String leakMessage = "Messages.ConsoleLogs.Leak.setSecretAdminWeightError";
            Bukkit.getPluginManager().callEvent(new AdminLeakEvent(adminID, staffID, leakMessage));
            return;
        }

        sender.sendPath(admin, "Messages.changeCodeAdmin",
                "%staff", staff.getName(),
                "%code", String.valueOf(code));

        sender.sendPath(staff, "Messages.changeCodeStaff",
                "%admin", admin.getName(),
                "%code", String.valueOf(code));

        if (secretCodeRepository.checkSecretCode(adminID)) {
            secretCodeRepository.updateSecretCode(adminID, staffID, code);
        } else {
            secretCodeRepository.staffSetSecretCode(adminID, staffID, code);
        }

        // TODO: Event?
    }

    @Override
    public void adminUnBanSystem(UUID adminID, UUID staffID) {
        boolean isBlockedStaff = staffRepository.checkAdminABan(staffID);
        boolean isBlockedAdmin = staffRepository.checkAdminABan(adminID);
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        if (isBlockedStaff) {
            sender.sendPath(staff, "Messages.Errors.youAdminAccessIsBlocked");
            return;
        }

        if (isBlockedAdmin) {
            staffRepository.delAdminABan(adminID);
        } else {
            sender.sendPath(staff, "Messages.Errors.adminNotBanned",
                    "%admin", admin.getName());
            return;
        }

        sender.sendPath(admin, "Messages.unbannedSystem",
                "%staff", staff.getName());

        sender.sendPath(staff, "Messages.successfulUnbannedSystem",
                "%admin", admin.getName());
    }

    @Override
    public void adminAddBlackList(UUID adminID, UUID staffID, String reason) {
        boolean inBlackList = staffRepository.checkAdminBlackList(adminID);
        boolean isAdmin = systemRepository.checkAdminInBase(adminID);
        Player staff = Bukkit.getPlayer(staffID);
        Player admin = Bukkit.getPlayer(adminID);

        if (inBlackList) {
            sender.sendPath(staff, "Messages.Errors.adminInBlackList");
            return;
        }

        if (isAdmin) {
            // Если это администратор, я с начала сравниваю веса:
            int weightStaff = systemRepository.getAdminWeight(staffID);
            int weightAdmin = systemRepository.getAdminWeight(adminID);

            // Если у таргета, вес больше чем у того кто снимает - подозрения в сливе.
            // Снять максимальный уровень админки может только консоль, это - безопасность сервера.
            if (weightStaff <= weightAdmin) {
                sender.sendPath(staff, "Messages.Errors.blacklistWeightError");

                String message = cfg.getString("Messages.ConsoleLogs.Leak.blacklistWeightError");
                Bukkit.getPluginManager().callEvent(new AdminLeakEvent(adminID, staffID, message));
                return;
            }

            // Если это админ меньшего ранга - снять, повесить, растерзать, прострелить коленные чашечки.
            // https://www.tiktok.com/@klyowa23/video/7657110663021694229?_r=1&_t=ZS-9879QZFfKpF
            deleteAdmin(adminID, staffID, reason);
        }

        sender.sendPath(staff, "Messages.successfulAddBlockList",
                "%admin", admin.getName());

        staffRepository.setAdminBlackList(adminID, staffID, reason);
    }

    @Override
    public void adminDelBlackList(UUID adminID, UUID staffID, String reason) {
        boolean inBlackList = staffRepository.checkAdminBlackList(adminID);
        Player admin = Bukkit.getPlayer(adminID);
        Player staff = Bukkit.getPlayer(staffID);

        if (!inBlackList) {
            sender.sendPath(staff, "Messages.Errors.adminNotInBlackList",
                    "%admin", admin.getName());
            return;
        }

        sender.sendPath(staff, "Messages.successfulRemoveBlackList",
                "%admin", admin.getName());

        staffRepository.delAdminBlackList(adminID);
    }
}
