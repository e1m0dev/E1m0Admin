package tvgirl.elmodev.e1m0Admin.state.secretcode;

import java.util.UUID;

public class SecretCodeState {
    private final UUID adminID;
    private int one_step = 0;
    private int two_step = 0;
    private int three_step = 0;
    private int fours_step = 0;

    public SecretCodeState(UUID adminID) {
        this.adminID = adminID;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public int getOne_step() {
        return one_step;
    }

    public int getTwo_step() {
        return two_step;
    }

    public int getThree_step() {
        return three_step;
    }

    public int getFours_step() {
        return fours_step;
    }

    public void setOne_step(int one_step) {
        this.one_step = one_step;
    }

    public void setTwo_step(int two_step) {
        this.two_step = two_step;
    }

    public void setThree_step(int three_step) {
        this.three_step = three_step;
    }

    public void setFours_step(int fours_step) {
        this.fours_step = fours_step;
    }
}
