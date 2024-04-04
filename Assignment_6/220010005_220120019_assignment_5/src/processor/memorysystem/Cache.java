package processor.memorysystem;

import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.CacheReadEvent;
import generic.CacheWriteEvent;
import generic.CacheResponseEvent;
import generic.Event;
import generic.EventQueue;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import configuration.Configuration;

public class Cache implements Element {
    Processor containingProcessor;
    int lineSize;
    int nLines;
    int nWordsPerLine;
    int nOffsetBits;
    int nTagBits;
    CacheLine[] cacheArray;

    boolean isBusy;

    public Cache(Processor containingProcessor, int lineSize, int nLines) {
        this.containingProcessor = containingProcessor;
        this.lineSize = lineSize;
        this.nLines = nLines;
        this.nWordsPerLine = lineSize/4;
        this.nOffsetBits =  (int) Math.ceil((Math.log(nWordsPerLine) / Math.log(2)));
        this.nTagBits = 32 - this.nOffsetBits;

        this.cacheArray = new CacheLine[nLines];

        for (int i = 0; i < nLines; i++)
            this.cacheArray[i] = new CacheLine(this.lineSize);
    }

    // public boolean isBusy(boolean b) {
    //     return this.isBusy;
    // }
    //
    // public void setBusy(boolean b) {
    //     this.isBusy = b;
    // }

    public int getWordsPerLine() {
        return nWordsPerLine;
    }

    public void setLine(CacheLine c_Line, int idx) {
        this.cacheArray[idx] = c_Line;
    }

    public int findLeastRecentlyUsedIdx() {
        int min_time = Integer.MAX_VALUE;
        int min_idx = -1;
        for (int idx = 0; idx < this.nLines; idx++)
            if(this.cacheArray[idx].getTimeOfLastAccess() < min_time) {
                min_idx = idx;
                min_time = this.cacheArray[idx].getTimeOfLastAccess();
            }
        return min_idx;
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == Event.EventType.CacheRead) {
			CacheReadEvent event = (CacheReadEvent) e;

			int address = event.getAddressToReadFrom();
			int tag = address >> nOffsetBits;
            int offset = (address << nTagBits) >>> nTagBits;

            int foundIdx = -1;

            EventQueue eQueue = Simulator.getEventQueue();

            for(int idx = 0; idx < nLines; idx ++) {
                if(this.cacheArray[idx].getTag() == tag && this.cacheArray[idx].isModified()) {
                    foundIdx = idx;
                    break;
                }
            }

            if(foundIdx == -1) {
                MemoryReadEvent mReadEvent = new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                                                event.getRequestingElement(),
                                                                this,
                                                                containingProcessor.getMainMemory(),
                                                                address,
                                                                nWordsPerLine);

                eQueue.addEvent(mReadEvent);
                System.out.println("Cache Miss. Added MemoryRead event to queue");
            }
            else {
                Event cResponseEvent = new CacheResponseEvent(Clock.getCurrentTime(),
															this,
															event.getRequestingElement(),
															-1);
                eQueue.addEvent(cResponseEvent);
            }
		}

        if(e.getEventType() == Event.EventType.CacheWrite) {
            CacheWriteEvent event = (CacheWriteEvent) e;
            EventQueue eQueue = Simulator.getEventQueue();

            int mAddr = event.getAddressToWriteTo();
            int value = event.getValue();

            // setWord(mAddr, value);
            //
            // Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
            //                                                 this,
            //                                                 event.getRequestingElement(),
            //                                                 -1);

            eQueue = Simulator.getEventQueue();
            MemoryWriteEvent mWriteEvent = new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                                            event.getRequestingElement(),
                                                            this,
                                                            containingProcessor.getMainMemory(),
                                                            mAddr,
                                                            value,
                                                            nWordsPerLine);

			eQueue.addEvent(mWriteEvent);
			System.out.println("Added IF event to queue");
        }

        if(e.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            EventQueue eQueue = Simulator.getEventQueue();

            for(int idx = 0; idx < nLines; idx ++) {
                if(this.cacheArray[idx].getTag() == tag && this.cacheArray[idx].isModified()) {
                    foundIdx = idx;
                    break;
                }
            }
        }
    }
}