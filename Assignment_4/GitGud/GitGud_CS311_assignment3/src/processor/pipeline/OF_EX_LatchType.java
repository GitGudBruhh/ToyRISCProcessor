package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	//added pc, branchTarget, immediates B(immx vs op2), A(op1), operand2, instruction and control signals
	int pc;
	int branchTarget; //goes to - IF - decided by - isRet ????
	int B; //comes from - immx vs (op2 from register file) - decided by - isImmediate
	int A; //comes from - op1
	int op2;
	int instruction;
	ControlSignals controlSignals;

	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
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

	public int getBranchTarget() {
		return this.branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}

	public int getB() {
		return this.B;
	}

	public void setB(int B) {
		this.B = B;
	}

	public int getA() {
		return this.A;
	}

	public void setA(int A) {
		this.A = A;
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
		branchTarget = 0;
		A = 0;
		B = 0;
		op2 = 0;
		controlSignals = new ControlSignals();
	}
}
