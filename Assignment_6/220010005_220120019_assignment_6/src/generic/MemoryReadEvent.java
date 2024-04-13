package generic;

public class MemoryReadEvent extends Event {

	Element requestingStage;
	int addressToReadFrom;
	int nWordsPerLine;
	
	public MemoryReadEvent(long eventTime, Element requestingStage, Element requestingElement, Element processingElement, int address, int nWordsPerLine) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.nWordsPerLine = nWordsPerLine;
		this.requestingStage = requestingStage;
	}

	public void setRequestingStage(Element requestingStage) {
		this.requestingStage = requestingStage;
	}

	public Element getRequestingStage() {
		return requestingStage;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}

	public int getNoWordsPerLine() {
		return nWordsPerLine;
	}

	public void setNoWordsPerLine(int nWordsPerLine) {
		this.nWordsPerLine = nWordsPerLine;
	}
}
