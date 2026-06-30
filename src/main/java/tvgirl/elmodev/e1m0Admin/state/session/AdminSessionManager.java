package tvgirl.elmodev.e1m0Admin.state.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tvgirl.elmodev.e1m0Admin.repository.AdminSystemRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminSessionManager {

    private final AdminSystemRepository systemRepository;

    private final HashMap<UUID, AdminSession> sessionsCache;

    public AdminSessionManager(AdminSystemRepository systemRepository, HashMap<UUID, AdminSession> sessionsCache) {
        this.systemRepository = systemRepository;
        this.sessionsCache = sessionsCache;
    }

    public void join(UUID id) {
        Player p = Bukkit.getPlayer(id);

        String adminPrefix = systemRepository.getAdminPrefix(id);
        int adminWeight = systemRepository.getAdminWeight(id);
        int adminSalary = systemRepository.getAdminSalary(id);

        if (adminWeight == -1 || adminPrefix.equalsIgnoreCase("NULL")) {
            Bukkit.getLogger().warning("Администратора который только что зашел - не существует в базе. ❗ ОПАСНАЯ НЕ ОПРЕДЕЛЕННОСТЬ");
            return;
        }

        AdminSession newSession = new AdminSession(
                p.getUniqueId(),
                p.getName(),
                adminSalary,
                adminWeight,
                adminPrefix,
                System.currentTimeMillis()
        );

        if (newSession == null) {
            Bukkit.getLogger().info("AdminSession: Is null, bug of a plugin?");
        }

        sessionsCache.put(p.getUniqueId(), newSession);
    }

    public void quit(UUID id) {
        sessionsCache.remove(id);
    }

    public Collection<AdminSession> getSessions() {
        return sessionsCache.values();
    }
}
