package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.report.Report;


@RegisterConstructorMapper(Report.class)
public interface ReportDAO {

    /* REPORT | 🚨 */
    @SqlUpdate("""
        INSERT INTO e1admin_reports
        (uuid, adminID, playerID, adminNick, playerNick, report, response, status)
        VALUES
        (:uuid, :adminID, :playerID, :adminNick, :playerNick, :report, :response, :status)
    """)
    void sendReport(
            @Bind("uuid") String uuid,
            @Bind("adminID") String adminID,
            @Bind("playerID") String playerID,
            @Bind("adminNick") String adminNick,
            @Bind("playerNick") String playerNick,
            @Bind("report") String report,
            @Bind("response") String response,
            @Bind("status") String status
    );
}
