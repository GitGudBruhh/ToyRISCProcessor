package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	//added pc, ldResult, aluResult, instruction, controlSignals
	int pc;
	int ldResult;
	long aluResult;
	int instruction;
	ControlSignals controlSignals;

	boolean isIgnore;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		isIgnore = true;
		controlSignals = new ControlSignals();
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getPc() {
		return this.pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getLdResult() {
		return this.ldResult;
	}

	public void setLdResult(int ldResult) {
		this.ldResult = ldResult;
	}

	public ControlSignals getControlSignals() {
		return this.controlSignals;
	}


	public long getAluResult() {
		return this.aluResult;
	}

	public void setAluResult(long aluResult) {
		this.aluResult = aluResult;
	}

	public void setControlSignals(ControlSignals controlSignals) {
		this.controlSignals = controlSignals;
	}

	public boolean isIgnore() {
		return this.isIgnore;
	}

	public void setIgnore(boolean ignore) {
		this.isIgnore = ignore;
	}

}
