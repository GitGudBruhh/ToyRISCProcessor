package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean IF_busy=false;
	boolean IF_branching_busy=false;
	boolean OF_busy=false;
	
	public IF_OF_LatchType()
	{
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

    public boolean isIF_busy() {
        return IF_busy;
    }

	public void setIF_busy(boolean iF_busy) {
		IF_busy = iF_busy;
	}

	public boolean isIF_branching_busy() {
		return IF_branching_busy;
	}

	public void setIF_branching_busy(boolean iF_branching_busy) {
		IF_branching_busy = iF_branching_busy;
	}

	public boolean isOF_busy() {
		return OF_busy;
	}

	public void setOF_busy(boolean oF_busy) {
		OF_busy = oF_busy;
	}

}
