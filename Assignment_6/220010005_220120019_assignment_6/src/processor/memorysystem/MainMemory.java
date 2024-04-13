package processor.memorysystem;

import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.Event;
import generic.EventQueue;
import generic.Simulator;
import processor.Clock;

public class MainMemory implements Element {
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

	public CacheLine getLine(int address, int nWordsPerLine) {

		int startIdx = nWordsPerLine * (int) (address/nWordsPerLine);
		int nOffsetBits =  (int) Math.ceil((Math.log(nWordsPerLine) / Math.log(2)));

		CacheLine cacheLine = new CacheLine(nWordsPerLine*4);
		int[] d_line = new int[nWordsPerLine];

		for(int i = 0; i < nWordsPerLine; i++) {
			d_line[i] = getWord(startIdx + i);
		}
		cacheLine.setDataLine(d_line);
		cacheLine.setTag(address >> nOffsetBits);
		return cacheLine;
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

			int mAddr = event.getAddressToReadFrom();
			int nWordsPerLine = event.getNoWordsPerLine();

			Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
															event.getRequestingStage(),
															this,
															event.getRequestingElement(),
															getLine(mAddr, nWordsPerLine),
															getWord(mAddr),
															MemoryResponseEvent.RequestType.READ);

			eQueue.addEvent(mResponseEvent);
		}

		if(e.getEventType() == Event.EventType.MemoryWrite) {
			MemoryWriteEvent event = (MemoryWriteEvent) e;
			EventQueue eQueue = Simulator.getEventQueue();

			int mAddr = event.getAddressToWriteTo();
			int value = event.getValue();
			int nWordsPerLine = event.getNoWordsPerLine();

			setWord(mAddr, value);
			System.out.println("Wrote " + value + " to " + mAddr + " in Memory");

			Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
															event.getRequestingStage(),
															this,
															event.getRequestingElement(),
															getLine(mAddr, nWordsPerLine),
															0,
															MemoryResponseEvent.RequestType.WRITE);

			eQueue.addEvent(mResponseEvent);
		}
	}
}
