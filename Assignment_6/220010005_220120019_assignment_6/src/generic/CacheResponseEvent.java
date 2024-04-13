package generic;

public class CacheResponseEvent extends Event {

	public enum RequestType {READ, WRITE};

	RequestType r_type;
	int value;
	
	public CacheResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, RequestType r_type) {
		super(eventTime, EventType.CacheResponse, requestingElement, processingElement);
		this.value = value;
		this.r_type = r_type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public RequestType getRequestType() {
		return r_type;
	}

	public void setRequestType(RequestType r_type) {
		this.r_type = r_type;
	}

}
