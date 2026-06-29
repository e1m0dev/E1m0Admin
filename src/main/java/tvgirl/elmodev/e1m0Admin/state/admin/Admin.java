package tvgirl.elmodev.e1m0Admin.state.admin;

import java.util.UUID;

public record Admin(
        UUID uuid,
        String nick,
        int weight,
        int salary,
        String prefix,
        String ip
) {}
