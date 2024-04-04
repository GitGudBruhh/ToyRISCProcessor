package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean freeze;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
		freeze = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean isFreeze() {
		return freeze;
	}

	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}

}
