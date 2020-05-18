package gui;

import gui.essences.DataTransmitter;
import gui.essences.Evil;

import java.util.Random;

public class GlobalConstants {
    private final static Random random = new Random();
    public final static int numberOfRobots = 6;
    public final static DataTransmitter globalDataTransmitter = new DataTransmitter();
    public final static Evil globalEvil = new Evil(100, 100, random.nextInt(numberOfRobots));

}
