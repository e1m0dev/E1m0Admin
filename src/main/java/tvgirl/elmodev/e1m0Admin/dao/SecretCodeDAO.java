package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.admin.Admin;

import java.util.UUID;

public interface SecretCodeDAO {

    /* SecretCode | 🧑‍🔬 */
    @SqlUpdate("""
        INSERT INTO e1admin_code
        (uuid, adminNick, code, regIP)
        VALUES
        (:uuid, :nick, :code, :regIP)
    """)
    void systemSetSecretCode(
            @Bind("uuid") UUID uuid,
            @Bind("adminNick") String nick,
            @Bind("code") int code,
            @Bind("regIP") String IP
    );

    @SqlUpdate("""
        INSERT INTO e1admin_code
        (uuid, adminNick, staffNick code, regIP)
        VALUES
                (:uuid, :adminNick, :staffNick, :code, :regIP)
    """)
    void staffSetSecretCode(
            @Bind("uuid") UUID uuid,
            @Bind("adminNick") String anick,
            @Bind("staffNick") String snick,
            @Bind("code") int code,
            @Bind("regIP") String IP
    );

    @SqlUpdate("""
        UPDATE e1admin_code SET
        code = :code, staffNick = :staffNick
        WHERE
        :uuid
    """)
    void staffChangeSecretCode(
            @Bind("uuid") UUID uuid,
            @Bind("stuffNick") String staffNick,
            @Bind("code") int code
    );

    @SqlQuery("""
        SELECT *
        FROM e1admin_code
        WHERE uuid = :uuid
    """)
    byte getCode(
            @Bind("uuid") UUID uuid
    );
}
