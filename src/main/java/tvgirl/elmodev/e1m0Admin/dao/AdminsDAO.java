package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public interface AdminsDAO {

    /* STAFF | 🧑‍🔬 */
    @SqlUpdate("""
        INSERT INTO e1admin_admins
        (uuid, nick, weight, salary, prefix, IP)
        VALUES
        (:uuid, :nick, :weight, :salary, :prefix, :IP)
    """)
    void insert(
            @Bind("uuid") UUID uuid,
            @Bind("nick") String nick,
            @Bind("weight") int weight,
            @Bind("salary") int salary,
            @Bind("prefix") String prefix,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
        UPDATE e1admin_admins SET
                weight = :weight, salary = :salary, prefix = :prefix
        WHERE
        :uuid
    """)
    void upStatus(
            @Bind("uuid") UUID uuid,
            @Bind("weight") int weight,
            @Bind("salary") int salary,
            @Bind("prefix") String prefix
    );

    @SqlUpdate("""
        UPDATE e1admin_admins SET
                weight = :weight, salary = :salary, prefix = :prefix
        WHERE
        :uuid
    """)
    void downStatus(
            @Bind("uuid") UUID uuid,
            @Bind("weight") int weight,
            @Bind("salary") int salary,
            @Bind("prefix") String prefix
    );

    @SqlUpdate("""
                DELETE FROM e1admin_admins WHERE uuid = :uuid
            """)
    void delAdmin(
            @Bind("adminID") UUID adminID
    );

    @SqlUpdate("""
                INSERT INTO e1admin_deletedAdminsLog
                (adminID, staffID, reason)
                VALUES
                (:adminID, :staffID, :reason)
            """)
    void delAdminLog(
            @Bind("adminID") UUID adminID,
            @Bind("staffID") UUID staffID,
            @Bind("reason") String reason
    );

    @SqlQuery("""
        SELECT *
        FROM e1admin_admins
        WHERE uuid = :uuid
    """)
    Admin findByUuid(@Bind("uuid") UUID uuid);
}
