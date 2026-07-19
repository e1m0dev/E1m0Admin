package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0admin.api.state.secretcode.SecretCodeState;

@RegisterConstructorMapper(SecretCodeState.class)
public interface SecretCodeDAO {

    /* SecretCode | 🧑‍🔬 */
    @SqlUpdate("""
        INSERT INTO e1admin_code
                        (uuid, adminNick, code, regIP)
        VALUES
                (:uuid, :adminNick, :code, :regIP)
    """)
    void systemSetSecretCode(
            @Bind("uuid") String uuid,
            @Bind("adminNick") String nick,
            @Bind("code") int code,
            @Bind("regIP") String IP
    );

    @SqlUpdate("""
        INSERT INTO e1admin_code
                (uuid, adminNick, staffNick, code, regIP)
        VALUES
                (:uuid, :adminNick, :staffNick, :code, :regIP)
    """)
    void staffSetSecretCode(
            @Bind("uuid") String uuid,
            @Bind("adminNick") String anick,
            @Bind("staffNick") String snick,
            @Bind("code") int code,
            @Bind("regIP") String IP
    );

    @SqlUpdate("""
                UPDATE e1admin_code SET code = :code, staffNick = :staffNick WHERE uuid = :uuid
            """)
    void staffUpdateSecretCode(
            @Bind("uuid") String uuid,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String staffNick,
            @Bind("code") int code
    );

    @SqlUpdate("""
                UPDATE e1admin_code SET code = :code, staffNick = :staffNick WHERE uuid = :uuid
            """)
    void systemUpdateSecretCode(
            @Bind("uuid") String uuid,
            @Bind("adminNick") String adminNick,
            @Bind("staffNick") String staffNick,
            @Bind("code") int code
    );

    @SqlUpdate("""
                DELETE FROM e1admin_code WHERE uuid = :adminID
            """)
    void delAdminSecret(
            @Bind("adminID") String adminID
    );

    @SqlQuery("""
                SELECT code
        FROM e1admin_code
        WHERE uuid = :uuid
    """)
    int getCode(
            @Bind("uuid") String uuid
    );


    @SqlQuery("""
            SELECT code
            FROM e1admin_code
            WHERE uuid = :uuid
            """)
    Integer checkCode(
            @Bind("uuid") String uuid
    );
}
