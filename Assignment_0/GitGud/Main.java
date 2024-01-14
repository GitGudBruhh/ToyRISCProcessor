import DutyCycling.*;

public class Main
{
    public static void main(String[] args)
    {
        Clock clock = new Clock();
        Border border = new Border(20);
        Infiltrator infiltrator = new Infiltrator(5,0);

        while(!infiltrator.hasCrossed)
        {
            border.updateAllSensors(probability)
            clock.incrementClockTime(1);
            int posToMove = infiltrator.studySurrounding(border);

            if(posToMove != 0)
            {
                infiltrator.isMoving == true;
                infiltrator.moveInfiltrator(posToMove);
                clock.incrementClockTime(9);
                infiltrator.isMoving == false;

                if(infiltrator.posY == border.width)
                    infiltrator.hasCrossed == true;
            }
        }

        System.out.println(clock.time);
    }
}
