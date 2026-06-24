package tvgirl.elmodev.e1m0Admin.service.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.api.service.gui.SecretCodeServiceAPI;
import tvgirl.elmodev.e1m0Admin.repository.gui.SecretCodeRepository;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;
import tvgirl.elmodev.e1m0Admin.utils.Color.E1m0Color;
import tvgirl.elmodev.e1m0Admin.utils.Message.E1m0Sender;
import tvgirl.elmodev.e1m0Admin.utils.permissions.PermissionsManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SecretCodeService implements SecretCodeServiceAPI {

    private final HashMap<UUID, SecretCodeState> secretCode = new HashMap<>();
    private final SecretCodeRepository secretCodeRepository;
    private final PermissionsManager permissionsManager;
    private final FileConfiguration cfg;
    private final E1m0Sender sender;

    private E1m0Color color = new E1m0Color();

    public SecretCodeService(SecretCodeRepository secretCodeRepository, PermissionsManager permissionsManager, FileConfiguration cfg, E1m0Sender sender) {
        this.secretCodeRepository = secretCodeRepository;
        this.permissionsManager = permissionsManager;
        this.sender = sender;
        this.cfg = cfg;
    }

    @Override
    public void oneStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setOne_step(i);
    }

    public void twoStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setTwo_step(i);
    }

    public void threeStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setThree_step(i);
    }

    public void foursStepHandler(UUID id, byte i) {
        if(!secretCode.containsKey(id)) return;
        SecretCodeState state = secretCode.get(id);

        state.setFours_step(i);
        String code = String.valueOf(state.getOne_step()) + String.valueOf(state.getTwo_step()) + String.valueOf(state.getThree_step()) + String.valueOf(state.getFours_step());

        Player p = Bukkit.getPlayer(id);
        byte repoCode = secretCodeRepository.getSecretCode(id);

        if(repoCode == Byte.valueOf(code)) { // ❗ | Опасная точка, перепроверить в тестерах
            if(cfg.getBoolean("Admin.SecretCode.accessCodeTrigger")) {
                for (Player adm : Bukkit.getOnlinePlayers()) {
                    if (adm.hasPermission("Permission.admin")) {
                        sender.sendPath(adm, "Messages.secretCodeAccess");
                    }
                }

                List<String> actions = cfg.getStringList("Admin.SecretCode.Actions.accessCodeActions");
                for (String s : actions) {
                    String command = s
                            .replace("%admin", p.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }

            permissionsManager.getAccess(id);
        } else {
            if(cfg.getBoolean("Admin.SecretCode.wrongCodeTrigger")) {
                for (Player adm : Bukkit.getOnlinePlayers()) {
                    if (adm.hasPermission("Permission.admin")) {
                        sender.sendPath(adm, "Messages.secretCodeIsWrong");
                    }
                }

                List<String> actions = cfg.getStringList("Admin.SecretCode.Actions.wrongCodeActions");
                for(String s : actions) {
                    String command = s
                            .replace("%admin", p.getName());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
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

        return one + two + three + fours;
    }
}
