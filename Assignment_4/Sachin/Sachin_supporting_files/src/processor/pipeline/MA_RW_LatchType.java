package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	int aluResult;
	int ldResult;
	Instruction instruction;
	boolean RW_enable;

	public int getLdResult(){
		return this.ldResult;
	}

	public void setLdResult(int LDResult){
		this.ldResult = LDResult;
	}

	public int getAluResult(){
		return this.aluResult;
	}

	public void setInstruction(Instruction instruction){
		this.instruction = instruction;
	}

	public void setAluResult(int ALUResult){
		this.aluResult = ALUResult;
	}

	public Instruction getInstruction() {
		return instruction;
	}
	
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

}
