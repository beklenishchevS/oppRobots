package gui.essences;

import gui.GlobalConstants;

import java.util.*;
import java.util.List;

public class DataTransmitter extends Observable implements Observer {
    private static final BaseRobot[] robots = new BaseRobot[GlobalConstants.numberOfRobots];
    private static Evil evil;

    public static void registerRobot(int id, BaseRobot robot)
    {
        if (GlobalConstants.canSetRobot(robot)) {
            robots[id] = robot;
        }
    }

    public static void registerEvil()
    {
        if (DataTransmitter.evil != null)
            return;
        EvilTread evilTread = new EvilTread();
        evilTread.run();
    }

    public static void setEvil(Evil evil)
    {
        DataTransmitter.evil = evil;
    }

    public static Evil getEvil()
    {
        return evil;
    }

    public static int numberOfRobot()
    {
        return robots.length;
    }

    public static void killRobot(int id)
    {
        robots[id] = null;
    }

    public static BaseRobot getRobot(int id)
    {
        return robots[id];
    }

    @Override
    public void update(Observable observable, Object o) {
        BaseRobot robot;
        if (GlobalConstants.canSetRobot((BaseRobot)o)) {
            robot = (Robot)o;
        }
        else
        {
            return;
        }
        robots[robot.getId()] = robot;
        setChanged();
        notifyObservers(robots[robot.getId()]);
        clearChanged();
    }

    public static Set<Integer> getKilledRobots()
    {
        Set<Integer> killed = new HashSet<>();
        for (int i=0; i<robots.length; i++)
        {
            if (robots[i] == null)
            {
                killed.add(i);
            }
        }
        return killed;
    }


}
