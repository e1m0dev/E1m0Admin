package tvgirl.elmodev.e1m0Admin.state.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class AdminSessionManager {

    private final AdminSystemRepository systemRepository;

    private Map<UUID, AdminSession> sessions;

    public AdminSessionManager(AdminSystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    public void join(UUID id) {
        Player p = Bukkit.getPlayer(id);

        String adminPrefix = systemRepository.getAdminPrefix(id);
        int adminWeight = systemRepository.getAdminWeight(id);

        if (adminWeight == -1 || adminPrefix.equalsIgnoreCase("NULL")) {
            Bukkit.getLogger().warning("Администратора который только что зашел - не существует в базе. ❗ ОПАСНАЯ НЕ ОПРЕДЕЛЕННОСТЬ");
            return;
        }

        sessions.put(
                p.getUniqueId(),
                new AdminSession(
                        p.getUniqueId(),
                        p.getName(),
                        0,
                        System.currentTimeMillis(),
                        adminWeight,
                        adminPrefix
                )
        );
    }

    public void quit(UUID id) {
        sessions.remove(id);
    }

    public Collection<AdminSession> getSessions() {
        return sessions.values();
    }
}
