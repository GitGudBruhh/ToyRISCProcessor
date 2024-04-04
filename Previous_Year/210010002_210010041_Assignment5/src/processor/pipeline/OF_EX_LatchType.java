package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
	int op1, op2, imm;
	boolean EX_busy=false;
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp1() {
		return op1;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getOp2() {
		return op2;
	}

	public void setImm(int imm) {
		this.imm = imm;
	}

	public int getImm() {
		return imm;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public boolean isEX_busy() {
		return EX_busy;
	}

	public void setEX_busy(boolean isEX_busy) {
		this.EX_busy = isEX_busy;
	}
	int opcode;
	public void setopcode(int op1) {
		this.opcode = op1;
	}

	public int getopcode() {
		return opcode;
	}
}
