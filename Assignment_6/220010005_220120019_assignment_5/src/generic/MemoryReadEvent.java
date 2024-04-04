package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	int nWordsPerLine;
	
	public MemoryReadEvent(long eventTime, Element requestingStage, Element requestingElement, Element processingElement, int address, int nWordsPerLine) {
		super(eventTime, EventType.MemoryRead, requestingStage, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.nWordsPerLine = nWordsPerLine;
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
