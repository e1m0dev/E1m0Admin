package tvgirl.elmodev.e1m0Admin.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tvgirl.elmodev.e1m0Admin.state.Admin;

import java.util.UUID;

public interface AdminsDAO {

    @SqlUpdate("""
        INSERT INTO e1admin_admins
        (uuid, nick, weight, salary prefix)
        VALUES
        (:uuid, :nick, :weight, :salary :prefix)
    """)
    void insert(
            @Bind("uuid") UUID uuid,
            @Bind("nick") String nick,
            @Bind("weight") int weight,
            @Bind("salary") int salary,
            @Bind("prefix") String prefix
    );

    @SqlQuery("""
        SELECT *
        FROM e1admin_admins
        WHERE uuid = :uuid
    """)

    Admin findByUuid(@Bind("uuid") UUID uuid);
}
