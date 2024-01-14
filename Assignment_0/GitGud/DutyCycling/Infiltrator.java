package DutyCycling;
// import BorderPKG.Border;

public class Infiltrator {
    private int posX;
    private int posY;
    private boolean isMoving;
    private boolean hasCrossed;

    public Infiltrator(int startPosX, int startPosY)
    {
        this.posX = startPosX;
        this.posY = startPosY;
    }

    public int getPosX()
    {
        return this.posX;
    }

    public int getPosY()
    {
        return this.posY;
    }


    public boolean getInfiltrationStatus()
    {
        return this.hasCrossed;
    }

    public void infiltrationSuccess(boolean hasCrossed)
    {
        this.hasCrossed = hasCrossed;
    }

    public int studySurrounding(Border border)
    {
        if(border.arrayOfSensors[this.posX][this.posY].isOn)
            return 0;

        if(this.posY == border.width - 1)
            return 8;

        if(this.posX != 0)
            if(!(border.arrayOfSensors[this.posX-1][this.posY+1].isOn))
                return 7;
        if(!(border.arrayOfSensors[this.posX][this.posY+1].isOn))
                return 8;
        if(this.posY != border.width - 1)
            if(!(border.arrayOfSensors[this.posX+1][this.posY+1].isOn))
                return 9;

        return 0;
    }

    public void changeMotionStatus(boolean status)
    {
        this.isMoving = status;
    }

    public void moveInfiltrator(int pos)
    {
        if(pos == 7)
        {
            this.posX -= 1;
            this.posY += 1;
        }

        if(pos == 8)
        {
            this.posY += 1;
        }

        if(pos == 9)
        {
            this.posX += 1;
            this.posY += 1;
        }
    }
}
