package tvgirl.elmodev.e1m0Admin.state;

import java.util.UUID;

public record Admin(
        long id,
        UUID uuid,
        String nick,
        int weight,
        int salary,
        String prefix
) {}
