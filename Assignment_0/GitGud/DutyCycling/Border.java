package DutyCycling;
// import SensorPKG.Sensor;

public class Border {
    public int length = 1000; //Can change later
    public int width;
    public Sensor[][] arrayOfSensors;

    public Border(int width)
    {
        this.width = width;
        for(int i=0;i<length;i++){
            for(int j = 0; j < width; j++)
                this.arrayOfSensors[i][j] = new Sensor();
        }

    }

    public void updateAllSensors(float probability)
    {
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.length; j++)
                arrayOfSensors[i][j].flipACoin(probability);
    }
}
