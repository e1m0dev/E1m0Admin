package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;

import java.util.UUID;

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
        (uuid, adminNick, staffNick code, regIP)
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

    @SqlQuery("""
                SELECT code
        FROM e1admin_code
        WHERE uuid = :uuid
    """)
    int getCode(
            @Bind("uuid") String uuid
    );
}
