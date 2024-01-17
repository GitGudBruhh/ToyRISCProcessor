import java.io.*;
import DutyCycling.*;

public class Main
{

    public static void main(String[] args) throws IOException
    {
        if(args.length < 3)
        {
            System.out.println("Usage: java Main border_width sensor_probability output_file");
            return;
        }

        int width = Integer.parseInt(args[0]);
        double probability = Double.parseDouble(args[1]);

        int numberOfSimulations = 5;
        int totalTime = 0;

        for(int i = 0; i < numberOfSimulations; i++)
        {
            totalTime += simulate(width, probability);
        }

        double avgSuccessTime = ((double) totalTime)/numberOfSimulations;
        // System.out.print(totalTime,avgSuccessTime,numberOfSimulations);
        // System.out.println(totalTime);
        // System.out.println(avgSuccessTime);

        FileOutputStream out = null;

        try
        {
            out = new FileOutputStream(args[2], true);

            // FileOutputStream fos = new FileOutputStream(fileName, true);
            out.write((Integer.toString(width)).getBytes());
            out.write(",".getBytes());
            out.write((Double.toString(probability)).getBytes());
            out.write(",".getBytes());
            out.write((Double.toString(avgSuccessTime)).getBytes());
            out.write("\n".getBytes());
            out.close();
        }
        finally
        {
            if(out != null)
                out.close();
        }
    }

    private static int simulate(int width, double probability)
    {
        Clock clock = new Clock();
        Border border = new Border(width);
        Infiltrator infiltrator = new Infiltrator(500,0);
        // double probability = 0.2;

        while(!infiltrator.getInfiltrationStatus())
        {
            // border.updateAllSensors(probability); // OLD SIMULATION, UPDATES ALL SENSORS
            border.updateAllSensors(probability, infiltrator.posX, infiltrator.posY); // NEW SIMUALTION
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

        // System.out.println(clock.getTime());
        return clock.getTime();
    }
}
