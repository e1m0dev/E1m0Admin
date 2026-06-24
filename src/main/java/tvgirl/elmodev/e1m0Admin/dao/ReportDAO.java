package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.report.Report;

import java.util.List;
import java.util.UUID;

public interface ReportDAO {

    /* REPORT | 🚨 */
    @SqlUpdate("""
        INSERT INTO e1admin_reports
        (uuid, adminID, playerID, adminNick, playerNick, report, response, status)
        VALUES
        (:uuid, :adminID, :playerID, :adminNick, :playerNick, :report, :response, :status)
    """)
    void sendReport(
            @Bind("uuid") UUID uuid,
            @Bind("adminID") UUID adminID,
            @Bind("playerID") UUID playerID,
            @Bind("adminNick") String adminNick,
            @Bind("playerNick") String playerNick,
            @Bind("report") String report,
            @Bind("response") String response,
            @Bind("status") String status
    );

    @SqlUpdate("""
        UPDATE e1admin_reports SET adminID = :adminID, adminNick = :adminNick, response = :response, status = :status,
        WHERE :uuid
    """)
    void updateReport(
            @Bind("adminID") UUID adminID,
            @Bind("adminNick") String adminNick,
            @Bind("response") String response,
            @Bind("status") String status
    );

    @SqlQuery("""
        SELECT uuid, adminID, playerID, adminNick, playerNick, report, response, status = :status,
        FROM e1admin_reports
        WHERE :status
        LIMIT :limit
    """)
    List<Report> getReportList(
            @Bind("status") String status,
            @Bind("limit") int limit
    );

    // Контроллер
    @SqlQuery("""
        SELECT uuid, adminID, playerID, adminNick, playerNick, report, response, status, createdAt
        FROM e1admin_reports
        WHERE :uuid
    """)
    Report getReport(
            @Bind("uuid") UUID uuid
    );
}
