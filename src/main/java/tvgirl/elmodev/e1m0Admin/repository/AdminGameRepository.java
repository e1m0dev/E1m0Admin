package tvgirl.elmodev.e1m0Admin.repository;

import org.jdbi.v3.core.Jdbi;
import tvgirl.elmodev.e1m0Admin.api.repo.GameRepositoryAPI;

public class AdminGameRepository implements GameRepositoryAPI {

    private final Jdbi jdbi;

    public AdminGameRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

}
