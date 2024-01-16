import java.io.*;
import DutyCycling.*;

public class Main
{

    public static void main(String[] args)
    {
        if(args.length < 3)
        {
            System.out.println("Usage: java Main border_width sensor_probability output_file");
            return;
        }
        int width = Integer.parseInt(args[0]);
        double probability = Double.parseDouble(args[1]);

        int numberOfSimulations = 20;
        int totalTime = 0;

        for(int i = 0; i < numberOfSimulations; i++)
        {
            totalTime += simulate(width, probability);
        }

        int avgSuccessTime = totalTime/numberOfSimulations;
        System.out.println(width, probability, avgSuccessTime);
    }

    public static int simulate(int width, double probability)
    {
        Clock clock = new Clock();
        Border border = new Border(20);
        Infiltrator infiltrator = new Infiltrator(500,0);
        // double probability = 0.2;

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
        }

        System.out.println(clock.getTime());
        return clock.getTime();
    }
}
