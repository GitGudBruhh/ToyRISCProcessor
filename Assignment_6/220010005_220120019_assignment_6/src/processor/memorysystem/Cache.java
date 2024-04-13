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

    public boolean isBusy() {
        return this.isBusy;
    }

    public void setBusy(boolean b) {
        this.isBusy = b;
    }

    public int getWordsPerLine() {
        return nWordsPerLine;
    }

    public void setLine(CacheLine c_Line, int idx) {
        this.cacheArray[idx] = c_Line;
    }

    public int findLeastRecentlyUsedIdx() {
        long min_time = Long.MAX_VALUE;
        int min_idx = -1;
        for (int idx = 0; idx < this.nLines; idx++)
            if(this.cacheArray[idx].getTimeOfLastAccess() < min_time) {
                min_idx = idx;
                min_time = this.cacheArray[idx].getTimeOfLastAccess();
            }
        return min_idx;
    }

    public void placeLine(CacheLine c_line) {
        int LRU_idx = findLeastRecentlyUsedIdx();
        c_line.setModified(true);
        c_line.setTimeOfLastAccess(Clock.getCurrentTime());

        this.cacheArray[LRU_idx] = c_line;
        return;
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            EventQueue eQueue = Simulator.getEventQueue();
            Element requestingStage = event.getRequestingStage();
            CacheLine returnedLine = event.getCacheLine();

            placeLine(returnedLine);

            if(event.getRequestType() == MemoryResponseEvent.RequestType.READ) {
                Event cResponseEvent = new CacheResponseEvent(Clock.getCurrentTime(),
                                                            this,
                                                            requestingStage,
                                                            event.getValue(),
                                                            CacheResponseEvent.RequestType.READ);
                eQueue.addEvent(cResponseEvent);
                System.out.println("Value fetched from memory");
                this.setBusy(false);
            }
            if(event.getRequestType() == MemoryResponseEvent.RequestType.WRITE) {
                Event cResponseEvent = new CacheResponseEvent(Clock.getCurrentTime(),
                                                            this,
                                                            requestingStage,
                                                            -1,
                                                            CacheResponseEvent.RequestType.WRITE);
                // NOTE: WHAT IS HAPPENING HERE????
                eQueue.addEvent(cResponseEvent);
                System.out.println("Stored, value fetched from memory");
                this.setBusy(false);
            }
            return;
        }

        if(this.isBusy()) {
            e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
			return;
        }
        else {
            if(e.getEventType() == Event.EventType.CacheRead) {
                CacheReadEvent event = (CacheReadEvent) e;
                EventQueue eQueue = Simulator.getEventQueue();

                int address = event.getAddressToReadFrom();
                int tag = address >> nOffsetBits;
                // int offset = (int) (((long) address << nTagBits) >>> nTagBits);
                int offset = 0;

                int foundIdx = -1;

                for(int idx = 0; idx < nLines; idx++) {
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
                    this.setBusy(true);
                }
                else {
                    CacheLine hitCacheLine = this.cacheArray[foundIdx];
                    System.out.println("WHATT " + offset + " " + nTagBits + " " + nOffsetBits + " " + address + " EEE " + (address << 32));
                    int value = hitCacheLine.getValueAtOffset(offset);
                    Event cResponseEvent = new CacheResponseEvent(Clock.getCurrentTime(),
                                                                this,
                                                                event.getRequestingElement(),
                                                                value,
                                                                CacheResponseEvent.RequestType.READ);
                    // NOTE: WHAT IS HAPPENING HERE????
                    eQueue.addEvent(cResponseEvent);
                    System.out.println("Cache Hit!");
                    System.out.println("CACHE HIT AT " + " foundIdx: " + foundIdx + " address: " + address + " value" + value);
                    this.setBusy(false);
                }
            }

            else if(e.getEventType() == Event.EventType.CacheWrite) {
                CacheWriteEvent event = (CacheWriteEvent) e;
                EventQueue eQueue = Simulator.getEventQueue();

                int mAddr = event.getAddressToWriteTo();
                int value = event.getValue();

                MemoryWriteEvent mWriteEvent = new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                                                event.getRequestingElement(),
                                                                this,
                                                                containingProcessor.getMainMemory(),
                                                                mAddr,
                                                                value,
                                                                nWordsPerLine);

                eQueue.addEvent(mWriteEvent);
                System.out.println("Added MemoryWrite event to queue");
                this.setBusy(true);
            }

        }
    }
}