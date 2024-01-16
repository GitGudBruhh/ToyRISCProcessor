package DutyCycling;
// import SensorPKG.Sensor;

public class Border {
    public int length = 200; //Can change later
    public int width;
    public Sensor[][] arrayOfSensors;

    public Border(int width)
    {
        this.width = width;
        this.arrayOfSensors = new Sensor[length][width];
        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < this.width; j++)
            {
                this.arrayOfSensors[i][j] = new Sensor();
            }
        }

    }

    public void updateAllSensors(double probability)
    {
        for(int i = 0; i < this.length; i++)
            for(int j = 0; j < this.width; j++)
                arrayOfSensors[i][j].flipACoin(probability);
    }
}
