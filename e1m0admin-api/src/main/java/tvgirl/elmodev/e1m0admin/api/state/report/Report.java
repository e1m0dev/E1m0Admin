package tvgirl.elmodev.e1m0admin.api.state.report;

import java.util.UUID;

public class Report {
    private final UUID uuid;
    private UUID adminID;
    private final UUID playerID;
    private String adminNick;
    private final String playerNick;
    private final String report;
    private String response;
    private String status;
    private final long createdAt;

    public Report(UUID uuid, UUID adminID, UUID playerID, String adminNick, String playerNick, String report, String response, String status, long createdAt) {
        this.uuid = uuid;
        this.adminID = adminID;
        this.playerID = playerID;
        this.adminNick = adminNick;
        this.playerNick = playerNick;
        this.report = report;
        this.response = response;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getAdminNick() {
        return adminNick;
    }

    public String getPlayerNick() {
        return playerNick;
    }

    public String getReport() {
        return report;
    }

    public String getResponse() {
        return response;
    }

    public String getStatus() {
        return status;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}