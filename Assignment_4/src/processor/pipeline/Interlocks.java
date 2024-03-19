package processor.pipeline;

public class Interlocks {

    DataInterlock dI_buffer;
    ControlInterlock cI_buffer;
    // boolean[] pS_buffer;

    DataInterlock dataInterlock;
    ControlInterlock controlInterlock;
    boolean[] pipelineStalls;

    public enum Stages {IF, OF, EX, MA, RW};

    public Interlocks() {
        this.dataInterlock = new DataInterlock();
        this.controlInterlock = new ControlInterlock();
        this.dI_buffer = new DataInterlock();
        this.cI_buffer = new ControlInterlock();
    }

    //======================================================================
    // after-cycle read
    public DataInterlock readDataInterlock() {
        return this.dataInterlock;
    }

    //in-cycle get & write
    public DataInterlock getDataInterlockBuf() {
        return this.dI_buffer;
    }

    public void setDataInterlockBuf(DataInterlock dataInterlock) {
        this.dI_buffer = dataInterlock;
    }

    //======================================================================

    // after-cycle read
    public ControlInterlock readControlInterlock() {
        return this.controlInterlock;
    }

    //in-cycle get & write
    public ControlInterlock getControlInterlockBuf() {
        return this.cI_buffer;
    }

    public void setControlInterlockBuf(ControlInterlock controlInterlock) {
        this.cI_buffer = controlInterlock;
    }

    //======================================================================

    // in-cycle read
    public boolean[] getPipelineStalls() {
        return this.pipelineStalls;
    }

    //after-cycle write
    public boolean getStageStall(int stage) {
        return this.pipelineStalls[stage];
    }

    public void setStageStall(int stage, boolean isStall) {
        this.pipelineStalls[stage] = isStall;
    }

    //in-cycle get & write (unused)
    public boolean getStageStall(int stage) {
        return this.pS_buffer[stage];
    }

    public void setStageStallBuf(int stage, boolean isStall) {
        this.pS_buffer[stage] = isStall;
    }

    //======================================================================

    //after-cycle write (processor's work)
    public void writeBufferContents() {
        this.dataInterlock = dI_buffer;
        this.controlInterlock = cI_buffer;
        this.pipelineStalls = pS_buffer;
    }
}