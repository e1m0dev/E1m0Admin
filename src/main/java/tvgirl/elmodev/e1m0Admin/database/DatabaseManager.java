package tvgirl.elmodev.e1m0Admin.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.E1m0Admin;

public class DatabaseManager {

    private final Jdbi jdbi;

    private final E1m0Admin plugin;

    public DatabaseManager(HikariDataSource source, E1m0Admin plugin) {
        this.jdbi = Jdbi.create(source);
        this.plugin = plugin;
    }

    public Jdbi getJdbi() {
        return jdbi;
    }

    public void initDatabase(HikariDataSource source) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            /* ADMINS | 🧑‍🔬 */
            String SQL_ADMINS = """
                        CREATE TABLE IF NOT EXISTS e1admin_admins (
                            uuid VARCHAR(36) NOT NULL,
                            nick VARCHAR(24) NOT NULL,
                            weight INTEGER NOT NULL,
                            salary INTEGER NOT NULL,
                            IP VARCHAR(32) NOT NULL,
                            prefix VARCHAR(64),
                            setAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    
                            PRIMARY KEY(uuid)
                        );
                    """;

            /* REPORTS | 🚨 */
            String SQL_REPORT = """
                    CREATE TABLE IF NOT EXISTS e1admin_reports (
                        uuid VARCHAR(36) NOT NULL,
                        adminID VARCHAR(36),
                        playerID VARCHAR(36) NOT NULL,
                        adminNick VARCHAR(24),
                        playerNick VARCHAR(24) NOT NULL,
                        report TEXT NOT NULL,
                        response TEXT,
                        status VARCHAR(16) NOT NULL,
                        createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                
                        PRIMARY KEY(uuid)
                        );
                    """;


            /* BONUS | 🎁 */
            String SQL_BONUS = """
                    CREATE TABLE IF NOT EXISTS e1admin_reports (
                        uuid VARCHAR(36) NOT NULL,
                        staffID VARCHAR(36) NOT NULL,
                        adminID VARCHAR(36) NOT NULL,
                        staffNick VARCHAR(24) NOT NULL,
                        adminNick VARCHAR(24) NOT NULL,
                        sum INT NOT NULL,
                        message VARCHAR(128) NOT NULL,
                        bonusAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                
                        PRIMARY KEY(uuid)
                        );
                    """;

            /* BONUS | 🚨 */
            String SQL_CODE = """
                    CREATE TABLE IF NOT EXISTS e1admin_code (
                        uuid VARCHAR(36) NOT NULL,
                        adminNick VARCHAR(24) NOT NULL,
                        staffNick VARCHAR(24),
                        code BYTE NOT NULL,
                        regIP VARCHAR(32) NOT NULL,
                        createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                
                        PRIMARY KEY(uuid)
                        );
                    """;


            try {
                jdbi.useHandle(handle -> {
                    handle.execute(SQL_ADMINS);
                    handle.execute(SQL_REPORT);
                    handle.execute(SQL_BONUS);
                    handle.execute(SQL_CODE);
                });
            } catch (Exception e) {
                plugin.getLogger().severe(e.getMessage());
            }
        });
    }
}