package DutyCycling;
// import SensorPKG.Sensor;

public class Border {
    int length = 1000; //Can change later
    int width;
    Sensor[][] arrayOfSensors;

    public Border(int width)
    {
        this.width = width;
        for(int i=0;i<length;i++){
            for(int j = 0; j < width; j++)
                this.arrayOfSensors[i][j] = new Sensor();
        }

    }

    public int getLength()
    {
        return this.length;
    }

    public int getWidth()
    {
        return this.width;
    }

    public void updateAllSensors(int probability)
    {
        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.length; j++)
                arrayOfSensors[i][j].flipACoin(probability);
    }
}
