import DutyCycling.*;

public class Main
{
    public static void main(String[] args)
    {
        Clock clock = new Clock();
        Border border = new Border(20);
        Infiltrator infiltrator = new Infiltrator(500,0);
        double probability = 0.2;

        while(!infiltrator.getInfiltrationStatus())
        {
            border.updateAllSensors(probability);
            clock.incrementClockTime(1);
            int posToMove = infiltrator.studySurrounding(border);
            // System.out.println(posToMove);

            if(posToMove != 0)
            {
                infiltrator.changeMotionStatus(true);
                infiltrator.moveInfiltrator(posToMove);
                infiltrator.changeMotionStatus(false);

                if(infiltrator.posY == border.width)
                {
                    infiltrator.infiltrationSuccess(true);
                    clock.incrementClockTime(9);
                    break;
                }

            }

            clock.incrementClockTime(9);
            // System.out.println(infiltrator.posX);
            // System.out.println(infiltrator.posY);

        }

        System.out.println(clock.getTime());
    }
}
