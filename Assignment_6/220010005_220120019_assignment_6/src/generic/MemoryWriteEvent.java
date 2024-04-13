package generic;

public class MemoryWriteEvent extends Event {

	Element requestingStage;
	int addressToWriteTo;
	int value;
	int nWordsPerLine;
	
	public MemoryWriteEvent(long eventTime, Element requestingStage, Element requestingElement, Element processingElement, int address, int value, int nWordsPerLine) {
		super(eventTime, EventType.MemoryWrite, requestingElement, processingElement);
		this.addressToWriteTo = address;
		this.value = value;
		this.nWordsPerLine = nWordsPerLine;
		this.requestingStage = requestingStage;
	}

	public void setRequestingStage(Element requestingStage) {
		this.requestingStage = requestingStage;
	}

	public Element getRequestingStage() {
		return requestingStage;
	}

	public int getAddressToWriteTo() {
		return addressToWriteTo;
	}

	public void setAddressToWriteTo(int addressToWriteTo) {
		this.addressToWriteTo = addressToWriteTo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getNoWordsPerLine() {
		return nWordsPerLine;
	}

	public void setNoWordsPerLine(int nWordsPerLine) {
		this.nWordsPerLine = nWordsPerLine;
	}
}
