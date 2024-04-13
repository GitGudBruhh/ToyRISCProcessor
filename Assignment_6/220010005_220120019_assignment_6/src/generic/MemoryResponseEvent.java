package generic;

import processor.memorysystem.CacheLine;

public class MemoryResponseEvent extends Event {

	public enum RequestType {READ, WRITE};

	RequestType r_type;
	int value;
	CacheLine cacheLine;
	Element requestingStage;
	
	public MemoryResponseEvent(long eventTime, Element requestingStage, Element requestingElement, Element processingElement, CacheLine c_line, int value, RequestType r_type) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.cacheLine = c_line;
		this.value = value;
		this.requestingStage = requestingStage;
		this.r_type = r_type;
	}

	public void setRequestingStage(Element requestingStage) {
		this.requestingStage = requestingStage;
	}

	public Element getRequestingStage() {
		return requestingStage;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public CacheLine getCacheLine() {
		return cacheLine;
	}

	public void setCacheLine(CacheLine c_line) {
		this.cacheLine = c_line;
	}

	public RequestType getRequestType() {
		return r_type;
	}

	public void setRequestType(RequestType r_type) {
		this.r_type = r_type;
	}

}
