package processor.pipeline;

public class EX_IF_LatchType {
	
	//added branchPC, controlSignals (use only isBranchTaken)
	int branchPC;
	ControlSignals controlSignals;
	ControlSignals cSig_buf;

	public EX_IF_LatchType()
	{
		this.controlSignals = new ControlSignals();
		this.cSig_buf = new ControlSignals();
	}

	public int getBranchPC() {
		return this.branchPC;
	}

	public void setBranchPC(int branchPC) {
		this.branchPC = branchPC;
	}

	public ControlSignals getControlSignals() {
		return this.controlSignals;
	}

	public void setControlSignals(ControlSignals controlSignals) {
		this.controlSignals = controlSignals;
	}

	public void setNop() {
		branchPC = 0;
		controlSignals = new ControlSignals();
		// cSig_buf = new ControlSignals();
	}
}
