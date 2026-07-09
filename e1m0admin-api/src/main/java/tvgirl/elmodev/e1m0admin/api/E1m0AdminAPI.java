package tvgirl.elmodev.e1m0admin.api;

import tvgirl.elmodev.e1m0admin.api.gui.ReportGuiAPI;
import tvgirl.elmodev.e1m0admin.api.gui.SecretCodeGuiAPI;
import tvgirl.elmodev.e1m0admin.api.repo.GameRepositoryAPI;
import tvgirl.elmodev.e1m0admin.api.repo.StaffRepositoryAPI;
import tvgirl.elmodev.e1m0admin.api.repo.SystemRepositoryAPI;
import tvgirl.elmodev.e1m0admin.api.repo.gui.ReportSystemRepositoryAPI;
import tvgirl.elmodev.e1m0admin.api.repo.gui.SecretCodeRepositoryAPI;
import tvgirl.elmodev.e1m0admin.api.service.ConsoleServiceAPI;
import tvgirl.elmodev.e1m0admin.api.service.GameServiceAPI;
import tvgirl.elmodev.e1m0admin.api.service.StaffServiceAPI;
import tvgirl.elmodev.e1m0admin.api.service.SystemServiceAPI;
import tvgirl.elmodev.e1m0admin.api.service.gui.ReportSystemServiceAPI;
import tvgirl.elmodev.e1m0admin.api.service.gui.SecretCodeServiceAPI;
import tvgirl.elmodev.e1m0admin.api.utils.PermissionsManagerAPI;

public interface E1m0AdminAPI {

    /* 🌐 | GUI */
    ReportGuiAPI reportUI();

    SecretCodeGuiAPI secretCodeUI();

    /* 📊 | REPOSITORY */
    /* 📊 | PLUGIN */
    GameRepositoryAPI gameStorage();

    StaffRepositoryAPI staffStorage();

    SystemRepositoryAPI systemStorage();

    /* 📊 | GUI */
    ReportSystemRepositoryAPI reportSystemStorage();

    SecretCodeRepositoryAPI secretCodeStorage();

    /* 🧑‍💻 | Service */
    /* 🧑‍💻 | PLUGIN */
    GameServiceAPI gameLogic();

    StaffServiceAPI staffLogic();

    SystemServiceAPI systemLogic();

    ConsoleServiceAPI consoleLogic();

    /* 🧑‍💻 | GUI */
    ReportSystemServiceAPI reportSystemLogic();

    SecretCodeServiceAPI secretCodeLogic();

    /* 🧑‍💻 | UTILS */
    PermissionsManagerAPI permissions();
}
