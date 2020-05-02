package gui;

import java.awt.*;

public class DataTransmitter {
    private static final Robot[] robots = new Robot[GlobalConstants.numberOfRobots];
    public static void registerRobot(int id, Robot robot)
    {
        robots[id] = robot;
    }

    public static Point getRobotCoordinate(int id)
    {
        return robots[id].getCoordinate();
    }

    public static int getRobotScore(int id)
    {
        return robots[id].getCurrentScore();
    }

    public static boolean robotScoreWasChanged(int id)
    {
        if (robots[id] == null)
            return false;
        return robots[id].isScoreUpdated();
    }

    public static boolean robotCoordinateWasChanged(int id)
    {
        if (robots[id] == null)
            return false;
        return robots[id].isScoreUpdated();
    }

    public static int numberOfRobot()
    {
        return robots.length;
    }

    public static void killRobot(int id)
    {
        robots[id] = null;
    }
}
