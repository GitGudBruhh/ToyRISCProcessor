package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	boolean OF_busy;
	int instruction;
	//added pc
	int pc;
	
	public IF_OF_LatchType() {
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
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

	public void setNop() {
		instruction = 0;
		pc = 0;
	}

	public boolean isOF_busy() {
		return OF_busy;
	}

	public void setOF_busy_due_to_EX(boolean oF_busy) {
		OF_busy = oF_busy;
	}
}
