package generic;

public class Event {
	
	public enum EventType {ExecutionComplete, MemoryRead, MemoryResponse, MemoryWrite, CacheRead, CacheWrite, CacheResponse};
	
	long eventTime;
	Element requestingStage;
	Element requestingElement;
	Element processingElement;
	EventType eventType;
	
	public Event(long eventTime, EventType eventType, Element requestingStage, Element requestingElement, Element processingElement)
	{
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.requestingStage = requestingStage;
		this.requestingElement = requestingElement;
		this.processingElement = processingElement;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public void setRequestingStage(Element requestingStage) {
		this.requestingStage = requestingStage;
	}

	public Element getRequestingStage() {
		return requestingStage;
	}

	public Element getRequestingElement() {
		return requestingElement;
	}

	public void setRequestingElement(Element requestingElement) {
		this.requestingElement = requestingElement;
	}

	public Element getProcessingElement() {
		return processingElement;
	}

	public void setProcessingElement(Element processingElement) {
		this.processingElement = processingElement;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
