package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface BlockDAO {

    /* ABAN | 🧑‍🔬 */
    @SqlUpdate("""
                INSERT INTO e1admin_aban
                            (uuid, adminID, staffID, adminNick, staffNick, IP)
                VALUES
                            (:uuid, :adminID, :staffID, :adminNick, :staffNick, :IP
            """)
    void insertToABan(
            @Bind("uuid") String uuid,
            @Bind("adminID") String adminID,
            @Bind("staffID") String staffID,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String staffNick,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
                DELETE FROM e1admin_aban WHERE uuid = :adminID
            """)
    void delAdminABan(
            @Bind("adminID") String adminID
    );

    @SqlUpdate("""
                DELETE FROM e1admin_aban WHERE uuid = :adminID
            """)
    boolean checkInABan(
            @Bind("adminID") String adminID
    );

    /* BLOCKLIST | 🧑‍🔬 */

    @SqlUpdate("""
                INSERT INTO e1admin_block
                            (uuid, adminID, staffID, adminNick, staffNick, reason, IP)
                VALUES
                            (:uuid, :adminID, :staffID, :adminNick, :staffNick, :reason, :IP
            """)
    void insertToBlackList(
            @Bind("uuid") String uuid,
            @Bind("adminID") String adminID,
            @Bind("staffID") String staffID,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String staffNick,
            @Bind("reason") String reason,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
                DELETE FROM e1admin_block WHERE uuid = :adminID
            """)
    void delAdminBlockList(
            @Bind("adminID") String adminID
    );

    @SqlUpdate("""
                DELETE FROM e1admin_block WHERE uuid = :adminID
            """)
    boolean checkInBlockList(
            @Bind("adminID") String adminID
    );
}
