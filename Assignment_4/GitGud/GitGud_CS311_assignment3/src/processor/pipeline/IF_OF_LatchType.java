package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean isIgnore;
	//added pc
	int pc;
	
	public IF_OF_LatchType() {
		OF_enable = false;
		isIgnore = true;
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

	public boolean isIgnore() {
		return this.isIgnore;
	}

	public void setIgnore(ignore) {
		this.isIgnore = ignore;
	}
}
