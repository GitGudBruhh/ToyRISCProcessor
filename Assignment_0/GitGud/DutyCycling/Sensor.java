package DutyCycling;
import java.util.*;

public class Sensor {
    boolean isOn;

    public void flipACoin(int probability)
    {
        int sample = (int) Math.random();
        if(sample <= probability)
            this.isOn = true;
        else
            this.isOn = false;
    }
}
