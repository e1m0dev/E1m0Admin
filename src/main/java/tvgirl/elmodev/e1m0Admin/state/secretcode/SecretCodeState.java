package tvgirl.elmodev.e1m0Admin.state.secretcode;

import java.util.UUID;

public class SecretCodeState {
    private final UUID adminID;
    private byte one_step = 0;
    private byte two_step = 0;
    private byte three_step = 0;
    private byte fours_step = 0;

    public SecretCodeState(UUID adminID) {
        this.adminID = adminID;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public byte getOne_step() {
        return one_step;
    }

    public byte getTwo_step() {
        return two_step;
    }

    public byte getThree_step() {
        return three_step;
    }

    public byte getFours_step() {
        return fours_step;
    }

    public void setOne_step(byte one_step) {
        this.one_step = one_step;
    }

    public void setTwo_step(byte two_step) {
        this.two_step = two_step;
    }

    public void setThree_step(byte three_step) {
        this.three_step = three_step;
    }

    public void setFours_step(byte fours_step) {
        this.fours_step = fours_step;
    }
}
