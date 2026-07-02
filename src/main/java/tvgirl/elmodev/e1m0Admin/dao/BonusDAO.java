package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public interface BonusDAO {

    /* BONUS | 🧑‍🔬 */
    @SqlUpdate("""
                INSERT INTO e1admin_bonus
        (uuid, staffID, adminID, staffNick, adminNick, sum, message)
        VALUES
        (:uuid, :staffID, :adminID, :staffNick, :adminNick, :sum, :message)
    """)
    void insert(
            @Bind("uuid") String uuid,
            @Bind("staffID") String staffID,
            @Bind("adminID") String adminID,
            @Bind("staffNick") String staffNick,
            @Bind("adminNick") String adminNick,
            @Bind("sum") int sum,
            @Bind("message") String message
    );
}
