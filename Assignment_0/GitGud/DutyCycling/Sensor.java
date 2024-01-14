package DutyCycling;
import java.util.*;

public class Sensor {
    public boolean isOn;

    public void flipACoin(float probability)
    {
        int sample = (int) Math.random();
        if(sample <= probability)
            this.isOn = true;
        else
            this.isOn = false;
    }
}
