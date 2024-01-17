package DutyCycling;
public class Clock
{
    private int time = 0;

    public int getTime()
    {
        return this.time;
    }
    public void incrementClockTime(int delta)
    {
        this.time += delta;
    }
}
