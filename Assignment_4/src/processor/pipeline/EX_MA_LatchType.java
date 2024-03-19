package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	//added pc, aluResult, op2, instruction, controlSignals
	int pc;
	long aluResult;
	int op2;
	int instruction;
	ControlSignals controlSignals;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		controlSignals.setMiscSignal(ControlSignals.MiscSignals.IGNORE.ordinal(), true);

	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
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

	public long getAluResult() {
		return this.aluResult;
	}

	public void setAluResult(long aluResult) {
		this.aluResult = aluResult;
	}

	public int getOp2() {
		return this.op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public ControlSignals getControlSignals() {
		return this.controlSignals;
	}

	public void setControlSignals(ControlSignals controlSignals) {
		this.controlSignals = controlSignals;
	}
}
