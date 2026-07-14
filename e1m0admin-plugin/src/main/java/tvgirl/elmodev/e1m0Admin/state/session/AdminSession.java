package tvgirl.elmodev.e1m0Admin.state.session;

import java.util.UUID;

public class AdminSession {

    private final UUID uuid;
    private final String name;

    private int workedMinutes;

    private final int adminSalary;
    private final int adminWeight;
    private final String adminPrefix;

    private final long joinTime;

    public AdminSession(UUID uuid, String name, int adminSalary, int adminWeight, String adminPrefix, long joinTime) {
        this.uuid = uuid;
        this.name = name;
        this.adminSalary = adminSalary;
        this.adminWeight = adminWeight;
        this.adminPrefix = adminPrefix;
        this.joinTime = joinTime;
    }


    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getWorkedMinutes() {
        return workedMinutes;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void plusWorkedMinutes() {
        workedMinutes++;
    }

    public int getAdminSalary() {
        return adminSalary;
    }

    public int getAdminWeight() {
        return adminWeight;
    }

    public String getAdminPrefix() {
        return adminPrefix;
    }
}
