package gui.Essences;

import gui.GlobalConstants;

import java.util.Observable;
import java.util.Observer;

public class DataTransmitter extends Observable implements Observer {
    private static final Robot[] robots = new Robot[GlobalConstants.numberOfRobots];

    public static void registerRobot(int id, Robot robot)
    {
        robots[id] = robot;
    }

    public static int numberOfRobot()
    {
        return robots.length;
    }

    public static void killRobot(int id)
    {
        robots[id] = null;
    }

    @Override
    public void update(Observable observable, Object o) {
        Robot robot = (Robot)o;
        robots[robot.getId()] = robot;
        setChanged();
        notifyObservers(robots[robot.getId()]);
        clearChanged();
    }
}
