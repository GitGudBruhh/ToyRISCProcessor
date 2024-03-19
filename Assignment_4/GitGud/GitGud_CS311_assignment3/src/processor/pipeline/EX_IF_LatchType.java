package processor.pipeline;

public class EX_IF_LatchType {
	
	//added branchPC, controlSignals (use only isBranchTaken)
	int branchPC_buf;
	int branchPC;
	ControlSignals controlSignals;
	ControlSignals controlSignals_buf;

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

	public int getBranchPC_buf() {
		return this.branchPC_buf;
	}

	public void setBranchPC_buf(int branchPC) {
		this.branchPC_buf = branchPC;
	}

	public ControlSignals getControlSignals_buf() {
		return this.controlSignals_buf;
	}

	public void setControlSignals_buf(ControlSignals controlSignals) {
		this.controlSignals_buf = controlSignals;
	}

	public void writeBuffer() {
		branchPC = branchPC_buf;
		controlSignals = controlSignals_buf;
	}
}
