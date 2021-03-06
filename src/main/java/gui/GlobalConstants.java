package gui;

import gui.essences.BaseRobot;
import gui.essences.DataTransmitter;
import gui.essences.Evil;
import gui.essences.EvilTread;
import gui.essences.Robot;

import java.awt.*;
import java.util.Random;

public class GlobalConstants {
    private final static Random random = new Random();
    public final static int numberOfRobots = 6;
    public final static DataTransmitter globalDataTransmitter = new DataTransmitter();


    public static boolean canSetRobot(BaseRobot baseRobot)
    {
        try {
            Robot robot = (Robot)baseRobot;
            return true;
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
}
