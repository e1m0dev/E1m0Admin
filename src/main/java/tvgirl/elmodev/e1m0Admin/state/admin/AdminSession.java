package tvgirl.elmodev.e1m0Admin.state;

import java.util.UUID;

public class AdminSession {

    private final UUID uuid;
    private final String name;
    private final long joinTime;

    private int workedHours;

    private final int adminSalary;
    private final String adminPrefix;

    public AdminSession(UUID uuid, String name, int workedHours, long joinTime, int adminSalary, String adminPrefix) {
        this.uuid = uuid;
        this.name = name;
        this.joinTime = joinTime;
        this.workedHours = workedHours;
        this.adminSalary = adminSalary;
        this.adminPrefix = adminPrefix;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getWorkedHours() {
        return workedHours;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void plusWorkedHours() {
        workedHours++;
    }

    public int getAdminSalary() {
        return adminSalary;
    }

    public String getAdminPrefix() {
        return adminPrefix;
    }
}
