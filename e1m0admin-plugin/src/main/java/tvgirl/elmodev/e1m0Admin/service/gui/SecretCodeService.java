package tvgirl.elmodev.e1m0Admin.service.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import tvgirl.elmodev.e1m0admin.api.service.gui.SecretCodeServiceAPI;
import tvgirl.elmodev.e1m0Admin.event.AdminAccessEvent;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeManager;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.E1m0Permission;
import tvgirl.elmodev.e1m0admin.api.state.secretcode.SecretCodeState;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SecretCodeService implements SecretCodeServiceAPI {

    private final HashMap<UUID, SecretCodeState> secretCode;
    private final SecretCodeRepository secretCodeRepository;
    private final E1m0Permission e1m0Permission;
    private final SecretCodeManager manager;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    private E1m0Color color = new E1m0Color();

    public SecretCodeService(HashMap<UUID, SecretCodeState> secretCode, SecretCodeRepository secretCodeRepository, E1m0Permission e1m0Permission, SecretCodeManager manager, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.e1m0Permission = e1m0Permission;
        this.secretCode = secretCode;
        this.manager = manager;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public boolean checkSecret(UUID id) {
        return secretCodeRepository.checkSecretCode(id);
    }

    @Override
    public void oneStepHandler(UUID id, int i) {
        if (secretCode.containsKey(id)) return;
        SecretCodeState state = new SecretCodeState(id);

        state.setOne_step(i);
        secretCode.put(id, state);
    }

    public void twoStepHandler(UUID id, int i) {
        if (!secretCode.containsKey(id)) {
            return;
        }

        SecretCodeState state = secretCode.get(id);

        state.setTwo_step(i);
    }

    public void threeStepHandler(UUID id, int i) {
        if (!secretCode.containsKey(id)) {
            return;
        }

        SecretCodeState state = secretCode.get(id);

        state.setThree_step(i);
    }

    public void foursStepHandler(UUID id, int i) {
        if (!secretCode.containsKey(id)) {
            return;
        }

        SecretCodeState state = secretCode.get(id);
        Player admin = Bukkit.getPlayer(id);

        state.setFours_step(i);
        String code = String.valueOf(state.getOne_step()) + String.valueOf(state.getTwo_step()) + String.valueOf(state.getThree_step()) + String.valueOf(state.getFours_step());

        // НЕ ТРОГАЙ, НЕ ПРАВЬ, ЭТО НЕ ПОДТВЕРЖДЕННЫЙ АДМИН, НЕ ЗАБЫВАТЬ, Я УЖЕ ЭТО ИСПРАВЛЯЛ 3 РАЗА.
        //     Пример: Рекон от 25.06.26.
        Player user = Bukkit.getPlayer(id);
        int repoCode = secretCodeRepository.getSecretCode(id);

        if (repoCode == Integer.parseInt(code)) {
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

                // CLS | Console Log
                boolean isActive = cfg.getBoolean("Settings.consoleLogActive");
                if (isActive) {
                    sender.sendConsole(Bukkit.getConsoleSender(), "Messages.ConsoleLogs.accessLog",
                            "%admin", admin.getName());
                }
            }

            // Вот тут - Админ имеет *ВЕС*, именно по этому действия уже с фактом системы, а не надуманным мной действием.
            user.closeInventory();
            manager.addAdminAccess(state);
            Bukkit.getPluginManager().callEvent(new AdminAccessEvent(user.getUniqueId()));


            SecretCodeState stateCheck = manager.getAdminByID(id);
            boolean f = stateCheck != null;
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

            secretCode.remove(id);
            user.closeInventory();
            Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(user.getPlayer(), "WRONG CODE"));
        }
    }

    @Override
    public String getInputCode(UUID id) {
        if (!secretCode.containsKey(id)) {
            return null;
        }

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

        return one + two + three + fours;
    }
}
