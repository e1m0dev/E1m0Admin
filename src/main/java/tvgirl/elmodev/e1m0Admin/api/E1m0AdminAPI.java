package tvgirl.elmodev.e1m0Admin.api;

import tvgirl.elmodev.e1m0Admin.api.gui.ReportGuiAPI;
import tvgirl.elmodev.e1m0Admin.api.gui.SecretCodeGuiAPI;
import tvgirl.elmodev.e1m0Admin.api.repo.GameRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.api.repo.StaffRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.api.repo.SystemRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.api.repo.gui.ReportSystemRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.api.repo.gui.SecretCodeRepositoryAPI;
import tvgirl.elmodev.e1m0Admin.api.service.ConsoleServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.service.GameServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.service.StaffServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.service.SystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.service.gui.ReportSystemServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.service.gui.SecretCodeServiceAPI;
import tvgirl.elmodev.e1m0Admin.api.utils.PermissionsManagerAPI;
import tvgirl.elmodev.e1m0Admin.api.utils.SenderAPI;

public interface E1m0AdminAPI {

    /* 🌐 | GUI */
    ReportGuiAPI reportGuiAPI();

    SecretCodeGuiAPI secretCodeGuiAPI();

    /* 📊 | REPOSITORY */
    /* 📊 | PLUGIN */
    GameRepositoryAPI gameRepositoryAPI();

    StaffRepositoryAPI staffRepositoryAPI();

    SystemRepositoryAPI systemRepositoryAPI();

    /* 📊 | GUI */
    ReportSystemRepositoryAPI reportSystemRepositoryAPI();

    SecretCodeRepositoryAPI secretCodeRepositoryAPI();

    /* 🧑‍💻 | Service */
    /* 🧑‍💻 | PLUGIN */
    GameServiceAPI gameServiceAPI();

    StaffServiceAPI staffServiceAPI();

    SystemServiceAPI systemServiceAPI();

    ConsoleServiceAPI consoleServiceAPI();

    /* 🧑‍💻 | GUI */
    ReportSystemServiceAPI reportSystemServiceAPI();

    SecretCodeServiceAPI secretCodeServiceAPI();

    /* 🧑‍💻 | UTILS */
    SenderAPI pluginSenderAPI();

    PermissionsManagerAPI permissionManagerAPI();
}
