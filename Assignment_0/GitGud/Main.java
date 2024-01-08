import java.util.*;

public class Main
{
    public static void main()
    {

    }
}

public class Clock
{
    int time;
}

public class Infiltrator {
    int posX;
    int posY;
    int startPosX;
    int startPosY;
    boolean isMoving;
    boolean hasCrossed;

    public void Infiltrator(int startPosX, int StartPosY)
    {
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.posX = startPosX;
        this.posY = startPosY;
    }

    public static int studySurrounding(Border border)
    {
        if(border.arrayOfSensors[posX][posY].isOn)
            return 0;


        if(this.posY == border.width - 1)
            return 8;

        else if(this.posY == 0)
        {
            if(this.posX == 0)
            {
                //check9
                if(!(border.arrayOfSensors[posX+1][posY+1].isOn))
                    return 9;
                //check8
                if(!(border.arrayOfSensors[posX][posY+1].isOn))
                    return 8;
                //check6
                if(!(border.arrayOfSensors[posX+1][posY].isOn))
                    return 6;
            }
            else if(this.posX == border.length - 1)
            {
                //check7
                //check8
                //check4
            }
        }
        else if(this.posX == 0)
        {
            //check9
            //check8
            //check6

            //Random
            //check3
            //check2

            // if(!(border.arrayOfSensors[posX-1][posY+1].isOn))

        }
        else if(this.posX == border.length - 1)
        {
            //check7
            //check8
            //check4

            //Random
            //check1
            //check2

            // if(!(border.arrayOfSensors[posX-1][posY+1].isOn))

        }

        else
        {
            //check7
            //check8
            //check9
            //check4
            //check6

            //Random
            //check1
            //check2
            //check3

            // if(!(border.arrayOfSensors[posX-1][posY+1].isOn))

        }
    }
}

public class Sensor {
    boolean isOn;

    public static void flipACoin(int probability)
    {
        int sample = (int) Math.random();
        if(sample <= probability)
            this.isOn = true;
        else
            this.isOn = false;
    }
}

public class Border {
    int length = 1000; //Can change later
    int width;
    Sensor[][] arrayOfSensors;

    Border(int width)
    {
        this.width = width;
        this.arrayOfSensors = new Sensor[length][width];
    }
}
