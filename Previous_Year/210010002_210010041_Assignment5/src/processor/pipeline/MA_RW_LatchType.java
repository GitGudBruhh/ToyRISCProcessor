package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int aluResult;
	int load_result;
	Instruction instruction;

	public MA_RW_LatchType()
	{
		RW_enable = false;

	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

    public void setALU_result(int alu_result) {
		this.aluResult = alu_result;
    }

	public int getALU_result() {
		return aluResult;
	}

	public void setLoad_result(int load_result) {
		this.load_result = load_result;
	}

	public int getLoad_result() {
		return load_result;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return instruction;
	}
	int opcode;
	public void setopcode(int op1) {
		this.opcode = op1;
	}
	public int getopcode() {
		return opcode;
	}
}
