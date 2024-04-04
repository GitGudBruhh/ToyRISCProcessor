package generic;

public class MemoryResponseEvent extends Event {

	int value;
	Element requestingStage;
	
	public MemoryResponseEvent(long eventTime, Element requestingStage, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingStage, requestingElement, processingElement);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
