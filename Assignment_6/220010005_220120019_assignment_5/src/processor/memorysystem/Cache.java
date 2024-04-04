package processor.memorysystem;

import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.Event;
import generic.EventQueue;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import configuration.Configuration;

public class Cache implements Element {
    Processor containingProcessor;
    int INT_MAX = ~(1 << 31);
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
        this.nOffsetBits = (int) (Math.log(nWordsPerLine) / Math.log(2));
        this.nTagBits = 32 - this.nOffsetBits;

        this.cacheArray = new CacheLine[nLines];

        for (int i = 0; i < nLines; i++)
            this.cacheArray[i] = new CacheLine(this.lineSize);
    }

    public boolean isBusy(boolean b) {
        return this.isBusy;
    }

    public void setBusy(boolean b) {
        this.isBusy = b;
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

    // public int readFromAddress(int address) {
    //     if(foundIdx != -1) {
    //         int value = this.cacheArray[foundIdx].getValueAtOffset(offset);
    //         return value;
    //     }
    //     // else {
    //     //
    //     // }
    // }

    @Override
    public void handleEvent() {
        if(e.getEventType() == Event.EventType.CacheRead) {
			CacheReadEvent event = (CacheReadEvent) e;

			int tag = address >> nOffsetBits;
            int offset = (address << nTagBits) >>> nTagBits;

            int foundIdx = -1;
            for(int idx = 0; idx < nLines; idx ++) {
                if(this.cacheArray[idx].getTag() == tag) {
                    foundIdx = idx;
                    break;
                }
            }

            if(foundIdx == -1) {
                EventQueue eQueue = Simulator.getEventQueue();

                EventQueue eQueue = Simulator.getEventQueue();
                MemoryReadEvent mReadEvent = new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                                                this,
                                                                containingProcessor.getMainMemory(),
                                                                programCounter);

                eQueue.addEvent(mReadEvent);
                System.out.println("Added MemoryRead event to queue");
            }
		}

            if(e.getEventType() == Event.EventType.MemoryWrite) {
                MemoryWriteEvent event = (MemoryWriteEvent) e;
                EventQueue eQueue = Simulator.getEventQueue();

                int mAddr = event.getAddressToWriteTo();
                int value = event.getValue();

                setWord(mAddr, value);

                Event mResponseEvent = new MemoryResponseEvent(Clock.getCurrentTime(),
                                                                this,
                                                                event.getRequestingElement(),
                                                                -1);

                eQueue.addEvent(mResponseEvent);
            }
    }
}