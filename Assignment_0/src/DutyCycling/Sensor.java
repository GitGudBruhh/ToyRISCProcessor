package DutyCycling;
import java.util.*;

public class Sensor {
    public boolean isOn;

    public void flipACoin(double probability)
    {
        double sample = Math.random();
        if(sample <= probability)
            this.isOn = true;
        else
            this.isOn = false;
    }
}
