package processor.pipeline;

public class EX_IF_LatchType {
	
	//added branchPC, controlSignals (use only isBranchTaken)
	int branchPC;
	ControlSignals controlSignals;

	public EX_IF_LatchType()
	{
		this.controlSignals = new ControlSignals();
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

}
