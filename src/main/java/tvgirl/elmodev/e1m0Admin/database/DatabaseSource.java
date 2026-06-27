package tvgirl.elmodev.e1m0Admin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseSource {

    private final FileConfiguration pConfig;
    private HikariDataSource source;

    public DatabaseSource(FileConfiguration pConfig) {
        this.pConfig = pConfig;
    }

    public void init() {
        HikariConfig hConfig = new HikariConfig();
        hConfig.setPoolName("e1M0dEv");

        String type = pConfig.getString("Database.type", "postgresql").toLowerCase();
        String database = pConfig.getString("Database.name");
        String host = pConfig.getString("Database.host");

        String user = pConfig.getString("Database.user");
        String pass = pConfig.getString("Database.pass");

        int port = pConfig.getInt("Database.port");

        String jdbcUrl;

        switch (type) {
            case "postgresql" -> {
                jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?sslmode=disable";
                hConfig.setDriverClassName("org.postgresql.Driver");
            }

            case "mysql" -> {
                jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                hConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            }

            case "mariadb" -> {
                jdbcUrl = "jdbc:mariadb://" + host + ":" + port + "/" + database;
                hConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            }

            default -> throw new IllegalArgumentException("Unsupported DB type: " + type);
        }

        hConfig.setJdbcUrl(jdbcUrl);
        hConfig.setUsername(user);
        hConfig.setPassword(pass);

        hConfig.setMaximumPoolSize(10);
        hConfig.setMinimumIdle(2);
        hConfig.setConnectionTimeout(10000);
        hConfig.setIdleTimeout(60000);
        hConfig.setMaxLifetime(1700000);

        this.source = new HikariDataSource(hConfig);
    }

    public HikariDataSource getSource() {
        if (this.source == null) {
            throw new IllegalStateException("Database source is not initialized");
        }

        return this.source;
    }

    public void shutdown() {
        if (source != null && !source.isClosed()) {
            source.close();
        }
    }
}