package processor.memorysystem;

import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.Event;
import generic.EventQueue;
import generic.Simulator;
import processor.Clock;

public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event e) {
		if(e.getEventType() == Event.EventType.MemoryRead) {
			MemoryReadEvent event = (MemoryReadEvent) e;
			EventQueue eQueue = Simulator.getEventQueue();

			Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
															this,
															event.getRequestingElement(),
															getWord(event.getAddressToReadFrom()));

			eQueue.addEvent(mResponseEvent);
		}

		if(e.getEventType() == Event.EventType.MemoryWrite) {
			MemoryWriteEvent event = (MemoryWriteEvent) e;
			EventQueue eQueue = Simulator.getEventQueue();

			Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
															this,
															event.getRequestingElement(),
															-1);

			eQueue.addEvent(mResponseEvent);
		}
	}
}
