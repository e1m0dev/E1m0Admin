package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

@RegisterConstructorMapper(Admin.class)
public interface AdminsDAO {

    /* STAFF | 🧑‍🔬 */
    @SqlUpdate("""
        INSERT INTO e1admin_admins
                    (uuid, nick, weight, salary, prefix, IP)
        VALUES
                    (:uuid, :nick, :weight, :salary, :prefix, :IP)
    """)
    void insert(
            @Bind("uuid") String uuid,
            @Bind("nick") String nick,
            @Bind("weight") int weight,
            @Bind("salary") int salary,
            @Bind("prefix") String prefix,
            @Bind("IP") String IP
    );

    @SqlUpdate("""
            INSERT INTO e1admin_deletedadminslogs
                (uuid, adminNick, staffNick, reason)
            VALUES
                (:uuid, :adminNick, :staffNick, :reason)
            """)
    void delAdminLogInsert(
            @Bind("uuid") String UUID,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String staffNick,
            @Bind("reason") String reason
    );

    @SqlUpdate("""
        UPDATE e1admin_admins SET
                weight = :weight, salary = :salary, prefix = :prefix
        WHERE
                uuid = :uuid
    """)
    void upStatus(
            @Bind("uuid") String uuid,
            @Bind("prefix") String prefix,

            @Bind("weight") int weight,
            @Bind("salary") int salary
    );

    @SqlUpdate("""
                UPDATE e1admin_admins SET
                    compliments = compliments + 1
                WHERE
                    uuid = :uuid
            """)
    void addCompliment(
            @Bind("uuid") String uuid
    );

    @SqlUpdate("""
        UPDATE e1admin_admins SET
                weight = :weight, salary = :salary, prefix = :prefix
        WHERE
                uuid = :uuid
    """)
    void downStatus(
            @Bind("uuid") String uuid,
            @Bind("prefix") String prefix,

            @Bind("weight") int weight,
            @Bind("salary") int salary
    );

    @SqlUpdate("""
                DELETE FROM e1admin_admins WHERE uuid = :adminID
            """)
    void delAdmin(
            @Bind("adminID") String adminID
    );

    @SqlQuery("""
                SELECT 
                    uuid, nick, weight, salary, prefix, ip
        FROM e1admin_admins
                WHERE 
                    uuid = :uuid
    """)
    Admin findByUuid(@Bind("uuid") String uuid);

    @SqlQuery("""
                SELECT 
                    compliments
                FROM e1admin_admins
                WHERE 
                    uuid = :uuid
            """)
    int getCompliments(@Bind("uuid") String uuid);
}
