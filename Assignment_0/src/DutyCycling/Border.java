package DutyCycling;
// import SensorPKG.Sensor;

public class Border {
    public int length = 1000; //Can change later
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

    public void updateAllSensors(double probability, int posX, int posY)
    {
        arrayOfSensors[posX][posY].flipACoin(probability);
        if(posY != this.width - 1)
        {
            if(posX != 0)
                arrayOfSensors[posX-1][posY+1].flipACoin(probability);
            arrayOfSensors[posX][posY+1].flipACoin(probability);
            if(posX != this.length - 1)
                arrayOfSensors[posX+1][posY+1].flipACoin(probability);
        }
    }
}
