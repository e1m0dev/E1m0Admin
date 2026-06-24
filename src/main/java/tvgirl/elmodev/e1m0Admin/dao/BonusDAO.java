package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public interface BonusDAO {

    /* STAFF | 🧑‍🔬 */
    @SqlUpdate("""
        INSERT INTO e1admin_reports
        (uuid, staffID, adminID, staffNick, adminNick, sum, message)
        VALUES
        (:uuid, :staffID, :adminID, :staffNick, :adminNick, :sum, :message)
    """)
    void insert(
            @Bind("uuid") UUID uuid,
            @Bind("staffID") UUID staffID,
            @Bind("adminID") UUID adminID,
            @Bind("staffNick") String staffNick,
            @Bind("adminNick") String adminNick,
            @Bind("sum") int sum,
            @Bind("message") String message
    );
}
