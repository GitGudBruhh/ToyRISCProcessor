import DutyCycling.*;

public class Main
{
    public static void main(String[] args)
    {
        Clock clock = new Clock();
        Border border = new Border(20);
        Infiltrator infiltrator = new Infiltrator(5,0);
        float probability = 0.2f;

        while(!infiltrator.getInfiltrationStatus())
        {
            border.updateAllSensors(probability);
            clock.incrementClockTime(1);
            int posToMove = infiltrator.studySurrounding(border);

            if(posToMove != 0)
            {
                infiltrator.changeMotionStatus(true);
                infiltrator.moveInfiltrator(posToMove);
                clock.incrementClockTime(9);
                infiltrator.changeMotionStatus(false);

                if(infiltrator.posY == border.width)
                    infiltrator.infiltrationSuccess(true);
            }
        }

        System.out.println(clock.getTime());
    }
}
