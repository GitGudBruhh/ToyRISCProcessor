package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean IF_busy;
	boolean IF_busy_due_to_OF;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public boolean isIF_busy() {
		return IF_busy;
	}

	public void setIF_busy(boolean iF_busy) {
		IF_busy = iF_busy;
	}

	public void setIF_busy_due_to_OF(boolean iF_busy) {
		IF_busy_due_to_OF = iF_busy;
	}

}
