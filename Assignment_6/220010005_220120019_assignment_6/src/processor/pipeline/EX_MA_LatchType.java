package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_busy;
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
		controlSignals = new ControlSignals();
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

	public void setNop() {
		instruction = 0;
		pc = 0;
		op2 = 0;
		aluResult = 0;
		controlSignals = new ControlSignals();
	}

	public boolean isMA_busy() {
		return MA_busy;
	}

	public void setMA_busy(boolean mA_busy) {
		MA_busy = mA_busy;
	}

	public void display() {
		System.out.println("-----------------------------------");
		System.out.println("EX_MA: ");
		System.out.println(" instruction - " + instruction + ", pc - " + pc + ", aluResult - " + aluResult + ", op2 - " + op2);
		controlSignals.display();
	}
}
