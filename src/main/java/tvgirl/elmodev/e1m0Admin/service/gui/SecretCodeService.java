package tvgirl.elmodev.e1m0Admin.service.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.gui.SecretCodeServiceAPI;
import tvgirl.elmodev.e1m0Admin.event.AdminAccessEvent;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SecretCodeService implements SecretCodeServiceAPI {

    private final HashMap<UUID, SecretCodeState> secretCode = new HashMap<>();
    private final SecretCodeRepository secretCodeRepository;
    private final E1m0Permission e1m0Permission;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    private SecretCodeManager manager = new SecretCodeManager();
    private E1m0Color color = new E1m0Color();

    public SecretCodeService(SecretCodeRepository secretCodeRepository, E1m0Permission e1m0Permission, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.e1m0Permission = e1m0Permission;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void oneStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setOne_step(i);
        Bukkit.getLogger().info("SecretCodeService | Точка входа twoStepHandler: " + i); // ТЕСТЕР
    }

    public void twoStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setTwo_step(i);
        Bukkit.getLogger().info("SecretCodeService | Точка входа twoStepHandler: " + i); // ТЕСТЕР
    }

    public void threeStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setThree_step(i);
        Bukkit.getLogger().info("SecretCodeService | Точка входа threeStepHandler: " + i); // ТЕСТЕР
    }

    public void foursStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setFours_step(i);
        String code = String.valueOf(state.getOne_step()) + String.valueOf(state.getTwo_step()) + String.valueOf(state.getThree_step()) + String.valueOf(state.getFours_step());

        Bukkit.getLogger().info("SecretCodeService | Финальный код: " + code); // ТЕСТЕР

        // НЕ ТРОГАЙ, НЕ ПРАВЬ, ЭТО НЕ ПОДТВЕРЖДЕННЫЙ АДМИН, НЕ ЗАБЫВАТЬ, Я УЖЕ ЭТО ИСПРАВЛЯЛ 3 РАЗА.
        //     Пример: Рекон от 25.06.26.
        Player user = Bukkit.getPlayer(id);
        byte repoCode = secretCodeRepository.getSecretCode(id);

        if (repoCode == Byte.valueOf(code)) { // ❗ | Опасная точка, перепроверить в тестерах. By: E1m0.
            if(cfg.getBoolean("Admin.SecretCode.accessCodeTrigger")) {
                for (Player adm : Bukkit.getOnlinePlayers()) {
                    if (adm.hasPermission("Permission.admin")) {
                        sender.sendPath(adm, "Admin.SecretCode.adminAccessNotify");
                    }
                }

                List<String> actions = cfg.getStringList("Admin.SecretCode.Actions.accessCodeActions");
                for (String s : actions) {
                    String command = s
                            .replace("%admin", user.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }

            // Вот тут - Админ имеет *ВЕС*, именно по этому действия уже с фактом системы, а не надуманным мной действием.

            manager.addAdminAccess(state);
            Bukkit.getPluginManager().callEvent(new AdminAccessEvent(user));
            Bukkit.getLogger().info("SecretCodeService | Точка входа foursStepHandler: 💚 Прошел.."); // ТЕСТЕР
        } else {
            if(cfg.getBoolean("Admin.SecretCode.wrongCodeTrigger")) {
                for (Player adm : Bukkit.getOnlinePlayers()) {
                    if (adm.hasPermission("Permission.admin")) {
                        sender.sendPath(adm, "Admin.SecretCode.wrongCodeNotify");
                    }
                }

                List<String> actions = cfg.getStringList("Admin.SecretCode.Actions.wrongCodeActions");
                for(String s : actions) {
                    String command = s
                            .replace("%admin", user.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }

            Bukkit.getLogger().info("SecretCodeService | Точка входа foursStepHandler: ❤️ НЕ прошел.."); // ТЕСТЕР
        }
    }

    @Override
    public String getInputCode(UUID id) {
        if(!secretCode.containsKey(id)) return null;
        SecretCodeState state = secretCode.get(id);

        String one = String.valueOf(state.getOne_step());
        if(one.equalsIgnoreCase("") || one == null) {
            return "*";
        }

        String two = String.valueOf(state.getTwo_step());
        if(two.equalsIgnoreCase("") || two == null) {
            return "*";
        }

        String three = String.valueOf(state.getThree_step());
        if(three.equalsIgnoreCase("") || three == null) {
            return "*";
        }

        String fours = String.valueOf(state.getFours_step());
        if(fours.equalsIgnoreCase("") || fours == null) {
            return "*";
        }

        Bukkit.getLogger().info("SecretCodeService | Точка входа getInputCode: " + one + two + three + fours); // ТЕСТЕР
        return one + two + three + fours;
    }
}
