package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface BlockDAO {

    /* ABAN | 🧑‍🔬 */
    @SqlUpdate("""
                INSERT INTO e1admin_aban
                            (uuid, adminID, suspectID, adminNick, suspectNick, IP)
                VALUES
                            (:uuid, :adminID, :suspectID, :adminNick, :suspectNick, :IP)
            """)
    void insertToABan(
            @Bind("uuid") String uuid,
            @Bind("adminID") String adminID,
            @Bind("suspectID") String suspectID,
            @Bind("adminNick") String adminNick,
            @Bind("suspectNick") String suspectNick,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
                DELETE FROM e1admin_aban WHERE suspectID = :suspectID
            """)
    void delAdminABan(
            @Bind("suspectID") String suspectID
    );

    @SqlQuery("""
                SELECT suspectID FROM e1admin_aban WHERE suspectID = :suspectID
            """)
    String checkInABan(
            @Bind("suspectID") String suspectID
    );

    /* BLOCKLIST | 🧑‍🔬 */

    @SqlUpdate("""
                INSERT INTO e1admin_block
                            (uuid, adminID, staffID, adminNick, staffNick, reason, IP)
                VALUES
                            (:uuid, :adminID, :staffID, :adminNick, :staffNick, :reason, :IP)
            """)
    void insertToBlackList(
            @Bind("uuid") String uuid,
            @Bind("adminID") String adminID,
            @Bind("staffID") String suspectID,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String suspectNick,
            @Bind("reason") String reason,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
                DELETE FROM e1admin_block WHERE adminID = :adminID
            """)
    void delAdminBlockList(
            @Bind("adminID") String adminID
    );

    @SqlQuery("""
                SELECT adminID FROM e1admin_block WHERE adminID = :adminID
            """)
    String checkInBlockList(
            @Bind("adminID") String adminID
    );
}
