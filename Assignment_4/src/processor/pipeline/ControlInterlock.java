package processor.pipeline;

public class ControlInterlock {
    boolean branchInExTaken;
    // int branchStage;

    public ControlInterlock() {
        this.branchTakenInEx = false;
        // branchStage = new int;
    }

    public boolean isBranchInExTaken() {
        return this.branchInExTaken;
    }

    public void setBranchInExTaken(boolean branchTaken) {
        this.branchInExTaken = branchTaken;
    }

    // public int getBranchStage() {
    //     return this.branchStage;
    // }

    // public void setBranchStage(int bStage) {
    //     this.branchStage = bStage;
    // }
}