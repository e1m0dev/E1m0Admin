package tvgirl.elmodev.e1m0Admin.api.service.gui;

import tvgirl.elmodev.e1m0Admin.state.secretcode.SecretCodeState;

import java.util.UUID;

public interface SecretCodeServiceAPI {
    void oneStepHandler(UUID id, int i);

    String getInputCode(UUID id);
}
