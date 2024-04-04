package processor.pipeline;

public class EX_IF_LatchType {
	int pc;
	boolean IF_enable;
	
	public EX_IF_LatchType()
	{
		IF_enable = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public int getPC() {
		return pc;
	}

	public void setPC(int pc) {
		this.pc = pc;
	}
}
