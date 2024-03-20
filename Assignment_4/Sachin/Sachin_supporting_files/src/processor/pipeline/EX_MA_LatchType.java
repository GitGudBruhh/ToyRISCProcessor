package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	Instruction instruction;
	int aluResult;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public void setInstruction(Instruction instruction){
		this.instruction = instruction;
	}

	public void setAluResult(int ALUResult){
		this.aluResult = ALUResult;
	}
	
	public int getAluResult(){
		return this.aluResult;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

}
