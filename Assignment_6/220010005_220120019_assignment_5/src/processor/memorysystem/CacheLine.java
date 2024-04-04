package processor.memorysystem;

public class CacheLine {
    int lineSize;
    int nWords;
    int tag;
    int time;
    int[] dataLine;
    boolean modified;

    public CacheLine(int lineSize) {
        //lineSize is in bytes
        //Each word is 4 bytes
        //no of words * 4 bytes = lineSize in bytes

        this.lineSize = lineSize;
        this.nWords = lineSize/4;
        this.dataLine = new int[this.nWords];
        this.modified = false;
        this.time = -1;
    }

    public int getTimeOfLastAccess() {
        return this.time;
    }

    public void setTimeOfLastAccess(int t) {
        this.time = t;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int t) {
        this.tag = t;
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean b) {
        this.modified = b;
    }

    public int getValueAtOffset(int offset) {
        return this.dataLine[offset];
    }

    public void setValueAtOffset(int offset, int value) {
        this.dataLine[offset] = value;
    }

    public int[] getDataLine() {
        return this.dataLine;
    }

    public void setDataLine(int[] d_line) {
        this.dataLine = d_line;
    }
}
