package tvgirl.elmodev.e1m0Admin.state;

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

    public Report(UUID uuid, UUID adminID, UUID playerID, String adminNick, String playerNick, String report, String response, String status) {
        this.uuid = uuid;
        this.adminID = adminID;
        this.playerID = playerID;
        this.adminNick = adminNick;
        this.playerNick = playerNick;
        this.report = report;
        this.response = response;
        this.status = status;
    }

    public void answer(UUID adminID, String adminNick, String response, String status) {
        this.adminID = adminID;
        this.adminNick = adminNick;
        this.response = response;
        this.status = status;
    }
}